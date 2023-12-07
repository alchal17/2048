package com.example.a2048

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class CellViewModel : ViewModel() {
    val sideLength = 4

    val cells =
        List<FilledCell?>((sideLength.toFloat().pow(2)).toInt()) { null }.toMutableStateList()
    var strings by mutableStateOf(mutableListOf("0"))


    fun generateCell() {
        val nullIndices = cells.indices.filter { index -> cells[index] == null }
        if (nullIndices.isNotEmpty()) {
            val randomIndex = nullIndices.random()
            cells[randomIndex] = FilledCell()
        }
    }

    fun generateValue(a: String) {
        val value = this.strings
        value.add(a)
        this.strings = value
    }
}