package com.example.lamevaprimeraaplicaci

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lamevaprimeraaplicaci.databinding.FragmentSecondBinding
import androidx.navigation.fragment.navArgs

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!




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
        val countText = getString(R.string.here_is_a_random_number_between_0_and_d, count)
        // Muestra el valor del argumento Countnumber en el TextView textView3
        view.findViewById<TextView>(R.id.textView3).text = countText
        // Muestra el valor del argumento Countnumber en el TextView
        view.findViewById<TextView>(R.id.textview_random).text = countText

    }


        // Obtén el valor del argumento "Countnumber" pasado desde FirstFragment
        //val args = SecondFragmentArgs.fromBundle(requireArguments())
        //val countNumber = args.Countnumber

        // Muestra el valor en la vista
        //val textView = view.findViewById<TextView>(R.id.random_heading)
        //textView.text = "Here is a random nomber between : $countNumber"








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}