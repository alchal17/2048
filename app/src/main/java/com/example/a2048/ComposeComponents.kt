package com.example.a2048

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2048.R
import java.net.Proxy.Type

@Composable
fun getScreenWidth() = LocalConfiguration.current.screenWidthDp

@Composable
fun getScreenHeight() = LocalConfiguration.current.screenHeightDp

@Composable
fun getMeanDp() = (getScreenHeight() + getScreenWidth()) / 2

@Composable
fun GridCell(cell: FilledCell, shape: Shape) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue, shape = shape), contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.value.toString(),
            style = TextStyle(
                fontSize = (getMeanDp() / 15 / cell.value.toString().length).sp,
                color = Color.Black
            ),
            fontFamily = FontFamily(Font(R.font.vina_sans_regular)),
        )
    }
}

