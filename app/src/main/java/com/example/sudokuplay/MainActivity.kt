package com.example.sudokuplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sudokuplay.ui.theme.SudokuPlayTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudokuPlayTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Sudoku()
                }
            }
        }
    }

    @Composable
    private fun Sudoku() {
        val initialTable = SudokuTable(
            Pair(2, 3) to 4,
            Pair(8, 9) to 5,
        )
        val viewModel = remember {
            SudokuViewModel(initialTable)
        }

        CompositionLocalProvider(
            LocalSudokuViewModel provides viewModel
        ){
            Column(Modifier
                .padding(32.dp)) {

                SudokuTableView(Modifier
                    .weight(2.5f)
                    .align(Alignment.CenterHorizontally))
                ControlerView(
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally),
                )
            }

        }

    }

    @Composable
    private fun SudokuTableView(modifier: Modifier = Modifier) {

        val viewModel = LocalSudokuViewModel.current
        val sudokuTable = viewModel.getTable().collectAsState().value
        BoxWithConstraints(
            modifier
                .background(MaterialTheme.colors.background)
        ) {

            val width = minOf(maxWidth, maxHeight) / 9
            val height = width
            Row() {
                repeat(3) { boxRow ->
                    Column() {
                        repeat(3) { boxColumn ->
                            val boxIndex = (boxColumn * 3) + boxRow
                            Box(
                                Modifier
                                    .width(width * 3)
                                    .height(height * 3)
                            ) {
                                SudokuBox(boxIndex = boxIndex)
                            }
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun SudokuBox(boxIndex: Int) {
        val rowStart = boxIndex / 3
        val columnStart = boxIndex % 3
        val sudokuTable = LocalSudokuViewModel.current.getTable().collectAsState().value
        BoxWithConstraints(Modifier
            .fillMaxSize()
            .border(2.dp, MaterialTheme.colors.onBackground)
        ) {
            val width = maxWidth / 3
            val height = maxHeight / 3

            Row() {
                repeat(3) { repeatedColumn ->
                    Column() {
                        repeat(3) { repeatedRow ->
                            val column = (columnStart * 3) + repeatedColumn
                            val row = (rowStart * 3) + repeatedRow
                            val sudokuCellValues = sudokuTable.get(row, column)
                            /*  Text("$row $column",
                                  Modifier.size(100.dp),
                                  fontSize = MaterialTheme.typography.h4.fontSize)*/
                            Box(Modifier.size(width, height)) {
                                SudokuCell(sudokuCellValues)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SudokuCell(cellData: CellData) {
        val viewModel = LocalSudokuViewModel.current
        val color =
            animateColorAsState(targetValue = if (cellData.isSelected) MaterialTheme.colors.primaryVariant.copy(
                alpha = 0.2f) else Color.Unspecified)
        Box(Modifier
            .fillMaxSize()
            .border(1.dp, Color.Gray)
            .background(color.value)
            .clickable { viewModel.clicked(cellData) }

        ) {
            val actualText = cellData.number?.toString() ?: ""
            cellData.attributes.forEach {
                it.Draw()
            }
            Text(
                actualText,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.h5.fontSize,

                )
        }
    }

}
