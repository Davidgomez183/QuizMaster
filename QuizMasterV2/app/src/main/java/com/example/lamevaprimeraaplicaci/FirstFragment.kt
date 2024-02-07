package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lamevaprimeraaplicaci.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }
    private fun sumaValor(view: View){

        //Get the text view
        val showCountTextView=view.findViewById<TextView>(R.id.textview_first0)

        //Get the value of the text view
        val countString = showCountTextView.text.toString()

        //Convert value to a number and increment el numero
        var count = countString.toInt()
        count++

        //Display the new value in the text view
        showCountTextView.text = count.toString()
    }

    private fun restaValor(view: View){

        //Get the text view
        val showCountTextView=view.findViewById<TextView>(R.id.textview_first0)

        //Get the value of the text view
        val countString = showCountTextView.text.toString()

        //Convert value to a number and increment el numero
        var count = countString.toInt()
        count = 1;

        count--

        //Display the new value in the text view
        showCountTextView.text = count.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<Button>(R.id.Random_bt).setOnClickListener {
            val showCountTextView = view.findViewById<TextView>(R.id.textview_first0)
            val nombreValor = view.findViewById<TextView>(R.id.nombreValor)

            // Obtén el texto de los TextViews
            val countText = showCountTextView.text.toString()
            val nombreText = nombreValor.text.toString()

            // Convierte el texto del contador a un valor numérico
            val currentCount = if (countText.isNotEmpty()) countText.toInt() else 0
            //Otro argumento para pasar al second la opcion de dificil
            val opcionSeleccionada = obtenerOpcionSeleccionada()
            Log.d("TAG", "La opción seleccionada es: $opcionSeleccionada")

            // Crea la acción de navegación con los argumentos
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount, nombreText,opcionSeleccionada!!)

            findNavController().navigate(action)

        }

        // troba el toast_button pel seu ID
        view.findViewById<Button>(R.id.Toast_bt).setOnClickListener {
            restaValor(view)
        }


        view.findViewById<Button>(R.id.Count_bt).setOnClickListener {
            sumaValor(view)
        }



    }

    private fun obtenerOpcionSeleccionada(): String? {
        val grupoRadio = view?.findViewById<RadioGroup>(R.id.opcionesRadioGroupFirst)
        val idRadioButtonSeleccionado = grupoRadio?.checkedRadioButtonId


        return when (idRadioButtonSeleccionado) {
            R.id.opcion1 -> "Facil"
            R.id.opcion2 -> "Normal"
            R.id.opcion3 -> "Dificil"
            else -> null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}