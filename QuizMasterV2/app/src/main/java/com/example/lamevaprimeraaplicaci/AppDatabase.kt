package com.example.lamevaprimeraaplicaci

import android.content.Context
import android.widget.Toast

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "game_database"
                    ).build()
                    INSTANCE = instance
                    instance
                } catch (e: Exception) {
                    // Mostrar el mensaje de error de la excepción en un Toast
                    Toast.makeText(context, "Error initializing database: ${e.message}", Toast.LENGTH_SHORT).show()
                    throw e
                }
            }
        }

    }
}
