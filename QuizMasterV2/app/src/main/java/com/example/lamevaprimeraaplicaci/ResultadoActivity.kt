package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ResultadoActivity : AppCompatActivity() {


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

            // Insertar los datos en la base de datos
            val personDao = db.personDao()
            val person = Person(name = nombre, score = puntaje, dificil = dificultad ?: "")
            insertPerson(personDao, person)

            GlobalScope.launch {
                val personDao =
                    app.room.personDao() // Obtener una instancia del DAO desde la base de datos
                val allPersons =
                    personDao.getAllPersons() // Obtener todas las personas de la base de datos

                // Mostrar los resultados en un Toast
                val allPersonsString =
                    allPersons.joinToString(separator = "\n") { "Nombre: ${it.name}, Puntaje: ${it.score}" }
                showToast("Personas obtenidas de la base de datos:\n$allPersonsString")
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }


        // Realizar una consulta SELECT para obtener todas las personas

    }

    private fun insertPerson(personDao: PersonDao, person: Person) {
        GlobalScope.launch {
            personDao.insert(listOf(person))
            runOnUiThread {
                // Obtener el TextView del layout
                val textViewResult = findViewById<TextView>(R.id.textViewResult)
                // Actualizar el TextView con los valores de person.name y person.score
                textViewResult.text = "Nombre: ${person.name}, Puntaje: ${person.score}"
            }
        }
    }



    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ResultadoActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}












