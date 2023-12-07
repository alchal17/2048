package com.example.a2048

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CellViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repeat(2) { viewModel.generateCell() }
        setContent {
            MainScreen()
        }
    }


    @Composable
    private fun MainScreen() {
        val minSwipe = (getMeanDp() / 40).dp
        var swipeAvailable by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    if (swipeAvailable) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val dragAmountDpX = dragAmount.x.toDp()
                            val dragAmountDpY = dragAmount.y.toDp()
                            if (dragAmountDpX > minSwipe) {
                                swipeAvailable = false
                                swipe(Swipe.RIGHT)
                            } else if (dragAmountDpX < -minSwipe) {
                                swipeAvailable = false
                                swipe(Swipe.LEFT)
                            } else if (dragAmountDpY > minSwipe) {
                                swipeAvailable = false
                                swipe(Swipe.DOWN)
                            } else if (dragAmountDpY < -minSwipe) {
                                swipeAvailable = false
                                swipe(Swipe.UP)
                            }
                        }
                    }
                }
                .fillMaxSize()
                .padding((getMeanDp() / 50).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height((getScreenWidth() * 0.95).dp)
                    .width((getScreenWidth() * 0.95).dp)
            ) {
                Grid()
            }
        }
        LaunchedEffect(!swipeAvailable){
            delay(10000)
            swipeAvailable = true
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun Grid() {
        val shape = RoundedCornerShape((getMeanDp() / 40).dp)
        val lazyGridState = rememberLazyGridState()
        LazyVerticalGrid(
            columns = GridCells.Fixed(viewModel.sideLength),
            state = lazyGridState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray, shape = shape)
                .clipToBounds()
        ) {
            items(viewModel.cells) { cell ->
                Box(
                    modifier = Modifier
                        .height((getScreenWidth() * 0.95 / viewModel.sideLength).dp)
                        .padding((getMeanDp() / 130).dp)
                        .background(
                            color = Color.Red,
                            shape = shape
                        )
                        .clipToBounds()
                ) {
                    cell?.let {
                        var visible by remember {
                            mutableStateOf(false)
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = scaleIn(animationSpec = tween(300)),
                        ) {
                            GridCell(cell = it, shape = shape)
                        }
                        LaunchedEffect(!visible) {
                            visible = true
                        }
                    }
                }
            }
        }
    }

    private fun swipe(swipe: Swipe) {
        when (swipe) {
            Swipe.DOWN -> Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show()
            Swipe.UP -> Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show()
            Swipe.LEFT -> Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show()
            Swipe.RIGHT -> Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show()
        }
        viewModel.generateCell()
    }
}

enum class Swipe {
    UP, DOWN, LEFT, RIGHT
}