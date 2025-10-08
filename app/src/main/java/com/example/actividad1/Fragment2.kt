package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth

class Fragment2 : Fragment() {
    val args: Fragment2Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var datousuario = args.usuario
        val txtusuario = view.findViewById<TextView>(R.id.textViewNombreUser)
        val btnatras = view.findViewById<Button>(R.id.buttonInicio)
        val btnsalir = view.findViewById<Button>(R.id.buttonSalir)
        txtusuario.text = datousuario
        val btnfrag3 = view.findViewById<Button>(R.id.bottonFragment3)
        val numero5: String = "5"

        btnatras.setOnClickListener {
            findNavController().popBackStack()
        }

        btnsalir.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        btnfrag3.setOnClickListener {
            findNavController().navigate(Fragment2Directions.actionFragment2ToFragmentexam(numero5))

        }
    }
}