package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
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

        /*// Obtener una referencia al botón
        val buttonPlayAgain = findViewById<Button>(R.id.buttonPlayAgain)*/

        // Configurar el OnClickListener
       /* buttonPlayAgain.setOnClickListener {
            // Aquí dentro puedes navegar a otro fragmento
            val nextFragment = FirstFragment() // Reemplaza TuNuevoFragmento con el nombre de tu fragmento
            supportFragmentManager.beginTransaction()
                .replace(R.id.FirstFragment, nextFragment)
                .commit()
        }*/
        // Configurar el OnClickListener para el botón de "Salir"
        /*val buttonExit = findViewById<Button>(R.id.buttonExit)
        buttonExit.setOnClickListener {
            // Cerrar la actividad actual y todas las actividades asociadas a ella
            finishAffinity()
        }*/
        // Obtener referencias a los TextView
        val textViewHeader1 = findViewById<TextView>(R.id.textViewHeader1)
        val textViewHeader2 = findViewById<TextView>(R.id.textViewHeader2)

// Establecer el texto de los TextView
        textViewHeader1.text = "Nombre"
        textViewHeader2.text = "Puntaje"

    }

    private fun updateView() {
        GlobalScope.launch {
            val allPersons = personDao.getAllPersons() // Obtener todos los registros de la base de datos
            val sortedPersons = allPersons.sortedByDescending { it.score } // Ordenar la lista de personas por puntaje de mayor a menor

            runOnUiThread {
                val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
                tableLayout.removeAllViews() // Limpiar todas las filas existentes antes de agregar nuevas filas

                // Agregar cabecera de la tabla
                val headerRow = TableRow(this@ResultadoActivity)
                val headerNames = listOf("Nombre", "Puntaje", "Dificultad")
                headerNames.forEach { headerName ->
                    val textView = TextView(this@ResultadoActivity).apply {
                        text = headerName
                        layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                            weight = 10f
                            marginStart = 16 // Ajusta el margen izquierdo según sea necesario
                            marginEnd = 16 // Ajusta el margen derecho según sea necesario
                            width = 0 // Establece el ancho en 0 para que el weight funcione correctamente
                        }
                    }
                    headerRow.addView(textView)
                }
                tableLayout.addView(headerRow)

                // Agregar datos de los jugadores
                sortedPersons.forEach { person ->
                    val tableRow = TableRow(this@ResultadoActivity)

                    val nameTextView = TextView(this@ResultadoActivity).apply {
                        text = person.name
                        layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                            weight = 10f
                            marginStart = 50 // Ajusta el margen izquierdo según sea necesario
                            marginEnd = 16 // Ajusta el margen derecho según sea necesario
                            width = 50 // Establece el ancho en 0 para que el weight funcione correctamente
                        }
                    }
                    val scoreTextView = TextView(this@ResultadoActivity).apply {
                        text = person.score.toString()
                        layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                            weight = 1f
                        }
                    }
                    val difficultyTextView = TextView(this@ResultadoActivity).apply {
                        text = person.dificil
                        layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                            weight = 1f
                        }
                    }

                    tableRow.addView(nameTextView)
                    tableRow.addView(scoreTextView)
                    tableRow.addView(difficultyTextView)

                    tableLayout.addView(tableRow)
                }
            }
        }
    }


    private fun insertPerson(person: Person) {
        GlobalScope.launch {
            // Insertar los datos en la base de datos
            personDao.insert(listOf(person))

            runOnUiThread {
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











