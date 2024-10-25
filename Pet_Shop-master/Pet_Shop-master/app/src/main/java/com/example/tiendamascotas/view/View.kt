package com.example.tiendamascotas.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamascotas.PantallaNo2
import com.example.tiendamascotas.R
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme

@Composable
fun Elementos() {
val mContext = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(painter = painterResource(id = R.drawable.imagenlogin),
            contentDescription = "petShop")

        Text("Pet shop",
            color = Color.Gray,
            fontSize = 48.sp
        )
        Text("Bienvenido",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.width(30.dp))

        //Botones pantalla principal log y otros
        Row () {
            OutlinedButton(
                onClick = {
                mContext.startActivity(Intent(mContext,
                    PantallaNo2::class.java))
            }) {
                Text("Login",
                    color = Color.Yellow
                )
            }
            //agregar 2do boton

        }

//agregar mas

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TiendaMascotasTheme {
        Elementos()
    }
}