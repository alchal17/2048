package com.example.a2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.pow
import com.example.a2048.ui.theme.Gray40

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
        val minSwipe = (getMeanDp() / 60).dp
        var swipeAvailable by remember { mutableStateOf(true) }
        val score = remember {
            mutableStateOf(0)
        }
        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    if (swipeAvailable) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val dragAmountDpX = dragAmount.x.toDp()
                            val dragAmountDpY = dragAmount.y.toDp()
                            if (dragAmountDpX > minSwipe && swipeAvailable) {
                                swipeAvailable = false
                                swipeRight(score)
                                viewModel.generateCell()
                            } else if (dragAmountDpX < -minSwipe && swipeAvailable) {
                                swipeAvailable = false
                                swipeLeft(score)
                                viewModel.generateCell()
                            } else if (dragAmountDpY > minSwipe && swipeAvailable) {
                                swipeAvailable = false
                                swipeDown(score)
                                viewModel.generateCell()
                            } else if (dragAmountDpY < -minSwipe && swipeAvailable) {
                                swipeAvailable = false
                                swipeUp(score)
                                viewModel.generateCell()
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
            Text(text = "Current score: ${score.value}")
        }
        LaunchedEffect(!swipeAvailable) {
            delay(300)
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
                            color = Gray40,
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


    private fun swipeUp(score: MutableState<Int>) {
        val side = viewModel.sideLength
        val cells = side.toFloat().pow(2).toInt()
        for (i in side until cells) {
            if (viewModel.cells[i - side] == null) {
                viewModel.cells[i - side] = viewModel.cells[i]
                viewModel.cells[i] = null
            } else if (viewModel.cells[i - side] == viewModel.cells[i]) {
                score.value += viewModel.cells[i]?.value ?: 0
                viewModel.cells[i - side]?.value = viewModel.cells[i - side]?.value!! * 2
                viewModel.cells[i] = null
            }
        }
    }

    private fun swipeRight(score: MutableState<Int>) {
        val side = viewModel.sideLength
        var index = 0
        repeat(side - 1) {
            for (i in index until side + index) {
                if (viewModel.cells[i + 1] == null) {
                    viewModel.cells[i + 1] = viewModel.cells[i]
                    viewModel.cells[i] = null
                } else if (viewModel.cells[i + 1] == viewModel.cells[i]) {
                    score.value += viewModel.cells[i]?.value ?: 0
                    viewModel.cells[i + 1]?.value = viewModel.cells[i + 1]?.value!! * 2
                    viewModel.cells[i] = null
                }
            }
            index += side
        }
    }

    private fun swipeLeft(score: MutableState<Int>) {
        val side = viewModel.sideLength
        var index = 0
        repeat(side) {
            for (i in side + index - 1 downTo index + 1) {
                if (viewModel.cells[i - 1] == null) {
                    viewModel.cells[i - 1] = viewModel.cells[i]
                    viewModel.cells[i] = null
                } else if (viewModel.cells[i - 1] == viewModel.cells[i]) {
                    score.value += viewModel.cells[i]?.value ?: 0
                    viewModel.cells[i - 1]?.value = viewModel.cells[i - 1]?.value!! * 2
                    viewModel.cells[i] = null
                }
            }
            index += side
        }
    }

    private fun swipeDown(score: MutableState<Int>) {
        val side = viewModel.sideLength
        val cells = side.toFloat().pow(2).toInt()
        for (i in 0 until cells - side) {
            if (viewModel.cells[i + side] == null) {
                viewModel.cells[i + side] = viewModel.cells[i]
                viewModel.cells[i] = null
            } else if (viewModel.cells[i + side] == viewModel.cells[i]) {
                score.value += viewModel.cells[i]?.value ?: 0
                viewModel.cells[i + side]?.value = viewModel.cells[i + side]?.value!! * 2
                viewModel.cells[i] = null
            }
        }
    }
}
