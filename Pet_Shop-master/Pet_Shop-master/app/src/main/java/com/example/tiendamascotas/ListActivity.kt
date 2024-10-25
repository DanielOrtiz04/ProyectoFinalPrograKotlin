package com.example.Pet_Shop_master

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiendamascotas.lazycolum.Articulo
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

class ListActivity : ComponentActivity() {

    val listado : List<Articulo> = listOf(
        Articulo(R.drawable.icons8mascotas48," Más sobre Mascotas", "comercio al por menor dedicado a la venta de productos para mascotas como alimentos, juguetes, snacks, cepillos, y accesorios para mascotas en general."),
        Articulo(R.drawable.icons8pettoys,"Más sobre Accesorios", "¡Variedad De Artículos En Un Solo Lugar! encuentra los mejores articulos para tu mascota"),
        Articulo(R.drawable.icons8petfood64,"Más sobre Alimentación", "Variedad de alimentos para tu mascota desde procesados a naturales"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaMascotasTheme (false){
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Pantalla(listado)
                }
            }
        }
    }
}

@Composable
fun Pantalla (listado: List< Articulo >){
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        items(listado){
                item -> ListItemRow(item)
        }
    }
}
@Composable
fun ListItemRow(item: Articulo){

    var masInfo = remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ){

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){

            Image(painter = painterResource(id = item.image),
                contentDescription = item.details)

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(70, 0, LinearEasing)
                    )
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (masInfo.value)
                    Text(
                        text = item.details,
                    )


            }

            IconButton(onClick = {masInfo.value = !masInfo.value}) {

                Icon(
                    Icons.Filled.ArrowDropDown,
                    "Icono expandir o colapzar",
                    Modifier.rotate(
                        if(masInfo.value) 180f else 360f
                    )
                )
            }

            /*
            OutlinedButton(onClick = {
                masInfo.value = !masInfo.value
            }) {
                Text( if(masInfo.value)"Ocultar" else "Más...")
            }
            */
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPreview(){
    val listado : List<Articulo> = listOf(
        Articulo(R.drawable.icons8mascotas48," Más sobre Mascotas", "comercio al por menor dedicado a la venta de productos para mascotas como alimentos, juguetes, snacks, cepillos, y accesorios para mascotas en general."),
        Articulo(R.drawable.icons8pettoys,"Más sobre Accesorios", "¡Variedad De Artículos En Un Solo Lugar! encuentra los mejores articulos para tu mascota"),
        Articulo(R.drawable.icons8petfood64,"Más sobre Alimentación", "Variedad de alimentos para tu mascota desde procesados a naturales"),
    )
    TiendaMascotasTheme(true) {
        Pantalla(listado)
    }
}