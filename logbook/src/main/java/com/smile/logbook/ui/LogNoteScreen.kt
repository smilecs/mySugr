package com.smile.logbook.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smile.logbook.domain.BgUnit

@Composable
fun LogNoteScreen(viewModel: LogViewModel) {
    val state = viewModel.stateFlow.collectAsState().value
    val selectedUnit = viewModel.selectedUnit.collectAsState().value
    val bgValue = viewModel.bgValue.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        state?.let {
            LogNoteStateLess(
                data = it,
                selectedUnit = selectedUnit,
                textValue = bgValue,
                onCheckChanged = { unit ->
                    viewModel.onCheckChanged(unit)
                },
                onTextValueChanged = { text ->
                    viewModel.updateBgValueInput(text)
                },
                onClick = {
                    viewModel.saveLog()
                }
            )
        }
    }
}

@Composable
fun LogNoteStateLess(
    data: UiLogNote,
    selectedUnit: BgUnit,
    textValue: String,
    onCheckChanged: (BgUnit) -> Unit,
    onTextValueChanged: (String) -> Unit,
    onClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Your average is ${data.averageValue ?: 0}",
                    fontSize = 16.sp
                )
            }
        }

        item {
            BgUnitComponentSelector(
                bgList = data.bgCheckBox,
                selectedUnit = selectedUnit,
                textValue = textValue,
                onCheckChanged = onCheckChanged,
                onTextValueChanged = onTextValueChanged,
                onClick = onClick
            )
        }

        items(data.bgValues) {
            Text(
                text = "${it.toString()} ${selectedUnit.unit}",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun BgUnitComponentSelector(
    bgList: List<BgUnit>,
    selectedUnit: BgUnit,
    textValue: String,
    onCheckChanged: (BgUnit) -> Unit,
    onTextValueChanged: (String) -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        bgList.forEach { bgUnit ->
            BgUnitCheckBox(
                bgUnit = bgUnit,
                checked = bgUnit == selectedUnit,
                onCheckChanged = onCheckChanged
            )
        }

        TextField(
            label = {
                Text(
                    text = selectedUnit.unit,
                    fontSize = 16.sp
                )
            },
            onValueChange = {
                onTextValueChanged(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = textValue,
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onClick
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun BgUnitCheckBox(
    bgUnit: BgUnit,
    checked: Boolean,
    onCheckChanged: (BgUnit) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckChanged(bgUnit) }
        )

        Text(
            text = bgUnit.unit,
            fontSize = 12.sp
        )
    }
}
