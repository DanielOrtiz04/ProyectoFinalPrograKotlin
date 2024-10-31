package com.example.tiendamascotas.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

data class Mascota(
    val nombre: String = "",
    val fotoUrl: String? = null
)

class ViewMascotasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewMascotas()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMascotas() {
    val mContexto = LocalContext.current
    var listaMascotas by remember { mutableStateOf(listOf<Mascota>()) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nombreNuevaMascota by remember { mutableStateOf(TextFieldValue("")) }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        fetchMascotas { fetchedMascotas ->
            listaMascotas = fetchedMascotas
        }
    }

    val tomarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (!success) {
            Log.e("CameraError", "Error al tomar la foto")
            Toast.makeText(mContexto, "Error al tomar la foto", Toast.LENGTH_SHORT).show()
        } else {
            Log.i("CameraSuccess", "Foto tomada exitosamente")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pet Garden",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0288D1)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mostrarDialogo = true
                },
                containerColor = Color(0xFF0288D1),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Mascota")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listaMascotas.forEach { mascota ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(mContexto, AdoptarMascotaActivity::class.java)
                            intent.putExtra("nombreMascota", mascota.nombre)
                            mContexto.startActivity(intent)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mascota.fotoUrl != null) {
                        Image(
                            painter = rememberImagePainter(mascota.fotoUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp) // Ajusta el tamaño según sea necesario
                                .clip(RoundedCornerShape(4.dp)), // Aplica el recorte aquí
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mascota.nombre,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            color = Color(0xFF0288D1),
                            shadow = Shadow(
                                color = Color.Gray,
                                offset = Offset(4f, 4f),
                                blurRadius = 8f
                            )
                        )
                    )
                }
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogo = false
                },
                title = {
                    Text(text = "Agregar Mascota", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombreNuevaMascota,
                            onValueChange = { nombreNuevaMascota = it },
                            label = { Text("Nombre de la mascota") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                // Verificar permisos antes de intentar tomar la foto
                                if (ContextCompat.checkSelfPermission(mContexto, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    fotoUri = getTempImageUri(mContexto)
                                    Log.i("CameraLaunch", "Lanzando el intent para tomar foto con URI: $fotoUri")
                                    tomarFotoLauncher.launch(fotoUri)
                                } else {
                                    // Si no se tienen permisos, solicitarlos
                                    ActivityCompat.requestPermissions(mContexto as Activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                                }
                            }
                        ) {
                            Text("Tomar Foto")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nombreNuevaMascota.text.isNotBlank()) {
                                if (fotoUri != null) {
                                    val nuevaMascota = Mascota(nombre = nombreNuevaMascota.text)
                                    addMascotaConFoto(mContexto, nuevaMascota, fotoUri) { fotoUrl ->
                                        if (fotoUrl != null) {
                                            listaMascotas = listaMascotas + nuevaMascota.copy(fotoUrl = fotoUrl)
                                            Toast.makeText(mContexto, "Mascota agregada con éxito.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Log.e("AddMascotaError", "Error al agregar la mascota.")
                                            Toast.makeText(mContexto, "Error al agregar la mascota.", Toast.LENGTH_SHORT).show()
                                        }
                                        // Limpiar los campos después de agregar la mascota
                                        nombreNuevaMascota = TextFieldValue("")
                                        fotoUri = null
                                        mostrarDialogo = false
                                    }
                                } else {
                                    Toast.makeText(mContexto, "Por favor toma una foto primero.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(mContexto, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                    ) {
                        Text("Agregar", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { mostrarDialogo = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                }
            )
        }
    }
}

fun addMascotaConFoto(
    context: Context,
    mascota: Mascota,
    fotoUri: Uri?,
    onSuccess: (String?) -> Unit
) {
    if (fotoUri != null) {
        val storageRef = FirebaseStorage.getInstance().reference.child("fotos_mascotas/${System.currentTimeMillis()}.jpg")
        Log.i("StorageUpload", "Subiendo foto a Firebase Storage: ${fotoUri.toString()}")
        storageRef.putFile(fotoUri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                Log.i("UploadSuccess", "Foto subida exitosamente, URL: ${uri.toString()}")
                val database = FirebaseDatabase.getInstance().getReference("Adopcion")
                val nuevaMascota = mascota.copy(fotoUrl = uri.toString())
                database.push().setValue(nuevaMascota).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(uri.toString())
                        Toast.makeText(context, "${mascota.nombre} ha sido agregada.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("DatabaseError", "Error al agregar la mascota a la base de datos: ${task.exception?.message}")
                        onSuccess(null)
                        Toast.makeText(context, "Error al agregar la mascota a la base de datos.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("UploadError", "Error al subir la foto: ${exception.message}")
                onSuccess(null)
                Toast.makeText(context, "Error al subir la foto.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun fetchMascotas(onFetchComplete: (List<Mascota>) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("Adopcion")
    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val mascotas = mutableListOf<Mascota>()
            for (data in snapshot.children) {
                val mascota = data.getValue(Mascota::class.java)
                if (mascota != null) {
                    mascotas.add(mascota)
                }
            }
            onFetchComplete(mascotas)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("DatabaseError", "Error al recuperar mascotas: ${error.message}")
            onFetchComplete(emptyList())
        }
    })
}

fun getTempImageUri(context: Context): Uri {
    val tempFile = File(context.cacheDir, "temp_image.jpg")
    return FileProvider.getUriForFile(context, context.packageName + ".provider", tempFile)
}

private const val CAMERA_PERMISSION_REQUEST_CODE = 1001