package com.example.tiendamascotas.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ListActivity
import com.example.tiendamascotas.ListActivity2
import com.example.tiendamascotas.ListActivity3
import com.example.tiendamascotas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewComidas() {
    val mContexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0288D1) // Cambiado a azul
                ),
                title = {
                    Text(
                        "Los mejores cuidados",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold, // Negrita para el título
                        color = Color.White // Cambiado a blanco
                    )
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
                    text = "Cuidado de Calidad",
                    fontWeight = FontWeight.Bold, // Hacer el texto en negrita
                    fontSize = 20.sp // Aumentar el tamaño de la fuente
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp), // Espaciado uniforme
        ) {
            // Los mejores baños
            Text(
                text = "Los mejores baños",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold // Negrita para el texto
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // Altura ajustada
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        mContexto.startActivity(
                            Intent(mContexto, ListActivity::class.java)
                        )
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .shadow(4.dp, RoundedCornerShape(16.dp)) // Sombra suave
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banos),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp) // Tamaño de la imagen ajustado
                )
            }

            // Las mejores camas
            Text(
                text = "Las mejores camas",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold // Negrita para el texto
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // Altura ajustada
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        mContexto.startActivity(
                            Intent(mContexto, ListActivity2::class.java)
                        )
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .shadow(4.dp, RoundedCornerShape(16.dp)) // Sombra suave
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cama),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp) // Tamaño de la imagen ajustado
                )
            }

            // La mejor comida
            Text(
                text = "La mejor comida",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold // Negrita para el texto
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // Altura ajustada
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        mContexto.startActivity(
                            Intent(mContexto, ListActivity3::class.java)
                        )
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .shadow(4.dp, RoundedCornerShape(16.dp)) // Sombra suave
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.alimentoprro),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp) // Tamaño de la imagen ajustado
                )
            }
        }
    }
}
