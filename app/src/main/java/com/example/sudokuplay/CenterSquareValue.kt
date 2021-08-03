package com.example.sudokuplay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

data class CenterSquareValue(val values: Set<Int>) : CellData.Attribute {
    @OptIn(ExperimentalUnitApi::class)
    @Composable
    override fun Draw() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                values.sorted().joinToString(""),
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = TextUnit(9f, TextUnitType.Sp),

                )
        }
    }
}

fun CellData.toggleCenterValue(value: Int): CellData {
    val currentValues =
        attributes.filterIsInstance<CenterSquareValue>().firstOrNull()?.values.orEmpty().let {
            if (it.contains(value)) {
                it - value
            } else {
                it + value
            }
        }
    return removeCenterValues().let {
        it.copy(attributes = it.attributes + CenterSquareValue(currentValues.toSet()))
    }
}

fun CellData.removeCenterValues(): CellData {
    return copy(attributes = attributes.filterNot { it is CenterSquareValue }.toSet())
}