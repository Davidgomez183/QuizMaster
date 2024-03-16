package com.example.lamevaprimeraaplicaci

import android.app.Application
import androidx.room.Room

class PersonApp : Application() {

    val  room = Room.databaseBuilder(this, PeopleDb::class.java , name = "person").build()
}