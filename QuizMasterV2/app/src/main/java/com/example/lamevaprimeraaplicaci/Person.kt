package com.example.lamevaprimeraaplicaci

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val name: String,
   val score: Int,
   val dificil: String
)