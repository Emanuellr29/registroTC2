package com.example.actividad1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1.databinding.ItemimgBinding

class FragmentAdapterGastos(private val tareas: MutableList<FragmentLista.imgGasto>,
                            private val onItemClicked: (FragmentLista.imgGasto) -> Unit, // para editar
                            private val onDeleteClicked: (FragmentLista.imgGasto) -> Unit  // para borrar
) : RecyclerView.Adapter<FragmentAdapterGastos.TareaImgVH>() {

    // ViewHolder interno
    // Necesitarás importar 'ItemBinding' (Alt + Enter)
    inner class TareaImgVH(private val binding: ItemimgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tarea: FragmentLista.imgGasto) {
            binding.tvTitulo.text = tarea.titulo
            binding.tvDescripcion.text = tarea.descripcion

            val res = if(tarea.imgId != 0L) tarea.imgId.toInt() else R.drawable.imagen1
            binding.imgTarea1.setImageResource(res)

            // Clic para editar
            binding.root.setOnClickListener { onItemClicked(tarea) }
            binding.btnborrar.setOnClickListener { onDeleteClicked(tarea) }

        }
    }

    // Métodos obligatorios del Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaImgVH {
        val binding = ItemimgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaImgVH(binding)
    }


    override fun onBindViewHolder(holder: TareaImgVH, position: Int) {
        holder.bind(tareas[position])
    }

    override fun getItemCount(): Int = tareas.size


    // Función para actualizar los datos
    fun updateData(newTareas: List<FragmentLista.imgGasto>) {
        tareas.clear()
        tareas.addAll(newTareas)
        notifyDataSetChanged()
    }
}