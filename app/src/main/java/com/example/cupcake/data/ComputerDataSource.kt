package com.example.cupcake.data

data class ComponentOption(
    val name: String,
    val price: Long
)

object ComputerDataSource {
    // Harga dasar Chassis / Laptop Body
    const val BASE_PRICE = 4_000_000L

    val processorOptions = listOf(
        ComponentOption("Intel Core i3-13100 / Ryzen 3 7320U", 0L),
        ComponentOption("Intel Core i5-13400 / Ryzen 5 7600X", 1_800_000L),
        ComponentOption("Intel Core i7-13700 / Ryzen 7 7700X", 3_800_000L),
        ComponentOption("Intel Core i9-13900K / Ryzen 9 7900X", 6_500_000L)
    )

    val ramOptions = listOf(
        ComponentOption("8 GB DDR4/DDR5 Standard", 0L),
        ComponentOption("16 GB DDR5 Dual Channel", 600_000L),
        ComponentOption("32 GB DDR5 Dual Channel", 1_400_000L),
        ComponentOption("64 GB DDR5 Dual Channel", 2_800_000L)
    )

    val storageOptions = listOf(
        ComponentOption("512 GB M.2 NVMe SSD", 0L),
        ComponentOption("1 TB M.2 NVMe SSD High-Speed", 750_000L),
        ComponentOption("2 TB M.2 NVMe SSD Gen4", 1_800_000L),
        ComponentOption("1 TB NVMe SSD + 2 TB HDD Sata", 1_300_000L)
    )

    val gpuOptions = listOf(
        ComponentOption("Integrated Graphics (Intel UHD / AMD Radeon)", 0L),
        ComponentOption("NVIDIA GeForce RTX 3050 6GB", 2_800_000L),
        ComponentOption("NVIDIA GeForce RTX 4060 8GB", 5_500_000L),
        ComponentOption("NVIDIA GeForce RTX 4080 16GB", 15_000_000L)
    )

    val monitorOptions = listOf(
        ComponentOption("14 Inch FHD (1920x1080) IPS", 0L),
        ComponentOption("15.6 Inch FHD 144Hz Gaming Display", 900_000L),
        ComponentOption("24 Inch FHD 180Hz Desktop Monitor", 1_900_000L),
        ComponentOption("27 Inch 4K UHD IPS Professional Monitor", 3_800_000L)
    )
}