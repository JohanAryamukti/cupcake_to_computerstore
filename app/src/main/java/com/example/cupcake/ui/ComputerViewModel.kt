package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import com.example.cupcake.data.ComponentOption
import com.example.cupcake.data.ComputerDataSource
import com.example.cupcake.data.ComputerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

class ComputerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ComputerUiState())
    val uiState: StateFlow<ComputerUiState> = _uiState.asStateFlow()

    init {
        calculatePrice(_uiState.value)
    }

    fun setProcessor(option: ComponentOption) {
        _uiState.update { calculatePrice(it.copy(processor = option)) }
    }

    fun setRam(option: ComponentOption) {
        _uiState.update { calculatePrice(it.copy(ram = option)) }
    }

    fun setStorage(option: ComponentOption) {
        _uiState.update { calculatePrice(it.copy(storage = option)) }
    }

    fun setGpu(option: ComponentOption) {
        _uiState.update { calculatePrice(it.copy(gpu = option)) }
    }

    fun setMonitor(option: ComponentOption) {
        _uiState.update { calculatePrice(it.copy(monitor = option)) }
    }

    fun resetConfig() {
        val initial = ComputerUiState()
        _uiState.value = calculatePrice(initial)
    }

    private fun calculatePrice(state: ComputerUiState): ComputerUiState {
        val total = ComputerDataSource.BASE_PRICE +
                state.processor.price +
                state.ram.price +
                state.storage.price +
                state.gpu.price +
                state.monitor.price

        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
        return state.copy(totalPrice = formattedPrice)
    }
}