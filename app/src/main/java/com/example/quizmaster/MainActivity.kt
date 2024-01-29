package com.example.quizmaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextNumberOfQuestions: EditText
    private lateinit var buttonEnter: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar los elementos de la interfaz
        editTextName = findViewById(R.id.editTextName)
        editTextNumberOfQuestions = findViewById(R.id.editTextText)
        buttonEnter = findViewById(R.id.buttonEnter)

        // Configurar un listener para el botón
        buttonEnter.setOnClickListener {
            // Obtener los valores de los EditText
            val nombre = editTextName.text.toString()
            val numeroPreguntas = editTextNumberOfQuestions.text.toString()

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || numeroPreguntas.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que el campo de preguntas contenga solo números
            if (!numeroPreguntas.matches("\\d+".toRegex())) {
                Toast.makeText(this, "El número de preguntas debe ser un valor numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que el número de preguntas esté en el rango de 1 a 30
            val preguntas = numeroPreguntas.toInt()
            if (preguntas < 1 || preguntas > 30) {
                Toast.makeText(this, "El número de preguntas debe estar entre 1 y 30", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Puedes hacer lo que quieras con los valores, por ejemplo, mostrarlos en un Toast
            // o pasarlos a otra actividad
            // Mostrar la información en un Toast
            val mensaje = "Nombre: $nombre\nNúmero de Preguntas: $numeroPreguntas"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    }
