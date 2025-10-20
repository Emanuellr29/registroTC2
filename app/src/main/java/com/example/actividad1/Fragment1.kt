package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.motion.widget.KeyPosition
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.actividad1.databinding.Fragment1Binding
import com.example.actividad1.databinding.ItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import kotlin.getValue
import kotlin.math.log

data class miTarea(
    val id:String = "uno@dom.com",
    val titulo: String = "",
    val descripcion: String = "",
    val userId: String = "",
    val creadoEn: Long = 0L
)
class Fragment1 : Fragment() {

    var usr: String = ""

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!
    private lateinit var tareaAdapter: TareaAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
            if (correo?.indexOf(string = "@")!! > 0) {
                var usua = correo.split("@")
                usr = usua.get(0).toString()
                textuser.text = usr
            }

        val onItemClickAction: (miTarea) -> Unit = { tarea ->
            Long.d("Fragment1", "clic en la tearea (EDITAR): ${tarea.titulo}!")
        }
        val onItemClickAction: (miTarea) -> Unit = { tarea ->
            Long.d("Fragment1", "clic en la tearea (EDITAR): ${tarea.titulo}!")
            borrarTarea(tarea)
        }
//        btn1.setOnClickListener {
//            findNavController().navigate(Fragment1Directions.actionFragment1ToFragment2(usr))
//        }
//
//        btnsalir.setOnClickListener {
//            val intent = Intent(requireActivity(), MainActivity::class.java)
//            startActivity(intent)
//        }
            tareaAdapter = TareaAdapter(
                mutableListOf(),
                onItemClickAction,
                onDeleteClickAction
            )
    }

    class TareaAdapter(private val tareas: MutableList<miTarea>,
        inner class TareaViewHolder(private val binding: ItemBinding) :
        }
    }
override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }
override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.bind(tareas[position])
    }
override fun getItemCount(): Int {
        return tareas.size
    }
fun uodateData(newTareas: List<miTarea>) {
    tareas.clear()
    tareas.addAll(newTareas)
    notifyDataSetChanged()
}

class TareaAdapter(private val tareas: MutableList<miTarea>,
    private val onItemClicked: (miTarea) -> Unit, // para editar
    private val onDeleteClicked: (miTarea) -> Unit )   //  borrar
    :RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {
    inner class TareaViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bind(tarea: miTarea) {
        binding.TVTitulo.text = tarea.titulo
        binding.TVDescripcion.text = tarea.descripcion
        // clic para editar
        binding.root.setOnClickListener {
            onItemClicked(tarea)
        }
        // para borrar
        binding.btnAdd.setOnClickListener {
            onDeleteClicked(tarea)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }
        override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
            holder.bind(tareas[position])
        }
        override fun getItemCount(): Int {
            return tareas.size
        }
        fun updateData(newTareas: List<miTarea>) {
            tareas.clear()
            tareas.addAll(newTareas)
            notifyDataSetChanged()
        }
    }
}