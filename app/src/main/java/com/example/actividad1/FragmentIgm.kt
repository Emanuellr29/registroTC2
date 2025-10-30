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


class FragmentIgm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_igm, container, false)
    }
    private lateinit var recicler: RecyclerView

    private val Imagenes = listOf(
        R.drawable.imagen1,
        R.drawable.imagen2,
        R.drawable.imagen3,
        R.drawable.imagen4,
        R.drawable.imagen5,
        R.drawable.imagen6,
        R.drawable.imagen7,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recicler = view.findViewById(R.id.recycleViewImg)
        recicler.layoutManager = GridLayoutManager(requireContext(), 3)
        recicler.adapter = FragmentAdaptador(Imagenes){ imagenSeleccionada ->
            setFragmentResult("imagenSeleccionada", Bundle().apply { putInt("resId", imagenSeleccionada)})
            findNavController().popBackStack()
            Toast.makeText(requireContext(),"Alguna imagen seleccionada", Toast.LENGTH_SHORT).show()
       }
    }


}