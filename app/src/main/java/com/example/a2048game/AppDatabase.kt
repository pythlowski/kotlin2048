package com.example.a2048game

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Score::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}