package com.example.tiendamascotas.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun CuidadosLinksScreen() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Cuidados para tu mascota",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Ejemplo de enlace
        Text(
            text = "Link a cuidados de baños",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    uriHandler.openUri("https://www.ejemplo.com/banos")
                },
            textDecoration = TextDecoration.Underline
        )

        Text(
            text = "Link a cuidados de camas",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    uriHandler.openUri("https://www.ejemplo.com/camas")
                },
            textDecoration = TextDecoration.Underline
        )

        Text(
            text = "Link a cuidados de comida",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    uriHandler.openUri("https://www.ejemplo.com/comida")
                },
            textDecoration = TextDecoration.Underline
        )
    }
}
