package com.example.tiendamascotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

class ListActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaMascotasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAlimento()
                }
            }
        }
    }
}

@Composable
fun PantallaAlimento() {
    val uri = "https://www.youtube.com/watch?v=_CcxqCB6GZY"
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondolinks2), // Cambia por tu imagen de fondo
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f)), // Fondo semi-transparente
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Top las mejores Alimentaciones", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.alimentoprro),
                contentDescription = "Alimento",
                modifier = Modifier
                    .size(72.dp)
                    .clickable { uriHandler.openUri(uri) } // Abre el enlace
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaAlimentoPreview() {
    PantallaAlimento()
}
