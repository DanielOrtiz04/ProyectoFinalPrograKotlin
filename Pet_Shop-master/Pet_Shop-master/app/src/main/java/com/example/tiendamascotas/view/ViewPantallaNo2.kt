package com.example.tiendamascotas.view

import android.content.Intent
import android.service.autofill.OnClickAction
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.tiendamascotas.ListActivity
import com.example.tiendamascotas.PantallaNo2
import com.example.tiendamascotas.R
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffolPrincipal2() {
    val mContexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
                title = {
                    Text(
                        text = "Pet Shop",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 11.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                contentColor = MaterialTheme.colorScheme.secondary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "CARRITO DE COMPRAS",
                )

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mContexto.startActivity(Intent(mContexto, ListActivity::class.java))
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ItemCard(
                title = "Accesorios",
                imageResource = R.drawable.accesorios2,
                onClick = {
                    mContexto.startActivity(
                        Intent(mContexto, ActivityAccesorios::class.java)
                    )
                }
            )
            ItemCard(
                title = "Comida",
                imageResource = R.drawable.comida,
                onClick = {
                    mContexto.startActivity(
                        Intent(mContexto, ActivityComidas::class.java)
                    )
                }
            )
            ItemCard(
                title = "Mascotas",
                imageResource = R.drawable.mascotas,
                onClick = {
                    mContexto.startActivity(
                    Intent(mContexto, ActivityMascotas::class.java)
                ) }
            )
        }
    }
}

@Composable
fun ItemCard(title: String, imageResource: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
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
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
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