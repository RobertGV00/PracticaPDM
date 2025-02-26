package com.practicapdmparalelo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicapdmparalelo.view.PantallaAñadirUsuario
import com.practicapdmparalelo.view.PantallaPrincipal
import com.practicapdmparalelo.view.ui.theme.PracticapdmparaleloTheme
import com.practicapdmparalelo.viewmodel.UsuarioViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticapdmparaleloTheme  {
                val navController = rememberNavController()

                // Compartir una única instancia de UsuarioViewModel
                val usuarioViewModel: UsuarioViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "pantalla_principal"
                ) {
                    composable("pantalla_principal") {
                        PantallaPrincipal(navController, usuarioViewModel)
                    }
                    composable("pantalla_añadir_usuario") {
                        PantallaAñadirUsuario(navController, usuarioViewModel)
                    }
                }
            }
        }
    }
}
