package com.example.lamevaprimeraaplicaci

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.navArgs
import com.example.lamevaprimeraaplicaci.databinding.FragmentSecondBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val questionIndex = AtomicInteger(0)  // Inicializa el índice en 0
    private var puntaje = 0
    private var indicePregunta = 0  // Nuevo índice para el array de preguntas
    // En algún lugar de tu clase, puedes declarar tiempoTranscurrido como una variable de clase.
    private var tiempoTranscurrido  = 0
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

        //Coger valores por argumentos
        val args: SecondFragmentArgs by navArgs()
        val count = args.Countnumber
        val nombreValor = args.nombre
        val opcionSelecionada = args.opcionSeleccionada

        val countText = getString(R.string.here_is_a_random_number_between_0_and_d, count)
        view.findViewById<TextView>(R.id.puntaje).text = countText

        val nombreText = getString(R.string.here_is_the_name_s, nombreValor)
        view.findViewById<TextView>(R.id.puntaje).text = nombreText

        //Muestra el valor en la consola
        Log.d("SecondFragment", "La opción seleccionada es: $opcionSelecionada")

        // Muestra el valor en un TextView (asumiendo que tienes un TextView en tu layout con el id textViewOpcionSeleccionada)
        view.findViewById<TextView>(R.id.opcionSelecionadaText).text = opcionSelecionada

        showQuestions()


        val puntajeTextView = view.findViewById<TextView>(R.id.puntajeTextView)
        puntajeTextView.text = "Punts: $puntaje"

        val nextButton = view?.findViewById<Button>(R.id.siguienteButton)
        nextButton?.setOnClickListener {
            nextQuestion()
            verificarRespuestaYMoverSiguiente()
        }

        // Iniciar el juego
        iniciarJuego()

    }
    private fun showQuestions() {
        val numberOfQuestionsToShow = 1
        val questionsToShow = QuestionRepository.allQuestions.toList().take(numberOfQuestionsToShow)

        if (questionsToShow.isNotEmpty()) {
            MostrarPregunta(questionsToShow.first())
        }
    }

    private fun iniciarJuego() {
        siguientePregunta()
        tiempoInicioPregunta = System.currentTimeMillis() // Inicializar el tiempo de inicio de la pregunta
    }
    private fun MostrarPregunta(question: Question) {
        // Mostrar Pregunta questionTextView
        val questionTextView = view?.findViewById<TextView>(R.id.PreguntaText)
        questionTextView?.text = question.questionText

        val option1TextView = view?.findViewById<RadioButton>(R.id.opcion1RadioButton)
        option1TextView?.text = "A) ${question.options[0]}"

        val option2TextView = view?.findViewById<RadioButton>(R.id.opcion2RadioButton)
        option2TextView?.text = "B) ${question.options[1]}"

        val option3TextView = view?.findViewById<RadioButton>(R.id.opcion3RadioButton)
        option3TextView?.text = "C) ${question.options[2]}"

        // Mostrar la imagen
        val questionImage = view?.findViewById<ImageView>(R.id.questionImageView)
        questionImage?.setImageResource(question.imageResourceId)
    }


    private fun nextQuestion() {
        val newIndex = questionIndex.incrementAndGet()  // Incrementa el índice al siguiente
        if (newIndex < QuestionRepository.allQuestions.size) {
            showQuestions()  // Muestra la siguiente pregunta
        } else {
            // Aquí puedes manejar el caso cuando se hayan mostrado todas las preguntas
            // Puedes reiniciar el índice o realizar otra acción según tu lógica
            // Por ejemplo:
            questionIndex.set(0)
            showQuestions()  // Muestra la primera pregunta de nuevo
        }
    }
    private var tiempoInicioPregunta: Long = 0
   // Esta función se encarga de verificar si la opción seleccionada por el usuario
   // es la respuesta correcta a la pregunta actual. Si es correcta, incrementa el puntaje en 10 puntos.
   private fun verificarRespuestaYMoverSiguiente() {
       val opcionSeleccionada = obtenerOpcionSeleccionada()
       val preguntaActual = QuestionRepository.allQuestions[indicePregunta]

       // Inicializar el tiempo de inicio de la pregunta
       tiempoInicioPregunta = System.currentTimeMillis()

       val tiempoMaximoPorPregunta = when (opcionSeleccionada) {
           "Facil" -> 3 // Tiempo máximo para preguntas fáciles (en segundos)
           "Normal" -> 45 // Tiempo máximo para preguntas normales
           "Dificil" -> 60 // Tiempo máximo para preguntas difíciles
           else -> 30 // Por defecto, se asume un tiempo máximo de 30 segundos
       }
       val tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicioPregunta) / 1000 // Calcular el tiempo transcurrido en segundos

       if (opcionSeleccionada == preguntaActual.correctAnswer) {
           puntaje += 10
       }else{
           puntaje -=5
       }

       // Verificar si se excedió el tiempo máximo
       if (tiempoTranscurrido > tiempoMaximoPorPregunta) {
           puntaje -= 10 // Restar 10 puntos si se excede el tiempo máximo
       }

       // Verificar si se excedió el tiempo máximo
       if (tiempoTranscurrido > tiempoMaximoPorPregunta) {
           puntaje -= 10 // Restar 10 puntos si se excede el tiempo máximo
       }

       val puntajeTextView = view?.findViewById<TextView>(R.id.puntajeTextView)
       puntajeTextView?.text = "Punts: $puntaje"

       siguientePregunta()
   }
//Esta función obtiene la opción seleccionada por el usuario en el grupo de botones de radio.
// Utiliza el ID del botón de radio seleccionado
// para determinar la opción correspondiente en la lista de opciones de la pregunta actual.
private fun obtenerOpcionSeleccionada(): String? {
    val grupoRadio = view?.findViewById<RadioGroup>(R.id.opcionesRadioGroup)
    val idRadioButtonSeleccionado = grupoRadio?.checkedRadioButtonId
    val opciones = QuestionRepository.allQuestions[indicePregunta].options

    return when (idRadioButtonSeleccionado) {
        R.id.opcion1RadioButton -> opciones[0]
        R.id.opcion2RadioButton -> opciones[1]
        R.id.opcion3RadioButton -> opciones[2]
        else -> null
    }
}

    private fun siguientePregunta() {
        indicePregunta++

        if (indicePregunta < QuestionRepository.allQuestions.size) {
            MostrarPregunta(QuestionRepository.allQuestions[indicePregunta])
        } else {
            // Aquí puedes manejar el caso cuando se hayan mostrado todas las preguntas
            // Por ejemplo, mostrar un mensaje de fin de juego o reiniciar el cuestionario
            // Reiniciar el índice y puntaje para comenzar de nuevo
            mostrarDialogoFinJuego()
            indicePregunta = 0
            puntaje = 0
            MostrarPregunta(QuestionRepository.allQuestions[indicePregunta])
        }
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
        _binding = null
    }
}