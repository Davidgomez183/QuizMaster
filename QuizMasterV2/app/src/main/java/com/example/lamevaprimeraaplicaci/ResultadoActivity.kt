package com.example.lamevaprimeraaplicaci

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Recibe los datos del nombre y el puntaje del Intent
        val nombre = intent.getStringExtra("nombre")
        val puntaje = intent.getIntExtra("puntaje", 0)
        val dificultad = intent.getStringExtra("opcion_seleccionada_dificultad")

        try {
            val gameResultDao = AppDatabase.getDatabase(applicationContext).gameResultDao()

            // Crea un nuevo alcance de coroutine con Dispatchers.IO para realizar operaciones de E/S
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Dentro de la coroutine, realiza la operación de inserción en la base de datos
                    val gameResult = GameResult(playerName = nombre!!, difficultyLevel = dificultad!!, score = puntaje)
                    gameResultDao.insertResult(gameResult)
                } catch (e: Exception) {
                    // Maneja cualquier excepción mostrando un Toast en el hilo principal
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Error al insertar en la base de datos", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Observa los resultados en el hilo principal
            gameResultDao.getAllResults().observe(this) { gameResults ->
                // Actualiza la interfaz de usuario con los resultados obtenidos
                if (gameResults.isNotEmpty()) {
                    mostrarResultados(gameResults)
                } else {
                    // Si no hay resultados, muestra un mensaje indicando que no hay datos disponibles
                    val resultadosTextView = findViewById<TextView>(R.id.resultadosTextView)
                    resultadosTextView.text = "No hay resultados disponibles"
                }
            }
        } catch (e: Exception) {
            // Si se produce una excepción al inicializar el DAO, muestra un Toast con un mensaje de error
            Log.e("ResultadoActivity", "Error al inicializar gameResultDao: $e")
            Toast.makeText(this, "Error al inicializar gameResultDao", Toast.LENGTH_SHORT).show()
        }


        //val reiniciarButton = findViewById<Button>(R.id.reiniciarButton)
        //reiniciarButton.setOnClickListener {
         //   reiniciarJuego()
       // }

        //val exitButton = findViewById<Button>(R.id.salir)
        //exitButton.setOnClickListener {
        //    exitGame()
       // }
    }

    private fun reiniciarJuego() {
        val intent = Intent(this, FirstFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

   private fun exitGame(){
       finishAffinity()
    }

    private fun mostrarResultados(gameResults: List<GameResult>) {
        // Construye una cadena con los resultados obtenidos y actualiza el TextView
        val sb = StringBuilder()
        for (result in gameResults) {
            sb.append("Nombre: ${result.playerName}, Puntaje: ${result.score}, Modo: ${result.difficultyLevel}\n")
        }
        // Actualiza el TextView con los resultados obtenidos
        val resultadosTextView = findViewById<TextView>(R.id.resultadosTextView)
        resultadosTextView.text = sb.toString()
    }
}
