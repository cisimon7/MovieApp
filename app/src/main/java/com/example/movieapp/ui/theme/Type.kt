package com.example.movieapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.movieapp.R

private val YakoneKaffeesatz = FontFamily(
    Font(R.font.yanone_kaffeesatz_regular),
    Font(R.font.yanone_kaffeesatz_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.yanone_kaffeesatz_light, weight = FontWeight.Light),
    Font(R.font.yanone_kaffeesatz_medium, weight = FontWeight.Medium),
    Font(R.font.yanone_kaffeesatz_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.yanone_kaffeesatz_bold, weight = FontWeight.Bold)
)

private val RobotoCondensed = FontFamily(
    Font(R.font.roboto_condensed_regular),
    Font(R.font.roboto_condensed_light, weight = FontWeight.Light),
    Font(R.font.roboto_condensed_italic, style = FontStyle.Italic),
    Font(R.font.roboto_condensed_bold, weight = FontWeight.Bold),
    Font(R.font.roboto_condensed_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.roboto_condensed_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic)
)

val MovieAppTypography = Typography(

    defaultFontFamily = RobotoCondensed,

    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    caption = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic
    ),
    h3 = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF101820)
    ),
    h4 = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    h5 = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        color = Color.Black,
        fontSize = 18.sp,
        fontStyle=FontStyle.Normal,
        fontWeight = FontWeight.Normal
    )
)