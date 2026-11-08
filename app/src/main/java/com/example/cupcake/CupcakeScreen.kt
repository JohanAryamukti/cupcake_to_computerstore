package com.example.cupcake

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.ComponentOption
import com.example.cupcake.data.ComputerDataSource
import com.example.cupcake.data.ComputerUiState
import com.example.cupcake.ui.ComputerViewModel

enum class ComputerScreen(val title: String) {
    ProcessorRam(title = "1. Prosesor & RAM"),
    StorageGpu(title = "2. Storage & Kartu Grafis"),
    Monitor(title = "3. Ukuran Monitor"),
    Summary(title = "Ringkasan Pesanan")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComputerAppBar(
    currentScreen: ComputerScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Computer Store Johan Arya",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentScreen.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    viewModel: ComputerViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ComputerScreen.valueOf(
        backStackEntry?.destination?.route ?: ComputerScreen.ProcessorRam.name
    )

    Scaffold(
        topBar = {
            ComputerAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ComputerScreen.ProcessorRam.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Halaman 1: 1. Prosesor & RAM
            composable(route = ComputerScreen.ProcessorRam.name) {
                SelectionStepScreen(
                    section1Title = "Pilih Jenis Prosesor",
                    options1 = ComputerDataSource.processorOptions,
                    selectedOption1 = uiState.processor,
                    onOption1Selected = { viewModel.setProcessor(it) },
                    section2Title = "Pilih Ukuran Memori (RAM)",
                    options2 = ComputerDataSource.ramOptions,
                    selectedOption2 = uiState.ram,
                    onOption2Selected = { viewModel.setRam(it) },
                    totalPrice = uiState.totalPrice,
                    onNextButtonClicked = { navController.navigate(ComputerScreen.StorageGpu.name) },
                    onCancelButtonClicked = { resetAndGoToStart(viewModel, navController) }
                )
            }

            // Halaman 2: 2. Storage & Kartu Grafis
            composable(route = ComputerScreen.StorageGpu.name) {
                SelectionStepScreen(
                    section1Title = "Pilih Ukuran & Tipe Hardisk/SSD",
                    options1 = ComputerDataSource.storageOptions,
                    selectedOption1 = uiState.storage,
                    onOption1Selected = { viewModel.setStorage(it) },
                    section2Title = "Pilih Tipe Kartu Grafis (VGA)",
                    options2 = ComputerDataSource.gpuOptions,
                    selectedOption2 = uiState.gpu,
                    onOption2Selected = { viewModel.setGpu(it) },
                    totalPrice = uiState.totalPrice,
                    onNextButtonClicked = { navController.navigate(ComputerScreen.Monitor.name) },
                    onCancelButtonClicked = { resetAndGoToStart(viewModel, navController) }
                )
            }

            // Halaman 3: 3. Ukuran Monitor
            composable(route = ComputerScreen.Monitor.name) {
                SingleSelectionStepScreen(
                    sectionTitle = "Pilih Ukuran Monitor",
                    options = ComputerDataSource.monitorOptions,
                    selectedOption = uiState.monitor,
                    onOptionSelected = { viewModel.setMonitor(it) },
                    totalPrice = uiState.totalPrice,
                    onNextButtonClicked = { navController.navigate(ComputerScreen.Summary.name) },
                    onCancelButtonClicked = { resetAndGoToStart(viewModel, navController) }
                )
            }

            // Halaman 4: Ringkasan Spesifikasi & Total Harga
            composable(route = ComputerScreen.Summary.name) {
                val context = LocalContext.current
                ComputerSummaryScreen(
                    uiState = uiState,
                    onCancelButtonClicked = { resetAndGoToStart(viewModel, navController) },
                    onShareButtonClicked = { summaryText ->
                        shareOrder(context, summaryText)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectionGroup(
    title: String,
    options: List<ComponentOption>,
    selectedOption: ComponentOption,
    onOptionSelected: (ComponentOption) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) }
                    )
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option.name,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SelectionStepScreen(
    section1Title: String,
    options1: List<ComponentOption>,
    selectedOption1: ComponentOption,
    onOption1Selected: (ComponentOption) -> Unit,
    section2Title: String,
    options2: List<ComponentOption>,
    selectedOption2: ComponentOption,
    onOption2Selected: (ComponentOption) -> Unit,
    totalPrice: String,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            SelectionGroup(section1Title, options1, selectedOption1, onOption1Selected)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SelectionGroup(section2Title, options2, selectedOption2, onOption2Selected)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Perkiraan Harga Sementara: $totalPrice",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                Text("Cancel")
            }
            Button(modifier = Modifier.weight(1f), onClick = onNextButtonClicked) {
                Text("Next")
            }
        }
    }
}

@Composable
fun SingleSelectionStepScreen(
    sectionTitle: String,
    options: List<ComponentOption>,
    selectedOption: ComponentOption,
    onOptionSelected: (ComponentOption) -> Unit,
    totalPrice: String,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            SelectionGroup(sectionTitle, options, selectedOption, onOptionSelected)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Perkiraan Harga Sementara: $totalPrice",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                Text("Cancel")
            }
            Button(modifier = Modifier.weight(1f), onClick = onNextButtonClicked) {
                Text("Next")
            }
        }
    }
}

@Composable
fun ComputerSummaryScreen(
    uiState: ComputerUiState,
    onCancelButtonClicked: () -> Unit,
    onShareButtonClicked: (String) -> Unit
) {
    val summaryText = """
        === SPECIFICATION COMPUTER STORE JOHAN ARYA ===
        • Prosesor: ${uiState.processor.name}
        • Memori (RAM): ${uiState.ram.name}
        • Storage: ${uiState.storage.name}
        • Kartu Grafis: ${uiState.gpu.name}
        • Monitor: ${uiState.monitor.name}
        --------------------------------------
        TOTAL PERKIRAAN HARGA: ${uiState.totalPrice}
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Rincian Kustomisasi Toko Komputer",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("PROSESOR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(uiState.processor.name)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("MEMORI (RAM)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(uiState.ram.name)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("STORAGE (HARDISK/SSD)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(uiState.storage.name)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("KARTU GRAFIS (VGA)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(uiState.gpu.name)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("MONITOR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(uiState.monitor.name)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "TOTAL PERKIRAAN HARGA:\n${uiState.totalPrice}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onShareButtonClicked(summaryText) }
            ) {
                Text("Kirim / Bagikan Pesanan")
            }
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCancelButtonClicked
            ) {
                Text("Cancel")
            }
        }
    }
}

private fun resetAndGoToStart(
    viewModel: ComputerViewModel,
    navController: NavHostController
) {
    viewModel.resetConfig()
    navController.popBackStack(ComputerScreen.ProcessorRam.name, inclusive = false)
}

private fun shareOrder(context: Context, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Penawaran Computer Store Johan Arya")
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(intent, "Kirim Spesifikasi Komputer")
    )
}