package com.example.tiendamascotas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*

data class Mascota(
    val nombre: String = ""
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

    // Estado mutable para la lista de mascotas
    var listaMascotas by remember { mutableStateOf(listOf<Mascota>()) }

    // Estado para el diálogo de agregar mascota
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nombreNuevaMascota by remember { mutableStateOf(TextFieldValue("")) }

    // Cargar mascotas desde Firebase al iniciar la pantalla
    LaunchedEffect(Unit) {
        fetchMascotas { fetchedMascotas ->
            listaMascotas = fetchedMascotas
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
                    containerColor = Color(0xFF0288D1) // Color azul vibrante
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mostrarDialogo = true
                },
                containerColor = Color(0xFF0288D1), // Color del botón
                contentColor = Color.White // Color del ícono "+"
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
            // Mostrar la lista de mascotas con sombreado mejorado
            listaMascotas.forEach { mascota ->
                Text(
                    text = mascota.nombre,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(mContexto, AdoptarMascotaActivity::class.java)
                            intent.putExtra("nombreMascota", mascota.nombre)
                            mContexto.startActivity(intent)
                        }
                        .padding(vertical = 8.dp), // Espacio vertical entre los nombres
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        color = Color(0xFF0288D1), // Color azul vibrante para los nombres de mascotas
                        shadow = Shadow(
                            color = Color.Gray, // Color de la sombra
                            offset = Offset(4f, 4f), // Desplazamiento en x e y
                            blurRadius = 8f // Radio de desenfoque para suavizar la sombra
                        )
                    )
                )
            }
        }

        // Mostrar diálogo para agregar una nueva mascota
        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogo = false
                },
                title = {
                    Text(text = "Agregar Mascota", fontWeight = FontWeight.Bold)
                },
                text = {
                    OutlinedTextField(
                        value = nombreNuevaMascota,
                        onValueChange = { nombreNuevaMascota = it },
                        label = { Text("Nombre de la mascota") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nombreNuevaMascota.text.isNotBlank()) {
                                val nuevaMascota = Mascota(nombre = nombreNuevaMascota.text)
                                addMascota(nuevaMascota) // Agregar a Firebase
                                listaMascotas = listaMascotas + nuevaMascota // También agrega a la lista local
                                Toast.makeText(mContexto, "${nombreNuevaMascota.text} ha sido agregada.", Toast.LENGTH_SHORT).show()
                                nombreNuevaMascota = TextFieldValue("") // Limpiar el campo de texto
                                mostrarDialogo = false
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

// Función para cargar mascotas desde Firebase
fun fetchMascotas(onMascotasFetched: (List<Mascota>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val mascotasRef = database.getReference("Adopcion") // Base de datos "Adopcion"

    mascotasRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val mascotas = mutableListOf<Mascota>()
            for (mascotaSnapshot in snapshot.children) {
                val mascota = mascotaSnapshot.getValue(Mascota::class.java)
                if (mascota != null) {
                    mascotas.add(mascota)
                }
            }
            onMascotasFetched(mascotas) // Devuelve la lista de mascotas
        }

        override fun onCancelled(error: DatabaseError) {
            // Manejar error
        }
    })
}

// Función para agregar una nueva mascota a Firebase
fun addMascota(mascota: Mascota) {
    val database = FirebaseDatabase.getInstance()
    val mascotasRef = database.getReference("Adopcion") // Base de datos "Adopcion"

    mascotasRef.push().setValue(mascota).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Agregado exitosamente
        } else {
            // Manejar error
        }
    }
}