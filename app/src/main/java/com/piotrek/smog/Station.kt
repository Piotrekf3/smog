package com.piotrek.smog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station (
        @PrimaryKey val id: Int,
        val name: String
)