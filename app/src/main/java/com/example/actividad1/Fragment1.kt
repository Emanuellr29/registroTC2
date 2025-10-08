package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.w3c.dom.Text
import kotlin.getValue

class Fragment1 : Fragment() {

    var usr : String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //creamos variables
        val btn1 = view.findViewById<Button>(R.id.buttonPerfil)
        val textuser = view.findViewById<TextView>(R.id.textViewVacio)
        val btnsalir = view.findViewById<TextView>(R.id.buttonSalir)

        //recuperar los datos enviados por el ususario
        val correo = arguments?.getString("usuario")
        if (correo == null)
            else
                if(correo?.indexOf(string = "@")!! > 0)
                {
                    var usua = correo.split("@")
                    usr = usua.get(0).toString()
                    textuser.text= usr
                }

        btn1.setOnClickListener {
            findNavController().navigate(Fragment1Directions.actionFragment1ToFragment2(usr))
        }

        btnsalir.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

    }
}