package com.practicapdmparalelo.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.aristidevs.practicapdmparalelo.R
import com.practicapdmparalelo.model.Genero

import com.practicapdmparalelo.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAñadirUsuario(navController: NavController, viewModel: UsuarioViewModel) {

    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf(Genero.MASCULINO) }
    // valor por defecto

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.añadir_usuario),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Campo de texto para nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        if (it.all { c -> c.isLetter() || c.isWhitespace() }) {
                            nombre = it
                        }
                    },
                    label = { Text(stringResource(R.string.nombre)) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de texto para edad
                OutlinedTextField(
                    value = edad,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() }) {
                            edad = it
                        }
                    },
                    label = { Text( stringResource(R.string.edad))},
                    modifier = Modifier.fillMaxWidth()
                )

                // Selección de género (RadioButtons)
                Text(stringResource(R.string.genero))
                Row {
                    RadioButton(
                        selected = (generoSeleccionado == Genero.MASCULINO),
                        onClick = { generoSeleccionado = Genero.MASCULINO }
                    )
                    Text(stringResource(R.string.generoH), modifier = Modifier.clickable {
                        generoSeleccionado = Genero.MASCULINO
                    })

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = (generoSeleccionado == Genero.FEMENINO),
                        onClick = { generoSeleccionado = Genero.FEMENINO }
                    )
                    Text(stringResource(R.string.generoM), modifier = Modifier.clickable {
                        generoSeleccionado = Genero.FEMENINO
                    })
                }
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón de Guardar
                Button(
                    onClick = {
                        viewModel.guardarDatos(nombre, edad, generoSeleccionado)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(viewModel.mensajeSnackBar)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = stringResource(R.string.guardar_btn))
                }

                // Botón para Volver
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = stringResource(R.string.volver))
                }
            }
        }
    }
}