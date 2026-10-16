package com.example.pr_32_kurs_5_kachalinsemen_6v.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weaters")
data class Weater(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val temp: Double,
    val pressure: Double,
    val windSpeed: Double,
    val cloud : Int,
    val days: String
)