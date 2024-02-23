package com.example.lamevaprimeraaplicaci

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val playerName: String,
    val difficultyLevel: String,
    val score: Int

)