package com.example.pr_32_kurs_5_kachalinsemen_6v.Data

import androidx.room.*

@Dao
interface Weater_Helper {
    @Query("SELECT * FROM weaters")
    fun getAll(): List<Weater>

    @Query("SELECT * FROM weaters WHERE id = :id")
    fun getById(id : Long) : Weater

    @Insert
    fun insert(weaters: Weater)

    @Update
    fun update(weaters: Weater)

    @Delete
    fun delete(weaters: Weater)
}