package com.example.lamevaprimeraaplicaci

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameResultDao {
    @Insert
    suspend fun insertResult(gameResult: GameResult)

    @Query("SELECT * FROM game_results")
    fun getAllResults(): LiveData<List<GameResult>>
}
