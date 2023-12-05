package com.example.a2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding((getMeanDp() / 50).dp)
                .clickable {
                    viewModel.generateCell()
                },
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
    }

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
                ) { cell?.let { GridCell(cell = it, shape = shape) } }
            }
        }
    }

    @Composable
    private fun Test() {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                val value = viewModel.strings
                value.add("1")
                viewModel.strings = value
                val n = 0
            }) {
            Text(text = viewModel.strings.joinToString(" "))
        }
    }
}
