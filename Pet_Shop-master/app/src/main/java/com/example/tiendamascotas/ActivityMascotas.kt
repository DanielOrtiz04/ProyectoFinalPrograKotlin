package com.example.Pet_Shop_master

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tiendamascotas.ui.theme.TiendaMascotasTheme
import com.example.tiendamascotas.view.ViewMascotas

class ActivityMascotas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaMascotasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ViewMascotas()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    TiendaMascotasTheme {
        ViewMascotas()
    }
}