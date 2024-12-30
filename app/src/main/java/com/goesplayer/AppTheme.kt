package com.goesplayer

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFFFFD600),
            background = Color.Black,
            onPrimary = Color.Black,
        ),
        typography = Typography(
            displayLarge = MaterialTheme.typography.displayLarge.copy(color = Color.White),
            displayMedium = MaterialTheme.typography.displayMedium.copy(color = Color.White),
            displaySmall = MaterialTheme.typography.displaySmall.copy(color = Color.White),
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            titleLarge = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            titleMedium = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            titleSmall = MaterialTheme.typography.titleSmall.copy(color = Color.White),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            bodyMedium = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            bodySmall = MaterialTheme.typography.bodySmall.copy(color = Color.White),
            labelLarge = MaterialTheme.typography.labelLarge.copy(color = Color.White),
            labelMedium = MaterialTheme.typography.labelMedium.copy(color = Color.White),
            labelSmall = MaterialTheme.typography.labelSmall.copy(color = Color.White),
        ),
        content = content
    )
}