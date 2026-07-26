package ru.myitschool.work.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

object Gradient {

    @Composable
    fun Gradient.Top(): Brush {
        val backgroundColor = MaterialTheme.colorScheme.background
        return Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(0.8f),
                backgroundColor.copy(0f)
            )
        )
    }

    @Composable
    fun Gradient.Bottom(): Brush {
        val backgroundColor = MaterialTheme.colorScheme.background
        return Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(0f),
                backgroundColor.copy(0.8f)
            )
        )
    }
}