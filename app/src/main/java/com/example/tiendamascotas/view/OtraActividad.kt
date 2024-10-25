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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OtraActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtén el usuario actual de Firebase Auth
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "anónimo"

        setContent {
            TiendaMascotasTheme {
                ChatScreen(chatId = "chat_id_1", currentUserEmail = currentUserEmail)
            }
        }
    }
}

@Composable
fun ChatScreen(chatId: String, currentUserEmail: String) {
    var currentMessage by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Message>()) }

    // Referencia a Firebase
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chats").child(chatId).child("messages")

    // Escuchar mensajes desde Firebase
    LaunchedEffect(Unit) {
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages = snapshot.children.mapNotNull {
                    it.getValue(Message::class.java)
                }.reversed() // Revertir para mostrar el más reciente en la parte superior
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondochat), // Cambia por tu imagen
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )

        // Contenedor de chat
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
                        sendMessage(chatId, currentUserEmail, currentMessage)
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
fun MessageBubble(message: Message) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFE1FFC7), shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Mostrar el remitente
        Text(text = message.sender, color = Color.Black.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(4.dp))
        // Mostrar el contenido del mensaje con texto negro
        Text(text = message.content, color = Color.Black) // Cambiado a color negro
    }
}

// Función para enviar un mensaje
fun sendMessage(chatId: String, sender: String, content: String) {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chats").child(chatId).child("messages")

    // Crea un objeto mensaje
    val message = Message(sender, content)

    // Enviar el mensaje usando push() para generar un ID único
    chatRef.push().setValue(message)
}

// Modelo de datos para un mensaje
data class Message(
    val sender: String = "",
    val content: String = ""
)

@Preview
@Composable
fun PreviewChatScreen() {
    TiendaMascotasTheme {
        ChatScreen(chatId = "chat_id_1", currentUserEmail = "user@example.com")
    }
}
