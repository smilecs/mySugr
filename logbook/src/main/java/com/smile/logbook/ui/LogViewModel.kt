package com.smile.logbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smile.logbook.domain.BgUnit
import com.smile.logbook.domain.IoDispatcher
import com.smile.logbook.domain.Log
import com.smile.logbook.domain.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal const val COEFFICIENT = 18.0182

@HiltViewModel
class LogViewModel @Inject constructor(
    private val repository: LogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<UiLogNote?>(null)
    val stateFlow: StateFlow<UiLogNote?> = _stateFlow

    private val logCache = mutableListOf<Log>()

    private val _bgValueInput = MutableStateFlow("")
    val bgValue: StateFlow<String> = _bgValueInput

    private var defaultBgUnit: BgUnit = BgUnit.Mg
    private var selectedBgUnit: BgUnit = defaultBgUnit

    private val _selectedUnit = MutableStateFlow<BgUnit>(defaultBgUnit)
    val selectedUnit: StateFlow<BgUnit> = _selectedUnit

    init {
        getLogNote()
    }

    fun onCheckChanged(newUnit: BgUnit) {
        _selectedUnit.value = newUnit
        selectedBgUnit = newUnit
        changeUnit(selectedBgUnit)
    }

    fun updateBgValueInput(input: String) {
        _bgValueInput.value = input
    }

    fun saveLog() {
        viewModelScope.launch(dispatcher) {
            val newValue = bgValue.value.trim()
            if (newValue.isNotBlank()) {
                val numericValue = newValue.toDoubleOrNull()
                numericValue?.let {
                    val convertedValue = when (selectedBgUnit) {
                        BgUnit.Mg -> newValue.toDouble()
                        BgUnit.MmoL -> mmToMg(numericValue.toDouble())
                    }
                    repository.insertEntity(
                        Log(
                            value = convertedValue,
                            bg = BgUnit.Mg
                        )
                    )
                    updateBgValueInput("")
                }
            }
        }
    }

    private fun changeUnit(unit: BgUnit) {
        viewModelScope.launch(dispatcher) {
            selectedBgUnit = unit
            mapAndEmitLog(logCache)
        }
    }

    private fun getLogNote() {
        viewModelScope.launch(dispatcher) {
            repository.getAllEntries().collect {
                logCache.clear()
                logCache.addAll(it)
                mapAndEmitLog(it)
            }
        }
    }

    private fun mapAndEmitLog(logs: List<Log>) {
        val bgValues = logs.map {
            if (it.bg != selectedBgUnit) {
                convertBgUnit(it)
            } else {
                it.value
            }
        }

        val average = if (bgValues.isEmpty()) {
            null
        } else {
            bgValues.average().toString()
        }

        val uiLogNote = UiLogNote(
            averageValue = average,
            bgValues = bgValues,
            bgCheckBox = listOf(
                BgUnit.Mg,
                BgUnit.MmoL
            )
        )
        _stateFlow.value = uiLogNote
    }

    private fun convertBgUnit(log: Log): Double {
        return when (log.bg) {
            BgUnit.Mg -> mgToMm(log.value)
            BgUnit.MmoL -> mmToMg(log.value)
        }
    }

    private fun mgToMm(value: Double): Double {
        val result = value / COEFFICIENT
        return BigDecimal(result)
            .setScale(4, RoundingMode.HALF_UP)
            .toDouble()
    }

    private fun mmToMg(value: Double): Double {
        val result = value * COEFFICIENT
        return BigDecimal(result)
            .setScale(4, RoundingMode.HALF_UP)
            .toDouble()
    }
}
