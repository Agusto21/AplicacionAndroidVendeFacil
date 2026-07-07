package com.tuusuario.vendefacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tuusuario.vendefacil.core.navigation.AppNavigation
import com.tuusuario.vendefacil.presentation.theme.VendeFacilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VendeFacilTheme {
                val appContainer = (application as VendeFacilApp).container
                AppNavigation(appContainer = appContainer)
            }
        }
    }
}