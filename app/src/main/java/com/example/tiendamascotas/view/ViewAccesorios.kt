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
    val photoUrl: String = ""
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
                        label = { Text("Especie de mascota") }
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
                        val newPet = Pet(name = nuevoNombreMascota, species = nuevoEspecieMascota)
                        if (selectedImageUri != null) {
                            addPet(newPet, selectedImageUri = selectedImageUri)
                        } else if (capturedImageBitmap != null) {
                            addPet(newPet, capturedBitmap = capturedImageBitmap)
                        }
                        nuevoNombreMascota = ""
                        nuevoEspecieMascota = ""
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
                            Text(
                                text = "${mascota.name} - ${mascota.species}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
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

        override fun onCancelled(error: DatabaseError) {
            println("Error fetching pets: ${error.message}")
        }
    })
}

fun deletePet(petId: String) {
    val database = FirebaseDatabase.getInstance()
    val petRef = database.getReference("pets").child(petId)
    petRef.removeValue().addOnSuccessListener {
        println("Mascota eliminada exitosamente")
    }.addOnFailureListener {
        println("Error al eliminar la mascota: ${it.message}")
    }
}

fun addPet(pet: Pet, selectedImageUri: Uri? = null, capturedBitmap: Bitmap? = null) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")

    if (selectedImageUri != null) {
        val storageRef = FirebaseStorage.getInstance().getReference("pet_images/${UUID.randomUUID()}")
        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val petWithPhoto = pet.copy(photoUrl = uri.toString())
                    petsRef.push().setValue(petWithPhoto)
                }
            }
            .addOnFailureListener {
                println("Error uploading image from gallery: ${it.message}")
            }
    } else if (capturedBitmap != null) {
        val storageRef = FirebaseStorage.getInstance().getReference("pet_images/${UUID.randomUUID()}")
        val baos = ByteArrayOutputStream()
        capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageRef.putBytes(data)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val petWithPhoto = pet.copy(photoUrl = uri.toString())
                    petsRef.push().setValue(petWithPhoto)
                }
            }
            .addOnFailureListener {
                println("Error uploading image from camera: ${it.message}")
            }
    } else {
        petsRef.push().setValue(pet)
    }
}

@Preview(showBackground = true)
@Composable
fun ViewAccesoriosPreview() {
    TiendaMascotasTheme {
        ViewAccesorios()
    }
}
