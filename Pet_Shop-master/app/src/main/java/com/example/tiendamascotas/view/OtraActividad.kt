package com.example.tiendamascotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

class OtraActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaMascotasTheme {
                ChatScreen() // Manteniendo el chat funcional en OtraActividad
            }
        }
    }
}

@Composable
fun ChatScreen() {
    var currentMessage by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<String>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondochat), // Reemplaza 'fondo' con el nombre de tu imagen
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )

        // Fondo para el chat
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Mostrar lista de mensajes
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages.size) { index ->
                    // Resaltar el mensaje
                    MessageBubble(message = messages[index])
                }
            }

            // Caja de entrada para escribir mensajes
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BasicTextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(56.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                        ) {
                            if (currentMessage.isEmpty()) {
                                Text(text = "Escribe un mensaje...")
                            }
                            innerTextField()
                        }
                    }
                )
                Button(onClick = {
                    if (currentMessage.isNotEmpty()) {
                        messages = listOf(currentMessage) + messages // Agregar el nuevo mensaje al principio
                        currentMessage = "" // Limpiar el campo de entrada
                    }
                }) {
                    Text(text = "Enviar")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: String) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFE1FFC7), shape = MaterialTheme.shapes.medium) // Color de fondo similar a WhatsApp
            .padding(12.dp) // Padding interno del burbuja
            .fillMaxWidth(), // Asegurarse de que la burbuja ocupe todo el ancho
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = message, color = Color.Black) // Color del texto
    }
}

@Preview
@Composable
fun PreviewChatScreen() {
    TiendaMascotasTheme {
        ChatScreen()
    }
}
