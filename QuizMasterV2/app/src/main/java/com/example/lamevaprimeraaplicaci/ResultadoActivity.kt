package com.example.lamevaprimeraaplicaci

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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


        // Obtener referencias a los TextView
        val textViewHeader1 = findViewById<TextView>(R.id.textViewHeader1)
        val textViewHeader2 = findViewById<TextView>(R.id.textViewHeader2)

        // Establecer el texto de los TextView
        textViewHeader1.text = "Nombre"
        textViewHeader2.text = "Puntaje"

    }

    private fun updateView() {
        GlobalScope.launch {
            val sortedPersons = withContext(Dispatchers.IO) {
                personDao.getAllPersons().sortedByDescending { it.score }
            }

            runOnUiThread {
                val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
                tableLayout.removeAllViews()

                addTableHeader(tableLayout)
                addPlayersData(tableLayout, sortedPersons)
            }
        }
    }

    private fun addTableHeader(tableLayout: TableLayout) {
        val headerRow = TableRow(this@ResultadoActivity)
        val headerNames = listOf("Nombre", "Puntaje", "Dificultad")
        headerNames.forEach { headerName ->
            val textView = createTextView(headerName, true)
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)
    }

    private fun addPlayersData(tableLayout: TableLayout, sortedPersons: List<Person>) {
        var alternateColor = true
        sortedPersons.forEach { person ->
            val tableRow = TableRow(this@ResultadoActivity)
            if (alternateColor) {
                tableRow.setBackgroundColor(Color.parseColor("#ECEFF1")) // Cambia el color de fondo de la fila
            }
            val nameTextView = createTextView(person.name, false, alternateColor)
            val scoreTextView = createTextView(person.score.toString(), false, alternateColor)
            val difficultyTextView = createTextView(person.dificil, false, alternateColor)

            tableRow.addView(nameTextView)
            tableRow.addView(scoreTextView)
            tableRow.addView(difficultyTextView)

            tableLayout.addView(tableRow)
            alternateColor = !alternateColor
        }
    }

    private fun createTextView(text: String, isHeader: Boolean = false, alternateColor: Boolean = false): TextView {
        return TextView(this@ResultadoActivity).apply {
            this.text = text
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                marginStart = 16
                marginEnd = 16
                width = 0
            }
            gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
            }
            if (alternateColor) {
                setBackgroundColor(Color.parseColor("#F5F5F5")) // Cambia el color de fondo de la celda
            }
            setPadding(10, 20, 10, 20) // Ajusta el relleno
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











