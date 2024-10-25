package com.example.Pet_Shop_master.carritocompras

import java.io.Serializable

data class Producto(
    var idProducto: String,
    var nomProducto: String,
    var descripcion: String,
    var precio: Double
): Serializable
