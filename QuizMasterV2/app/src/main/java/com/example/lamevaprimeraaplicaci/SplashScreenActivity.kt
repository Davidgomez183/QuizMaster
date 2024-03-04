package com.example.lamevaprimeraaplicaci

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : Activity() {
    private val SPLASH_TIME_OUT: Long = 2000 // Tiempo en milisegundos para mostrar el splash screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        // Inicia un temporizador con un Handler
        Handler().postDelayed({
            // Este método se ejecutará una vez transcurrido el tiempo especificado
            // Inicia la MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Cierra esta actividad
            finish()
        }, SPLASH_TIME_OUT)
    }
}