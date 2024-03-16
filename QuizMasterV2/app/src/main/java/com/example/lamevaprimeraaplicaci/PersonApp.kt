package com.example.lamevaprimeraaplicaci

import android.app.Application
import androidx.room.Room

class PersonApp : Application() {

    lateinit var room: PeopleDb // Cambiado a lateinit para inicialización diferida

    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(this, PeopleDb::class.java, "person").build()
    }
}
