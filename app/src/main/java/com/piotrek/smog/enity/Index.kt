package com.piotrek.smog.enity

data class Index (
    val id: Int?,
    val indexLevelName: String?
) {
    companion object {
        const val VERY_GOOD = "Bardzo dobry"
        const val GOOD = "Dobry"
        const val MODERATE = "Umiarkowany"
        const val SUFFICIENT = "Dostateczny"
        const val BAD = "Zły"
        const val VERY_BAD = "Bardzo zły"
    }
}