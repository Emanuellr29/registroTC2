package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlin.getValue


class fragmentexam : Fragment() {
    val args: fragmentexamArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentexam, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnsuma = view.findViewById<Button>(R.id.buttonSuma)
        //val txtnumero = view.findViewById<Button>(R.id.numero)
        val textResultado = view.findViewById<TextView>(R.id.resultado)
        val caja = view.findViewById<EditText>(R.id.numero)
        var numero = args.cinco
        val resultado: Int

        btnsuma.setOnClickListener {
            if (caja.text.isEmpty()){
                textResultado.text = numero
            }else{
                val resultado = suma(numero, caja.text.toString())
                textResultado.text = resultado
            }

        }
    }
    fun suma(numero5: String, numeroIngresado: String ) : String? {
        val numero5Entero: Int? = numero5.toIntOrNull()
        val numeroIngEntero: Int? = numeroIngresado.toIntOrNull()
        val suma: Int
        val sumaString: String
        if(numero5Entero != null && numeroIngEntero !=null){
            suma = numero5Entero + numeroIngEntero
            sumaString = suma.toString()
            return sumaString
        }else{
            return null
        }
    }

}