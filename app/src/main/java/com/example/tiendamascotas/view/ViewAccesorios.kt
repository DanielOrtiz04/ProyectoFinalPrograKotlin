package com.example.tiendamascotas.view

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

data class Pet(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val age: Int = 0,
    val description: String = "",
    val adopted: Boolean = false,
    val photoUrl: String = "",
    val location: String = "",
    val adoptionDate: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccesorios() {
    val mContexto = LocalContext.current
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var mascotas by remember { mutableStateOf(listOf<Pet>()) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nuevoNombreMascota by remember { mutableStateOf("") }
    var nuevoEspecieMascota by remember { mutableStateOf("") }
    var nuevoLugarMascota by remember { mutableStateOf("") }
    var nuevaFechaMascota by remember { mutableStateOf("") }
    var nuevaDescripcionMascota by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        capturedImageBitmap = bitmap
    }

    val filteredMascotas = mascotas.filter {
        it.name.contains(searchQuery.value.text, ignoreCase = true) ||
                it.species.contains(searchQuery.value.text, ignoreCase = true)
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Agregar nueva mascota") },
            text = {
                Column {
                    TextField(
                        value = nuevoNombreMascota,
                        onValueChange = { nuevoNombreMascota = it },
                        label = { Text("Nombre de mascota") }
                    )
                    TextField(
                        value = nuevoEspecieMascota,
                        onValueChange = { nuevoEspecieMascota = it },
                        label = { Text("Raza de mascota") }
                    )
                    TextField(
                        value = nuevoLugarMascota,
                        onValueChange = { nuevoLugarMascota = it },
                        label = { Text("Ultima vez visto") }
                    )
                    TextField(
                        value = nuevaFechaMascota,
                        onValueChange = { nuevaFechaMascota = it },
                        label = { Text("Fecha") }
                    )
                    TextField(
                        value = nuevaDescripcionMascota,
                        onValueChange = { nuevaDescripcionMascota = it },
                        label = { Text("Breve Descripción") },
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { galleryLauncher.launch("image/*") }) {
                        Text("Seleccionar Foto")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { cameraLauncher.launch() }) {
                        Text("Tomar Foto ")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoNombreMascota.isNotEmpty() && nuevoEspecieMascota.isNotEmpty()) {
                        val newPet = Pet(
                            name = nuevoNombreMascota,
                            species = nuevoEspecieMascota,
                            description = nuevaDescripcionMascota,
                            location = nuevoLugarMascota,
                            adoptionDate = nuevaFechaMascota
                        )
                        if (selectedImageUri != null) {
                            addPet(newPet, selectedImageUri = selectedImageUri)
                        } else if (capturedImageBitmap != null) {
                            addPet(newPet, capturedBitmap = capturedImageBitmap)
                        }
                        nuevoNombreMascota = ""
                        nuevoEspecieMascota = ""
                        nuevoLugarMascota = ""
                        nuevaFechaMascota = ""
                        nuevaDescripcionMascota = ""
                        selectedImageUri = null
                        capturedImageBitmap = null
                        mostrarDialogo = false
                    }
                }) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        fetchPets { fetchedPets ->
            mascotas = fetchedPets
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0288D1)
                ),
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Búsqueda de Mascotas",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true },
                containerColor = Color(0xFF0288D1),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar nueva mascota", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar mascota por nombre o especie") }
            )

            if (filteredMascotas.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMascotas) { mascota ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (mascota.photoUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberImagePainter(mascota.photoUrl),
                                    contentDescription = "Foto de ${mascota.name}",
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nombre: ${mascota.name}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Especie: ${mascota.species}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Ultima vez visto: ${mascota.location}",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Fecha: ${mascota.adoptionDate}",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Descripción: ${mascota.description}",
                                    fontSize = 14.sp,
                                    maxLines = 3
                                )
                            }

                            IconButton(onClick = { deletePet(mascota.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Eliminar mascota",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "No hay resultados para la búsqueda.",
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun fetchPets(onPetsFetched: (List<Pet>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")

    petsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val pets = mutableListOf<Pet>()
            snapshot.children.forEach { petSnapshot ->
                val petId = petSnapshot.key ?: return
                val pet = petSnapshot.getValue(Pet::class.java)?.copy(id = petId)
                if (pet != null) {
                    pets.add(pet)
                }
            }
            onPetsFetched(pets)
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

fun addPet(pet: Pet, selectedImageUri: Uri? = null, capturedBitmap: Bitmap? = null) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")
    val petId = petsRef.push().key ?: return

    if (selectedImageUri != null) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("fotos_mascotas/${UUID.randomUUID()}.jpg")
        storageRef.putFile(selectedImageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                petsRef.child(petId).setValue(pet.copy(id = petId, photoUrl = uri.toString()))
            }
        }
    } else if (capturedBitmap != null) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("fotos_mascotas/${UUID.randomUUID()}.jpg")
        val baos = ByteArrayOutputStream()
        capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                petsRef.child(petId).setValue(pet.copy(id = petId, photoUrl = uri.toString()))
            }
        }
    } else {
        petsRef.child(petId).setValue(pet.copy(id = petId))
    }
}

fun deletePet(petId: String) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")
    petsRef.child(petId).removeValue()
}

@Preview(showBackground = true)
@Composable
fun ViewMascotasPreview() {
    TiendaMascotasTheme {
        ViewAccesorios()
    }
}
