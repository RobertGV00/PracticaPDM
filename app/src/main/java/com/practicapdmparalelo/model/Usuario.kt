package com.practicapdmparalelo.model

enum class Genero {
    MASCULINO, FEMENINO
}

data class Usuario(
    val nombre: String,
    var edad: Int,
    val genero: Genero
)
