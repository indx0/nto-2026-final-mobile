package ru.myitschool.work.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.appShadow(
    corner: Dp = 32.dp
): Modifier {
    return this.shadow(
        elevation = 4.dp,
        ambientColor = MaterialTheme.colorScheme.outline,
        spotColor = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(corner)
    )
}

@Composable
fun Modifier.appOutline(
    corner: Dp = 32.dp
): Modifier {
    return this.border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(corner)
    )
}