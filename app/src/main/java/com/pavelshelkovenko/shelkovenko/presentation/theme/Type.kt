package com.pavelshelkovenko.shelkovenko.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pavelshelkovenko.shelkovenko.R


val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, weight = FontWeight.Normal),
    Font(R.font.roboto_light, weight = FontWeight.Light),
    Font(R.font.roboto_medium, weight = FontWeight.Medium),
    Font(R.font.roboto_bold, weight = FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    labelSmall = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),
    displayLarge = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
)