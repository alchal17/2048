package com.example.a2048

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.math.ln

@Composable
fun getScreenWidth() = LocalConfiguration.current.screenWidthDp

@Composable
fun getScreenHeight() = LocalConfiguration.current.screenHeightDp

@Composable
fun getMeanDp() = (getScreenHeight() + getScreenWidth()) / 2

@Composable
fun GridCell(cell: FilledCell, shape: Shape) {
    var switch by remember { mutableStateOf(false) }
    val bgColor: Color by animateColorAsState(
        if (switch) Color(
            red = 0,
            green = 256 / 33 * cell.value,
            blue = 0
        ) else Color.Black, animationSpec = tween(500)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = bgColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.value.toString(),
            style = TextStyle(
                fontSize = (getMeanDp() / 15 / cell.value.toString().length).sp,
                color = Color.White
            ),
            fontFamily = FontFamily(Font(R.font.vina_sans_regular)),
        )
    }
    LaunchedEffect(!switch) {
        switch = true
    }
}

