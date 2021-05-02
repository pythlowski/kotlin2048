package com.example.a2048game

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score ORDER BY score DESC")
    fun getAll(): List<Score>

    @Query("SELECT * FROM score WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Score>

    @Query("SELECT * FROM score ORDER BY score DESC LIMIT 1")
    fun getTopScorer(): Score

    @Insert
    fun insert(vararg score: Score)

    @Delete
    fun delete(score: Score)
}

