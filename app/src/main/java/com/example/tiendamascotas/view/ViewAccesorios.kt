package com.example.tiendamascotas.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme
import com.google.firebase.database.*

data class Pet(
    val name: String = "",
    val species: String = "",
    val age: Int = 0,
    val description: String = "",
    val adopted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccesorios() {
    val mContexto = LocalContext.current
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    // Estado mutable para la lista de mascotas
    var mascotas by remember { mutableStateOf(listOf<Pet>()) }

    // Estado para controlar si el diálogo para agregar una nueva mascota está visible
    var mostrarDialogo by remember { mutableStateOf(false) }
    // Estado para controlar el texto del nuevo nombre de mascota
    var nuevoNombreMascota by remember { mutableStateOf("") }
    var nuevoEspecieMascota by remember { mutableStateOf("") } // Nueva especie

    // Filtrar los resultados según la búsqueda
    val filteredMascotas = mascotas.filter {
        it.name.contains(searchQuery.value.text, ignoreCase = true) ||
                it.species.contains(searchQuery.value.text, ignoreCase = true)
    }

    // Mostrar el diálogo si está activo
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
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoNombreMascota.isNotEmpty() && nuevoEspecieMascota.isNotEmpty()) {
                        val newPet = Pet(name = nuevoNombreMascota, species = nuevoEspecieMascota, age = 0, description = "", adopted = false)
                        addPet(newPet) // Agrega a la base de datos
                        mascotas = mascotas + newPet // También agrega a la lista local
                        nuevoNombreMascota = "" // Limpia el campo de texto
                        nuevoEspecieMascota = "" // Limpia el campo de especie
                        mostrarDialogo = false // Cierra el diálogo
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

    // Cargar mascotas desde Firebase al iniciar la pantalla
    LaunchedEffect(Unit) {
        fetchPets { fetchedPets ->
            mascotas = fetchedPets
        }
    }

    // Resto de tu código de UI sigue igual...
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0288D1) // Cambiado a azul
                ),
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Búsqueda de Mascotas",
                            fontWeight = FontWeight.Bold,
                            color = Color.White // Cambiado a blanco
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF0288D1), // Fondo azul vibrante
                contentColor = Color.White, // Texto en blanco
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Búsqueda filtrada de mascotas",
                    fontWeight = FontWeight.Bold, // Hacer el texto en negrita
                    fontSize = 20.sp // Aumentar el tamaño de la fuente
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true }, // Abre el diálogo al presionar el FAB
                containerColor = Color(0xFF0288D1), // Color del FAB
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar nueva mascota", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(), // Asegúrate de que la columna ocupe el tamaño completo
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de búsqueda
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar mascota por nombre o especie") }
            )

            // Resultados de búsqueda filtrados
            if (filteredMascotas.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMascotas) { mascota ->
                        Text(
                            text = "${mascota.name} - ${mascota.species}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
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

// Función para cargar mascotas desde Firebase
fun fetchPets(onPetsFetched: (List<Pet>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")

    petsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val pets = mutableListOf<Pet>()
            for (petSnapshot in snapshot.children) {
                val pet = petSnapshot.getValue(Pet::class.java)
                if (pet != null) {
                    pets.add(pet)
                }
            }
            onPetsFetched(pets) // Devuelve la lista de mascotas
        }

        override fun onCancelled(error: DatabaseError) {
            // Manejar error
        }
    })
}

// Función para agregar una nueva mascota a Firebase
fun addPet(pet: Pet) {
    val database = FirebaseDatabase.getInstance()
    val petsRef = database.getReference("pets")

    petsRef.push().setValue(pet).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Agregado exitosamente
        } else {
            // Manejar error
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewAccesoriosPreview() {
    TiendaMascotasTheme {
        ViewAccesorios()
    }
}
