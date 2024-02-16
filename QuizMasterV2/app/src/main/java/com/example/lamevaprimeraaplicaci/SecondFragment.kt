package com.example.lamevaprimeraaplicaci

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.lamevaprimeraaplicaci.databinding.FragmentSecondBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val questionIndex = AtomicInteger(0)
    private var puntaje = 0
    private var indicePregunta = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    val args: SecondFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val count = args.Countnumber
        val nombreValor = args.nombre
        val opcionSelecionadaDificultad = args.opcionSeleccionada

        view.findViewById<TextView>(R.id.puntaje).text = getString(R.string.here_is_a_random_number_between_0_and_d, count)
        view.findViewById<TextView>(R.id.puntaje).text = getString(R.string.here_is_the_name_s, nombreValor)

        Log.d("SecondFragment", "La opción seleccionada es: $opcionSelecionadaDificultad")
        view.findViewById<TextView>(R.id.opcionSelecionadaText).text = opcionSelecionadaDificultad

        val puntajeTextView = view.findViewById<TextView>(R.id.puntajeTextView)
        puntajeTextView.text = "Punts: $puntaje"

        val nextButton = view.findViewById<Button>(R.id.siguienteButton)
        nextButton.setOnClickListener {
            lifecycleScope.launch {
                verificarRespuestaYMoverSiguiente()
            }
        }
        // Mostramos la primera pregunta
        showQuestions()
    }


    private fun showQuestions() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val questions = QuestionRepository.getAllQuestions()
                val questionsToShow = questions.take(1) // Mostramos solo una pregunta

                if (questionsToShow.isNotEmpty()) {
                    MostrarPregunta(questionsToShow.first())
                }
            } catch (e: Exception) {
                // Mostrar el error en un Toast
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun MostrarPregunta(question: Question) {
        val questionTextView = view?.findViewById<TextView>(R.id.PreguntaText)
        questionTextView?.text = question.questionText

        val option1TextView = view?.findViewById<RadioButton>(R.id.opcion1RadioButton)
        option1TextView?.text = "A) ${question.options[0]}"

        val option2TextView = view?.findViewById<RadioButton>(R.id.opcion2RadioButton)
        option2TextView?.text = "B) ${question.options[1]}"

        val option3TextView = view?.findViewById<RadioButton>(R.id.opcion3RadioButton)
        option3TextView?.text = "C) ${question.options[2]}"
    }

    private fun showNextQuestion() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val opcionSeleccionada = obtenerOpcionSeleccionada()
                if (opcionSeleccionada != null) {
                    val questions = QuestionRepository.getAllQuestions()
                    if (questions.isNotEmpty()) { // Verifica si hay preguntas disponibles
                        val newIndex = questionIndex.incrementAndGet()
                        if (newIndex < questions.size) {
                            val preguntaActual = questions[newIndex]
                            MostrarPregunta(preguntaActual)
                            indicePregunta = newIndex
                        } else {
                            mostrarDialogoFinJuego()
                        }
                    } else {
                        // Mostrar un mensaje indicando que no hay más preguntas disponibles
                        Toast.makeText(requireContext(), "No hay más preguntas disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Mostrar un mensaje indicando que se debe seleccionar una respuesta antes de pasar a la siguiente pregunta
                    Toast.makeText(requireContext(), "Debe seleccionar una respuesta", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Mostrar el error en un Toast
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private suspend fun verificarRespuestaYMoverSiguiente() {
        val opcionSeleccionada = obtenerOpcionSeleccionada()
        val questions = QuestionRepository.getAllQuestions()
        val questionsString = questions.joinToString("\n") { it.questionText }

        // Verificar si el índicePregunta está dentro de los límites de la lista de preguntas
        if (indicePregunta < questions.size) {
            val preguntaActual = questions[indicePregunta]
            val opcionSeleccionadaDificultad = args.opcionSeleccionada
            val puntosARestar = when (opcionSeleccionadaDificultad) {
                "Facil" -> facil()
                "Normal" -> Normal()
                "Dificil" -> Dificil()
                else -> 0
            }

            val preguntasFalladas = mutableListOf<Question>()
            if (opcionSeleccionada == preguntaActual.correctAnswer) {
                puntaje += 10
                Toast.makeText(requireContext(), "¡Respuesta correcta! Has ganado 10 puntos.", Toast.LENGTH_SHORT).show()
            } else {
                puntaje -= puntosARestar
                preguntasFalladas.add(preguntaActual)
                Toast.makeText(requireContext(), "Respuesta incorrecta. Se han restado $puntosARestar puntos.", Toast.LENGTH_SHORT).show()
            }

            val puntajeTextView = view?.findViewById<TextView>(R.id.puntajeTextView)
            puntajeTextView?.text = "Punts: $puntaje"

            showNextQuestion()
        } else {
            Toast.makeText(requireContext(), "Contenido de las preguntas:\n$questionsString", Toast.LENGTH_LONG).show()
            // Manejo de la situación cuando el índice es mayor o igual al tamaño de la lista de preguntas
            Toast.makeText(requireContext(), "Se ha alcanzado el final de las preguntas.", Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun obtenerOpcionSeleccionada(): String? {
        val grupoRadio = view?.findViewById<RadioGroup>(R.id.opcionesRadioGroup)
        val idRadioButtonSeleccionado = grupoRadio?.checkedRadioButtonId
        val questions = QuestionRepository.getAllQuestions()

        return when (idRadioButtonSeleccionado) {
            R.id.opcion1RadioButton -> obtenerOpcion(questions, 0)
            R.id.opcion2RadioButton -> obtenerOpcion(questions, 1)
            R.id.opcion3RadioButton -> obtenerOpcion(questions, 2)
            else -> null
        }
    }

    private suspend fun obtenerOpcion(questions: List<Question>, index: Int): String? {
        return if (index < 0 || index >= questions.size) {
            null
        } else {
            val opciones = questions[indicePregunta].options // Utiliza el índice de la pregunta actual
            if (index < opciones.size) opciones[index] else null
        }
    }

    private fun facil(): Int {
        return 2
    }

    private fun Normal(): Int {
        return 3
    }

    private fun Dificil(): Int {
        return 5
    }

    private fun mostrarDialogoFinJuego() {
        val nombreValor = requireArguments().getString("nombre")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Fin del juego")

            .setPositiveButton("OK") { _, _ ->
                // Puedes agregar lógica adicional después de hacer clic en OK, si es necesario
            }
            .setCancelable(false)
            .create()
            .show()
        puntaje = 0

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
