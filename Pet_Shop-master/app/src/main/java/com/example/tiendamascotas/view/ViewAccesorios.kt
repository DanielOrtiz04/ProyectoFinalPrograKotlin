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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ActivityAccesorios
import com.example.tiendamascotas.ActivityMascotas
import com.example.tiendamascotas.EmptyViewActivity
import com.example.tiendamascotas.ListActivity
import com.example.tiendamascotas.R
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccesorios() {

    val mContexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("Accesorios para mascotas")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "La vida con mascotas",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Cama para mascotas", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura según tus necesidades
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cama1para1mascotas),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            mContexto.startActivity(
                                Intent(mContexto, ListActivity::class.java)
                            )
                        }
                        .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
                )
            }
            Text(text = "Cepillo para mascotas", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura según tus necesidades
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cepillo1para1mascotas),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
                        .clickable {
                            mContexto.startActivity(
                                Intent(mContexto, ListActivity::class.java)
                            )
                        }
                )
            }
            Text(text = "Collar para mascotas", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.collar1para1mascotas),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
                        .clickable {
                            mContexto.startActivity(
                                Intent(mContexto, ListActivity::class.java)
                            )
                        }
                )
            }
        }
    }
}

