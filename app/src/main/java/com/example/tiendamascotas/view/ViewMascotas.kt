package com.example.tiendamascotas.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

data class Mascota(
    val id: String = "",
    val nombre: String = "",
    val especie: String = "",
    val fotoUrl: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMascotas() {
    val mContexto = LocalContext.current
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var listaMascotas by remember { mutableStateOf(listOf<Mascota>()) }
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

    val filteredMascotas = listaMascotas.filter {
        it.nombre.contains(searchQuery.value.text, ignoreCase = true) ||
                it.especie.contains(searchQuery.value.text, ignoreCase = true)
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
                        Text("Tomar Foto")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoNombreMascota.isNotEmpty() && nuevoEspecieMascota.isNotEmpty()) {
                        val nuevaMascota = Mascota(nombre = nuevoNombreMascota, especie = nuevoEspecieMascota)
                        if (selectedImageUri != null) {
                            addMascota(nuevaMascota, selectedImageUri = selectedImageUri)
                        } else if (capturedImageBitmap != null) {
                            addMascota(nuevaMascota, capturedBitmap = capturedImageBitmap)
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
        fetchMascotas { fetchedMascotas ->
            listaMascotas = fetchedMascotas
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mascotas", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0288D1))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Mascota")
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

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredMascotas) { mascota ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // Redirigir a AdoptarMascotasActivity
                                val intent = Intent(mContexto, AdoptarMascotaActivity::class.java)
                                intent.putExtra("mascotaId", mascota.id) // Pasar el ID de la mascota si es necesario
                                mContexto.startActivity(intent)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (mascota.fotoUrl.isNotEmpty()) {
                            Image(
                                painter = rememberImagePainter(mascota.fotoUrl),
                                contentDescription = "Foto de ${mascota.nombre}",
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${mascota.nombre} - ${mascota.especie}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { deleteMascota(mascota.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Eliminar mascota",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

fun fetchMascotas(onMascotasFetched: (List<Mascota>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val mascotasRef = database.getReference("mascotas")

    mascotasRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val mascotas = mutableListOf<Mascota>()
            snapshot.children.forEach { mascotaSnapshot ->
                val mascotaId = mascotaSnapshot.key ?: return
                val mascota = mascotaSnapshot.getValue(Mascota::class.java)?.copy(id = mascotaId)
                if (mascota != null) {
                    mascotas.add(mascota)
                }
            }
            onMascotasFetched(mascotas)
        }

        override fun onCancelled(error: DatabaseError) {
            println("Error fetching mascotas: ${error.message}")
        }
    })
}

fun deleteMascota(mascotaId: String) {
    val database = FirebaseDatabase.getInstance()
    val mascotaRef = database.getReference("mascotas").child(mascotaId)
    mascotaRef.removeValue().addOnSuccessListener {
        println("Mascota eliminada exitosamente")
    }.addOnFailureListener {
        println("Error al eliminar la mascota: ${it.message}")
    }
}

fun addMascota(mascota: Mascota, selectedImageUri: Uri? = null, capturedBitmap: Bitmap? = null) {
    val database = FirebaseDatabase.getInstance()
    val mascotasRef = database.getReference("mascotas")

    if (selectedImageUri != null) {
        val storageRef = FirebaseStorage.getInstance().getReference("pet_images/${UUID.randomUUID()}")
        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val mascotaConFoto = mascota.copy(fotoUrl = uri.toString())
                    mascotasRef.push().setValue(mascotaConFoto)
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
                    val mascotaConFoto = mascota.copy(fotoUrl = uri.toString())
                    mascotasRef.push().setValue(mascotaConFoto)
                }
            }
            .addOnFailureListener {
                println("Error uploading captured image: ${it.message}")
            }
    } else {
        mascotasRef.push().setValue(mascota)
    }
}
