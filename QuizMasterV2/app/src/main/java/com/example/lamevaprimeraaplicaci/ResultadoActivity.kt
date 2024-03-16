package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ResultadoActivity : AppCompatActivity() {

    private lateinit var personDao: PersonDao // Declarar una variable lateinit para el DAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Recibe los datos del nombre y el puntaje del Intent
        val nombre: String = intent.getStringExtra("nombre") ?: ""
        val puntaje: Int = intent.getIntExtra("puntaje", 0)
        val dificultad: String? = intent.getStringExtra("opcion_seleccionada_dificultad")

        try {
            val app = applicationContext as PersonApp
            val db = app.room

            personDao = db.personDao() // Inicializar el DAO

            // Insertar los datos en la base de datos
            val person = Person(name = nombre, score = puntaje, dificil = dificultad ?: "")
            insertPerson(person)

            // Actualizar la vista después de insertar los datos
            updateView()

        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    private fun updateView() {
        GlobalScope.launch {
            val allPersons = personDao.getAllPersons() // Obtener todos los registros de la base de datos
            val allPersonsString = buildString {
                append("Marcador:\n")
                allPersons.forEachIndexed { index, person ->
                    append("${index + 1}. Nombre: ${person.name}, Puntaje: ${person.score}\n")
                }
            }
            // Mostrar todos los registros en el TextView
            runOnUiThread {
                val textViewResult = findViewById<TextView>(R.id.textViewResult)
                textViewResult.text = allPersonsString
            }
        }
    }
    private fun insertPerson(person: Person) {
        GlobalScope.launch {
            // Insertar los datos en la base de datos
            personDao.insert(listOf(person))

            runOnUiThread {
                // Obtener el TextView del layout
                val textViewResult = findViewById<TextView>(R.id.textViewResult)
                // Actualizar el TextView con los valores de person.name y person.score
                textViewResult.text = "Nombre: ${person.name}, Puntaje: ${person.score}"

                // Llamar a updateView para mostrar todos los jugadores, incluido el nuevo jugador insertado
                updateView()
            }
        }
    }


    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ResultadoActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}











