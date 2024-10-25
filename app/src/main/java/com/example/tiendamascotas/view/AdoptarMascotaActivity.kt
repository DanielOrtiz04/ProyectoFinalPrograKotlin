package com.example.tiendamascotas.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import com.google.firebase.database.FirebaseDatabase

data class Adopcion(
    val nombreMascota: String = "",
    val nombreUsuario: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val motivo: String = ""
)

class AdoptarMascotaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombreMascota = intent.getStringExtra("nombreMascota")

        setContent {
            AdoptarMascotaScreen(nombreMascota ?: "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptarMascotaScreen(nombreMascota: String) {
    val mContexto = LocalContext.current

    // Variables para el formulario
    var nombreUsuario by remember { mutableStateOf(TextFieldValue("")) }
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var telefono by remember { mutableStateOf(TextFieldValue("")) }
    var motivo by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Adopta a $nombreMascota",
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Información de la mascota $nombreMascota",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            // Campo de texto para ingresar nombre del usuario
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para dirección
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para motivo de adopción
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo de adopción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3 // Permitir más de una línea
            )

            // Botón de "Adoptar"
            Button(
                onClick = {
                    if (nombreUsuario.text.isNotBlank() && direccion.text.isNotBlank() &&
                        telefono.text.isNotBlank() && motivo.text.isNotBlank()
                    ) {
                        val adopcion = Adopcion(
                            nombreMascota = nombreMascota,
                            nombreUsuario = nombreUsuario.text,
                            direccion = direccion.text,
                            telefono = telefono.text,
                            motivo = motivo.text
                        )
                        addAdopcion(adopcion) // Agregar adopción a Firebase
                        Toast.makeText(
                            mContexto,
                            "¡Felicidades ${nombreUsuario.text}, has adoptado a $nombreMascota!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            mContexto,
                            "Por favor, completa todos los campos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
            ) {
                Text("Adoptar", color = Color.White)
            }
        }
    }
}

// Función para agregar una adopción a Firebase
fun addAdopcion(adopcion: Adopcion) {
    val database = FirebaseDatabase.getInstance()
    val adopcionesRef = database.getReference("Adopciones") // Base de datos "Adopciones"

    adopcionesRef.push().setValue(adopcion).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Agregado exitosamente
        } else {
            // Manejar error
        }
    }
}