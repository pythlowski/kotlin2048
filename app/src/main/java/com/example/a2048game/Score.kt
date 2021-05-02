package com.example.a2048game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "position") var position: Int = 0,
    @ColumnInfo(name = "nickname") val nickname: String?,
    @ColumnInfo(name = "score") val score: Int?
)