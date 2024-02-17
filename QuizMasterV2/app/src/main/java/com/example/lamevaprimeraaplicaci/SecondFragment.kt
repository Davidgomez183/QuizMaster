package com.example.lamevaprimeraaplicaci

import android.app.AlertDialog
import android.media.MediaPlayer
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
    private var questions: List<Question> = emptyList()
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
        //view.findViewById<TextView>(R.id.opcionSelecionadaText).text = opcionSelecionadaDificultad

        val puntajeTextView = view.findViewById<TextView>(R.id.puntaje)
        puntajeTextView.text = "Points: $puntaje"

        val nextButton = view.findViewById<Button>(R.id.siguienteButton)

        nextButton.setOnClickListener {
            lifecycleScope.launch {
                verificarRespuestaYMoverSiguiente()
            }
        }
        // Inicializar las preguntas aquí
        loadQuestions()

    }
    private fun loadQuestions() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val loadedQuestions = QuestionRepository.getAllQuestions()
                // Aquí procesas las preguntas para asegurarte de que la respuesta correcta esté en la tercera opción
                questions = loadedQuestions.map { question ->
                    val opcionesConRespuestaCorrecta = question.options.toMutableList()
                    if (!opcionesConRespuestaCorrecta.contains(question.correctAnswer)) {
                        // Agregar la respuesta correcta si aún no está presente
                        opcionesConRespuestaCorrecta.add(question.correctAnswer)
                    }

                    // Asegurarte de que la respuesta correcta esté en la tercera posición
                    val correctAnswerIndex = opcionesConRespuestaCorrecta.indexOf(question.correctAnswer)
                    if (correctAnswerIndex != 2) {
                        // Intercambiar la respuesta correcta con la tercera opción
                        opcionesConRespuestaCorrecta[2] = question.correctAnswer
                        opcionesConRespuestaCorrecta[correctAnswerIndex] = question.options[2]
                    }

                    question.copy(options = opcionesConRespuestaCorrecta)
                }
                // Ahora puedes mostrar la primera pregunta o hacer cualquier otro procesamiento necesario
                showQuestions()
            } catch (e: Exception) {
                // Manejar errores aquí
                Toast.makeText(requireContext(), "Error al cargar las preguntas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun showQuestions() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val questionsToShow = questions.take(1) // Mostramos solo una pregunta

                if (questionsToShow.isNotEmpty()) {
                    MostrarPregunta(questionsToShow.first())
                }
            } catch (e: Exception) {

                val questionsSize = questions.size
                Toast.makeText(requireContext(), "Tamaño de la lista de preguntas: $questionsSize", Toast.LENGTH_SHORT).show()
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

    private var mediaPlayer: MediaPlayer? = null

    private fun reproducirSonidoAplausos() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.correcto)
        mediaPlayer?.start()
    }

    private fun reproducirSonidoIncorrecto() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.incorrecto)
        mediaPlayer?.start()
    }

    private fun releaseMediaPlayerIncorrecto() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private suspend fun verificarRespuestaYMoverSiguiente() {
        val opcionSeleccionada = obtenerOpcionSeleccionada()

        val questionsString = questions.joinToString("\n") { it.questionText }

        Toast.makeText(requireContext(), "ffff "+questions.size, Toast.LENGTH_SHORT).show()
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
                reproducirSonidoAplausos()
                Toast.makeText(requireContext(), "¡Respuesta correcta! Has ganado 10 puntos.", Toast.LENGTH_SHORT).show()
            } else {
                puntaje -= puntosARestar
                reproducirSonidoIncorrecto()
                preguntasFalladas.add(preguntaActual)
                Toast.makeText(requireContext(), "Respuesta incorrecta. Se han restado $puntosARestar puntos.", Toast.LENGTH_SHORT).show()
            }

            val puntajeTextView = view?.findViewById<TextView>(R.id.puntaje)
            puntajeTextView?.text = "Punts: $puntaje"

            showNextQuestion()
        } else {

            val questionsSize = questions.size
            Toast.makeText(requireContext(), "Tamaño de la lista de preguntas: $questionsSize", Toast.LENGTH_SHORT).show() // Mostrar

        }
    }


    private suspend fun obtenerOpcionSeleccionada(): String? {
        val grupoRadio = view?.findViewById<RadioGroup>(R.id.opcionesRadioGroup)
        val idRadioButtonSeleccionado = grupoRadio?.checkedRadioButtonId


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
            .setMessage("Nombre: $nombreValor\nPunts: $puntaje")
            .setPositiveButton("OK") { _, _ ->
                // Puedes agregar lógica adicional después de hacer clic en OK, si es necesario
            }
            .setCancelable(false)  // Evita que el usuario cierre el diálogo con clic fuera del cuadro de diálogo
            .create()
            .show()
        puntaje = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer() //limpiar memoria
        releaseMediaPlayerIncorrecto() //limpiar memoria
        _binding = null
    }
}
