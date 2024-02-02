package com.example.quizmaster

import SecondFragment
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  // Asegúrate de que el nombre de tu clase de enlace sea correcto
    private lateinit var buttonEnter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Declarar preguntas fuera del bloque if
        // Configurar un listener para el botón
        buttonEnter = binding.buttonEnter

        buttonEnter.setOnClickListener {
            // Obtener los valores de los EditText
            val nombre = binding.editTextName.text.toString()
            val numeroPreguntas = binding.editTextText.text.toString()

            if (nombre.isEmpty() || numeroPreguntas.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!numeroPreguntas.matches("\\d+".toRegex())) {
                Toast.makeText(this, "El número de preguntas debe ser un valor numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val preguntas = numeroPreguntas.toInt()
            if (preguntas < 1 || preguntas > 30) {
                Toast.makeText(this, "El número de preguntas debe estar entre 1 y 30", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Resto del código para mostrar la información en un Toast y pasar datos al segundo fragmento
            val mensaje = "Nombre: $nombre\nNúmero de Preguntas: $numeroPreguntas"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

            // Resto del código para pasar información al segundo fragmento
            val segundoFragmento = SecondFragment()
            val bundle = Bundle()
            bundle.putString("nombre", nombre)
            bundle.putString("numeroPreguntas", numeroPreguntas)
            segundoFragmento.arguments = bundle

            // Ocultar el primer fragmento
            val primerFragmento = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (primerFragmento != null) {
                supportFragmentManager.beginTransaction().hide(primerFragmento).commit()
            }

            // Realizar la transacción al segundo fragmento
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, segundoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}

