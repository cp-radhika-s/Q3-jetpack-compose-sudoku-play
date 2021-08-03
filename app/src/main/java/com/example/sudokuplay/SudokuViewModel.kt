package com.example.sudokuplay

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal val LocalSudokuViewModel =
    compositionLocalOf { SudokuViewModel(SudokuTable()) }


class SudokuViewModel(private val sudokuTable: SudokuTable) {

    private val table = MutableStateFlow(sudokuTable)
    fun getTable(): StateFlow<SudokuTable> = table

    private fun updateCell(
        cellData: CellData,
        newCellData: CellData,
    ) {
        val state = table.value
        val newState =
            state.copy(cellData.row, cellData.column, newCellData)
        table.value = newState
    }

    fun clicked(cellData: CellData) {
        val foundCell = table.value.get(cellData.row, cellData.column)
        val newSudokuCell = if (foundCell.isSelected) {
            cellData.deselect()
        } else {
            cellData.select()
        }
        updateCell(cellData, newSudokuCell)
    }

    fun onNumberPressed(number: Int) {
        val newNumbers =
            table.value.values.map {
                if (it.isSelected) {
                    it.copy(number = number)
                } else {
                    it
                }
            }
        table.value = table.value.copy(newNumbers)
    }

    fun clear() {
        val newNumbers =
            table.value.values.map { it.deselect() }
        table.value = table.value.copy(newNumbers)
    }

    fun delete() {
        clear()
        table.value = sudokuTable
    }
}