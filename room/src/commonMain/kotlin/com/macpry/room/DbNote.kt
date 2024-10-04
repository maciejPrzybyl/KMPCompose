package com.macpry.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val content: String
)