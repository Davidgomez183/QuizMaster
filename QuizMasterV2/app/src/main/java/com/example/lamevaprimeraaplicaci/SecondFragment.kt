package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.lamevaprimeraaplicaci.databinding.FragmentSecondBinding
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


        val args: SecondFragmentArgs by navArgs()
        val count = args.Countnumber
        val nombreValor = args.nombre
        val score = 0
        val countText = getString(R.string.here_is_a_random_number_between_0_and_d, count)
        view.findViewById<TextView>(R.id.textView3).text = countText

        val nombreText = getString(R.string.here_is_the_name_s, nombreValor)
        view.findViewById<TextView>(R.id.textView3).text = nombreText

        showQuestions()

        val nextButton = view?.findViewById<Button>(R.id.siguienteButton)
        nextButton?.setOnClickListener {
            nextQuestion()
        }

    }

    private fun showQuestions() {
        val numberOfQuestionsToShow = 1
        val questionsToShow = QuestionRepository.allQuestions.toList().shuffled().take(numberOfQuestionsToShow)

        if (questionsToShow.isNotEmpty()) {
            showQuestion(questionsToShow.first())
        }
    }

    private fun showQuestion(question: Question) {
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}