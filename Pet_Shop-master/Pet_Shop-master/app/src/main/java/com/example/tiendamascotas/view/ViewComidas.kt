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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.ActivityAccesorios
import com.example.tiendamascotas.ListActivity
import com.example.tiendamascotas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewComidas() {

    val mContexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("Comidas para mascotas")
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
                    text = "La mejor comida",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Comida para gatos", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura según tus necesidades
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat2food),
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
            Text(text = "Comida para perros", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura según tus necesidades
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog2food),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
                        .clickable { mContexto.startActivity(
                            Intent(mContexto, ListActivity::class.java)
                        ) }
                )
            }
            Text(text = "Comida para peces", fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del Box
                    .padding(16.dp), // Espaciado interno
                contentAlignment = Alignment.Center, // Centra el contenido
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fish3food),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
                        .clickable { mContexto.startActivity(
                            Intent(mContexto, ListActivity::class.java)
                        ) }
                )
            }
        }
    }
}
