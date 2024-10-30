package com.example.tiendamascotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Resena(
    val calificacion: Float = 0f,
    val comentario: String = "",
    val reseñador: String = ""
)

class ResenasActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("resenas")

        setContent {
            TiendaMascotasTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ResenasScreen(database)
                }
            }
        }
    }
}

@Composable
fun ResenasScreen(database: DatabaseReference) {
    var calificacion by remember { mutableStateOf(0f) }
    var comentario by remember { mutableStateOf("") }
    val reseñas = remember { mutableStateListOf<Resena>() }

    LaunchedEffect(Unit) {
        // Escuchar los datos en Firebase y actualizar la lista de reseñas
        database.get().addOnSuccessListener { snapshot ->
            reseñas.clear() // Limpiamos para evitar duplicados
            snapshot.children.forEach { child ->
                val resena = child.getValue(Resena::class.java)
                resena?.let { reseñas.add(it) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Reseñas de Mascotas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(reseñas.size) { index ->
                val resena = reseñas[index]
                ResenaItem(
                    resena = resena.comentario,
                    calificacion = resena.calificacion,
                    reseñador = resena.reseñador
                )
            }
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text("Calificación (1-5 estrellas):")
            RatingBar(rating = calificacion, onRatingChanged = { calificacion = it })

            TextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Escribe un comentario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Button(
                onClick = {
                    val nuevaResena = Resena(calificacion = calificacion, comentario = comentario, reseñador = "Usuario Anónimo")
                    database.push().setValue(nuevaResena)
                    reseñas.add(nuevaResena)
                    comentario = ""
                    calificacion = 0f
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            ) {
                Text("Agregar Reseña")
            }
        }
    }
}

@Composable
fun ResenaItem(resena: String, calificacion: Float, reseñador: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Acción futura de ver o editar reseña */ },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.resenas1), // Reemplaza con tu recurso de imagen
                contentDescription = "Imagen de mascota",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = resena,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Reseñador: $reseñador",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Calificación: ${"★".repeat(calificacion.toInt())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float, onRatingChanged: (Float) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        (1..5).forEach { index ->
            Icon(
                painter = painterResource(id = R.drawable.estrella), // Reemplaza con tu recurso de estrella
                contentDescription = "Star",
                tint = if (index <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(index.toFloat()) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResenasPreview() {
    TiendaMascotasTheme {
        ResenasScreen(FirebaseDatabase.getInstance().reference.child("resenas"))
    }
}
