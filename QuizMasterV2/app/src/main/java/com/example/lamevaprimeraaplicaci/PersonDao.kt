package com.example.lamevaprimeraaplicaci

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonDao {

    @Query("SELECT * FROM Person")
  suspend  fun getAllPersons(): List<Person>

    @Query("SELECT * FROM Person WHERE id = :id")
  suspend  fun getPersonById(id: Int): Person

    @Update
  suspend  fun update(person: Person)

    @Insert
  suspend fun insert(person: List<Person>)

    @Delete
 suspend  fun delete(person: Person)

}