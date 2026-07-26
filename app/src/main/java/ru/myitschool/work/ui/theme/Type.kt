package ru.myitschool.work.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.myitschool.work.R

// Set of Material typography styles to start with

val InterDisplay = FontFamily(
    Font(
        resId = R.font.inter_display_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.inter_display_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.inter_display_medium_italic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    )
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = InterDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = InterDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),

    /* bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ) */

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)