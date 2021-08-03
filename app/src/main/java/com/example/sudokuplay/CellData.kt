package com.example.sudokuplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class CellData(
    val number: Int?,
    val row: Int,
    val column: Int,
    val attributes: Set<Attribute> = setOf(),
) {
    interface Attribute {
        @Composable
        fun Draw()
    }
}

object Selected : CellData.Attribute {
    @Composable
    override fun Draw() {
        Box(
            Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f))) {
        }
    }
}
val CellData.isSelected: Boolean get() = attributes.contains(Selected)
fun CellData.select(): CellData = copy(attributes = attributes + Selected)
fun CellData.deselect(): CellData = copy(attributes = attributes - Selected)