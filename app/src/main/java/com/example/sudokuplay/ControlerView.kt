package com.example.sudokuplay

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlin.math.max

@OptIn(ExperimentalUnitApi::class)

@Composable
fun ControlerView(
    modifier: Modifier = Modifier,
) {

    Row(
        modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f)
                .clip(CutCornerShape(8.dp))
                .background(MaterialTheme.colors.primaryVariant.copy(0.2f))
        ) {
            ControlBox()

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .offset(16.dp)
        ) {
            val viewModel = LocalSudokuViewModel.current
            Button(onClick = {
                viewModel.clear()
            }) {
                Text(text = "Clear Cell")
            }
            Spacer(modifier = Modifier.size(24.dp))
            Button(onClick = {
                viewModel.delete()
            }) {
                Text(text = "Reset")
            }
        }

    }
}

@Composable
fun NumberInputTypeButton(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelection: () -> Unit,
    content: @Composable () -> Unit,
) {
    val backgroundColor =
        animateColorAsState(
            targetValue = if (isSelected) MaterialTheme.colors.primary.copy(
                alpha = 0.2f
            ) else Color.Unspecified
        ).value
    val borderColor = backgroundColor.copy(backgroundColor.alpha + 0.1f)

    Row(
        modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                onSelection()
            }
            .border(1.dp, borderColor)
            .background(backgroundColor)
            .size(32.dp)
            .padding(2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        content()
    }
}

@Composable
fun ControlBox() {
    val viewModel = LocalSudokuViewModel.current
    FixedGrid(columnCount = 3, Modifier.clickable { }) {
        repeat(9) {
            val number = it + 1
            NumberPadButton(
                number = number
            ) {
                viewModel.onNumberPressed(number)
            }
        }
    }
}

@Composable
fun FixedGrid(columnCount: Int, modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(modifier = modifier,
        content = children,
        measurePolicy = { measurables, constraints ->
            val width = constraints.maxWidth / columnCount
            val placeables = measurables.map {
                it.measure(
                    constraints.copy(
                        minWidth = width,
                        maxWidth = width
                    )
                )
            }
            layout(constraints.maxWidth, constraints.maxHeight) {
                var x = 0
                var y = 0
                var maxHeight = 0
                placeables.forEachIndexed { index, placeable ->
                    if (index % columnCount == 0) {
                        x = 0
                        y = maxHeight
                    }
                    placeable.placeRelative(x, y)
                    x += placeable.width
                    maxHeight = max(maxHeight, y + placeable.height)

                }
            }
        })
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun NumberPadButton(number: Int, onClick: (Int) -> Unit) {
    Box(Modifier.padding(5.dp)) {

        Button(modifier = Modifier.aspectRatio(1f),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(2.dp),
            onClick = { onClick(number) }) {
            Text(
                text = number.toString(),
                fontSize = TextUnit(24f, TextUnitType.Sp)
            )
        }
    }
}
