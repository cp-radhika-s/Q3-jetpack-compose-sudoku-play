package com.example.sudokuplay

data class SudokuTable(
    val values: List<CellData>,
) {

    fun get(row: Int, column: Int): CellData {
        return values[(row * 9) + column]
    }

    fun copy(row: Int, column: Int, cellData: CellData): SudokuTable {
        return values.toMutableList().apply {
            set((row * 9) + column, cellData)
        }.let {
            SudokuTable(it)
        }
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        operator fun invoke(vararg values: Pair<Pair<Int, Int>, Int>): SudokuTable {
            return SudokuTable(buildList {
                repeat(9) { row ->
                    repeat(9) { column ->
                        val number = values.firstOrNull {
                            it.first.first == row + 1 && it.first.second == column + 1
                        }?.second
                        add(CellData(
                            number,
                            row,
                            column
                        ))
                    }
                }
            })
        }
    }
}