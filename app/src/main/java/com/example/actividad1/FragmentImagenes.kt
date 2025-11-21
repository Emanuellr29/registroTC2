package com.example.actividad1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentImagenes : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_imagenes, container, false)
    }

    private lateinit var recicler: RecyclerView

    private val Imagenes = listOf(
        R.drawable.vivienda,
        R.drawable.transporte,
        R.drawable.dieta,
        R.drawable.salud,
        R.drawable.entretenimiento,
        R.drawable.cuidadopersonal,
        R.drawable.gastos,
        R.drawable.educacion,
        R.drawable.mascotas)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recicler = view.findViewById(R.id.RGaleria2)
        recicler.layoutManager = GridLayoutManager(requireContext(), 3)
        recicler.adapter = FragmentAdaptador(Imagenes){ imagenSeleccionada ->
            setFragmentResult("imagenSeleccionada", Bundle().apply { putInt("resId", imagenSeleccionada)})
            findNavController().popBackStack()
            Toast.makeText(requireContext(),"Alguna imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }
}