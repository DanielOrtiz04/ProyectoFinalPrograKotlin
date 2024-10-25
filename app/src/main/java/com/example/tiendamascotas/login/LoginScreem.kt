package com.example.tiendamascotas.login

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.R
import com.example.tiendamascotas.PantallaNo2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(false) }
    var isValidPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)) // Fondo celeste claro
    ) {
        Image(
            painter = painterResource(id = R.drawable.mascotas_felices1),
            contentDescription = "Imagen de fondo de mascotas",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Superposición de color semitransparente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido",
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5),
                    letterSpacing = 1.5.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de texto para el email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = !isValidEmail && email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isValidPassword = password.length >= 6
                },
                label = { Text(text = "Contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Ver contraseña")
                    }
                },
                isError = !isValidPassword && password.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    if (isValidEmail && isValidPassword) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    context.startActivity(Intent(context, PantallaNo2::class.java))
                                } else {
                                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Email o contraseña inválidos", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isValidEmail && isValidPassword,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .shadow(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para registrar un nuevo usuario
            Button(
                onClick = {
                    if (isValidEmail && isValidPassword) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                                    context.startActivity(Intent(context, PantallaNo2::class.java))
                                } else {
                                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Email o contraseña inválidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .shadow(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)) // Mismo color que Iniciar Sesión
            ) {
                Text(
                    text = "Registrar Usuario",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Texto en blanco
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}