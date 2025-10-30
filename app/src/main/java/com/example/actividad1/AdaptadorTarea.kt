package com.example.actividad1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.actividad1.Fragment1.TareaAdapter
import com.example.actividad1.databinding.ItemBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1.databinding.ItemimgBinding

class AdaptadorTarea(private val tareas: MutableList<Fragment2.imgTarea>,
                     private val onItemClicked: (Fragment2.imgTarea) -> Unit, // para editar
                     private val onDeleteClicked: (Fragment2.imgTarea) -> Unit  // para borrar
) : RecyclerView.Adapter<AdaptadorTarea.TareaImgVH>() {

    // ViewHolder interno
    // Necesitarás importar 'ItemBinding' (Alt + Enter)
    inner class TareaImgVH(private val binding: ItemimgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tarea: Fragment2.imgTarea) {
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
    fun updateData(newTareas: List<Fragment2.imgTarea>) {
        tareas.clear()
        tareas.addAll(newTareas)
        notifyDataSetChanged()
    }
}