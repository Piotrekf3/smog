package com.piotrek.smog.enity

data class Index (
    val id: Int?,
    val indexLevelName: String?
) {
    companion object {
        const val BAD = "Zły"
        const val VERY_BAD = "Bardzo zły"
    }
}