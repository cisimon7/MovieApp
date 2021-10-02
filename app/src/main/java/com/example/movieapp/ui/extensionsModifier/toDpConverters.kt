package com.example.movieapp.ui.extensionsModifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

@Composable
fun Offset.pxToDp() = with(LocalDensity.current) { x.toDp() to y.toDp() }

@Composable
fun IntSize.pxToDp() = with(LocalDensity.current) { width.toDp() to height.toDp() }