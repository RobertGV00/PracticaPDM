package com.practicapdmparalelo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.practicapdmparalelo.model.Genero
import com.practicapdmparalelo.model.Usuario

class UsuarioViewModel : ViewModel() {

    // Lista de Usuarios
    var listaUsuarios = mutableStateListOf(
        Usuario("Sergio", 42,Genero.MASCULINO),
        Usuario("Carlos", 38,Genero.MASCULINO),
        Usuario("Robert", 25,Genero.MASCULINO),
        Usuario("Andrea", 35,Genero.FEMENINO),
        Usuario("Laur", 28,Genero.MASCULINO)
    )
        private set

    // Estado del nombre del usuario:
    var nombre  = ""
        private set

    // Estado de la edad del usuario:
    var edad = ""
        private set

    // Estado del mensaje Snackbar
    var mensajeSnackBar by mutableStateOf("")
        private set

    // Función para actualizar el nombre:
    fun actualizarNombre(nuevoNombre: String) {
        if (nuevoNombre.all { it.isLetter() || it.isWhitespace() }) {
            nombre = nuevoNombre
        }
    }

    // Función para actualizar la edad:
    fun actualizarEdad(nuevaEdad: String) {
        if (nuevaEdad.all { it.isDigit() }) {
            edad = nuevaEdad
        }
    }

    // Guardar los datos:
    fun guardarDatos(nombre: String, edad: String, genero: Genero) {
        // Validar que el nombre no esté vacío y solo contenga letras y espacios
        if (nombre.isBlank() || !nombre.all { it.isLetter() || it.isWhitespace() }) {
            mensajeSnackBar = "El nombre no puede estar vacío y solo debe contener letras"
            return
        }

        try {
            val edadInt = edad.toInt()
            if (edadInt !in 0..120) {
                mensajeSnackBar = "La edad debe estar entre 0 y 120 años"
                return
            }

            if (listaUsuarios.any { it.nombre.equals(nombre, ignoreCase = true) }) {
                mensajeSnackBar = "El usuario $nombre ya existe"
                return
            }

            // Aquí se crea el usuario usando el género pasado como parámetro,
            // que puede ser Genero.MASCULINO o Genero.FEMENINO
            listaUsuarios.add(Usuario(nombre = nombre, edad = edadInt, genero = genero))
            mensajeSnackBar = "Usuario $nombre creado con éxito"

            // Limpiar los campos después de guardar
            this.nombre = ""
            this.edad = ""

        } catch (e: NumberFormatException) {
            mensajeSnackBar = "La edad debe ser un número válido"
        }
    }



    // Función para borrar un usuario:
    fun borrarUsuario(usuario: Usuario) {
        listaUsuarios.remove(usuario)

        // Actualizar mensaje del Snackbar
        mensajeSnackBar = "Usuario ${usuario.nombre} eliminado"
    }

    // Limpiar el mensaje del Snackbar
    fun limpiarMensajeSnackBar() {
        mensajeSnackBar = ""
    }
}
