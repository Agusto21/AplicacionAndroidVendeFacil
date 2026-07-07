package com.tuusuario.vendefacil.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = VendeFacilBlue,
    background = BackgroundLight,
    surface = CardWhite,
    onPrimary = CardWhite,
    onBackground = TextDark
)

@Composable
fun VendeFacilTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColorScheme, content = content)
}