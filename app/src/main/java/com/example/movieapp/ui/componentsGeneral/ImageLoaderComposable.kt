package com.example.movieapp.ui.componentsGeneral

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieapp.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageLoaderComposable(modifier: Modifier, imageUrl: String, imageLabel: String) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                placeholder(R.drawable.image_placeholder_3)
                crossfade(1_000)
            }
        ),
        contentDescription = "Cover Image $imageLabel",
        modifier = modifier.background(Color.Transparent).clip(RoundedCornerShape(3.dp))
            .fillMaxWidth()
            .height(500.dp),
        contentScale = ContentScale.Crop
    )
}