package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        count--

        //Display the new value in the text view
        showCountTextView.text = count.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<Button>(R.id.Random_bt).setOnClickListener {
            val showCountTextView = view.findViewById<TextView>(R.id.textview_first0)
            val currentCount = showCountTextView.text.toString().toInt()
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}