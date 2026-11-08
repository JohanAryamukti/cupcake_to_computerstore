package com.example.cupcake.data

data class ComputerUiState(
    val processor: ComponentOption = ComputerDataSource.processorOptions[0],
    val ram: ComponentOption = ComputerDataSource.ramOptions[0],
    val storage: ComponentOption = ComputerDataSource.storageOptions[0],
    val gpu: ComponentOption = ComputerDataSource.gpuOptions[0],
    val monitor: ComponentOption = ComputerDataSource.monitorOptions[0],
    val totalPrice: String = "Rp 4.000.000"
)