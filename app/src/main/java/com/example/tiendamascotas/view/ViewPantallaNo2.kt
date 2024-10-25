package com.example.tiendamascotas.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ActivityAccesorios
import com.example.tiendamascotas.ActivityComidas
import com.example.tiendamascotas.ActivityMascotas
//import com.example.tiendamascotas.ListActivity
import com.example.tiendamascotas.OtraActividad
import com.example.tiendamascotas.R
import com.example.tiendamascotas.ResenasActivity
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffolPrincipal2() {
    val mContexto = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)) // Fondo celeste claro
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.mascotas_felices1),
            contentDescription = "Imagen de fondo de mascotas",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().shadow(8.dp).graphicsLayer {
                alpha = 0.8f // Para hacer la imagen más opaca
            }
        )

        // Capa semitransparente encima de la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Fondo semitransparente
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color(0xFFB3E5FC)), // Fondo azul claro
                    title = {
                        Text(
                            text = "Pet Garden",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 11.dp),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color(0xFF1E88E5) // Azul intenso para el texto
                            )
                        )
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.background(Color(0xFF0288D1)), // Fondo azul más intenso
                    contentColor = Color.White,
                ) {
                    // Primer botón "Ir a Reseñas"
                    Button(
                        onClick = {
                            mContexto.startActivity(Intent(mContexto, ResenasActivity::class.java))
                        },
                        modifier = Modifier
                            .weight(1f) // Ajuste para distribuir los botones uniformemente
                            .padding(8.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp)) // Sombra para resaltar el botón
                            .clip(RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF81C784), // Fondo verde vibrante
                            contentColor = Color.White // Color del texto del botón
                        )
                    ) {
                        Text(
                            text = "Ir a Reseñas",
                            fontSize = 16.sp, // Aumentar tamaño de la fuente
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Segundo botón que redirige a otra actividad
                    Button(
                        onClick = {
                            mContexto.startActivity(Intent(mContexto, OtraActividad::class.java)) // Asegúrate de que OtraActividad sea la actividad correcta
                        },
                        modifier = Modifier
                            .weight(1f) // Ajuste para distribuir los botones uniformemente
                            .padding(8.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp)) // Sombra para resaltar el botón
                            .clip(RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF81C784), // Fondo verde vibrante
                            contentColor = Color.White // Color del texto del botón
                        )
                    ) {
                        Text(
                            text = "Chat",
                            fontSize = 16.sp, // Aumentar tamaño de la fuente
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            // floatingActionButton = {
            // FloatingActionButton(
            // onClick = {
            //     mContexto.startActivity(Intent(mContexto, ListActivity::class.java))
            // },
            // modifier = Modifier.padding(16.dp)
            // ) {
            //     Icon(Icons.Default.Add, contentDescription = "Add")
            //  }
            //}
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ItemCard(
                    title = "Busqueda y Reporte",
                    imageResource = R.drawable.accesorios2,
                    onClick = {
                        mContexto.startActivity(
                            Intent(mContexto, ActivityAccesorios::class.java)
                        )
                    }
                )
                ItemCard(
                    title = "Cuidado de Mascotas",
                    imageResource = R.drawable.comida,
                    onClick = {
                        mContexto.startActivity(
                            Intent(mContexto, ActivityComidas::class.java)
                        )
                    }
                )
                ItemCard(
                    title = "Adopción",
                    imageResource = R.drawable.mascotas,
                    onClick = {
                        mContexto.startActivity(
                            Intent(mContexto, ActivityMascotas::class.java)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ItemCard(title: String, imageResource: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFB3E5FC), shape = RoundedCornerShape(16.dp)) // Fondo azul claro
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
        )
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
fun GreetingPreview2() {
    TiendaMascotasTheme {
        ScaffolPrincipal2()
    }
}