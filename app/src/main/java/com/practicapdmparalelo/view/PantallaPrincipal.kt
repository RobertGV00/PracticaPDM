package com.practicapdmparalelo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aristidevs.practicapdmparalelo.R
import com.practicapdmparalelo.model.Genero
import com.practicapdmparalelo.model.Usuario
import com.practicapdmparalelo.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavController, viewModel: UsuarioViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var mostrarDialogo = remember { mutableStateOf(false) }
    var usuarioAEliminar = remember { mutableStateOf<Usuario?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.titulo_perfil),
                        style = MaterialTheme.typography.titleLarge, // Texto más grande
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                // Colores de la barra
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        // FAB extendido (ícono + texto)
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("pantalla_añadir_usuario")
                },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.icono_navegar)
                    )
                },
                text = { Text(text = stringResource(R.string.añadir_usuario)) },
                containerColor = MaterialTheme.colorScheme.secondary
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        // Mostrar SnackBar si hay mensaje
        LaunchedEffect(viewModel.mensajeSnackBar) {
            if (viewModel.mensajeSnackBar.isNotEmpty()) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(viewModel.mensajeSnackBar)
                }
                viewModel.limpiarMensajeSnackBar()
            }
        }

        // Lista de usuarios
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.listaUsuarios) { usuario ->
                UsuarioCard(
                    usuario = usuario,
                    onEliminar = {
                        usuarioAEliminar.value = usuario
                        mostrarDialogo.value = true
                    }
                )
            }
        }

        // Diálogo de confirmación de borrado
        if (mostrarDialogo.value) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo.value = false },
                title = { Text(stringResource(R.string.confirmacion)) },
                text = { Text(stringResource(R.string.pregunta_confirmacion)) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.borrarUsuario(usuarioAEliminar.value!!)
                        usuarioAEliminar.value = null
                        mostrarDialogo.value = false

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Usuario eliminado con éxito")
                        }
                    }) {
                        Text(stringResource(R.string.si))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo.value = false }) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }
    }
}

/**
 * Composable que dibuja la tarjeta de un usuario para tener un codigo mas limpio y visual.
 */
@Composable
fun UsuarioCard(usuario: Usuario, onEliminar: () -> Unit) {
    // Elegir recurso según el género
    val avatarRes = if (usuario.genero == Genero.MASCULINO) {
        R.drawable.avatar_hombre
    } else {
        R.drawable.avatar_mujer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = stringResource(R.string.imagenperfil),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.nombre_label, usuario.nombre))
                Text(text = stringResource(R.string.edad_label, usuario.edad))
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.eliminarusuario),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEliminar() }
            )
        }
    }
}

