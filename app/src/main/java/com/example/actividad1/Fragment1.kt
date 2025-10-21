package com.example.actividad1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.KeyPosition
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.actividad1.databinding.Fragment1Binding
import com.example.actividad1.databinding.ItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import kotlin.getValue
import kotlin.math.E
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
        //return inflater.inflate(R.layout.fragment_1, container, false)
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //creamos variables
//        val btn1 = view.findViewById<Button>(R.id.buttonPerfil)
       val textuser = view.findViewById<TextView>(R.id.textViewVacio)
//        val btnsalir = view.findViewById<TextView>(R.id.buttonSalir)

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
            android.util.Log.d("Fragment1", "Clic en la tarea (EDITAR): ${tarea.titulo}!") // <-- Corregido a Log.d
            // Aquí iría la lógica para editar
        }

        val onDeleteClickAction: (miTarea) -> Unit = { tarea -> // <-- Nombre corregido
            android.util.Log.d("Fragment1", "Solicitando borrar tarea: ${tarea.titulo}") // <-- Corregido a Log.d
            borrarTarea(tarea) // <-- Esta función la agregaremos en el Paso 4
        }

        tareaAdapter = TareaAdapter(
            mutableListOf(),
            onItemClickAction,
            onDeleteClickAction
        )
        binding.recycle.apply {
            adapter = tareaAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        binding.buttonPerfil.setOnClickListener {
            findNavController().navigate(Fragment1Directions.actionFragment1ToFragment2(usr))
        }
        binding.floatingActionButton.setOnClickListener {
            mostrarDialogoNuevaTarea()
        }

        obtenerTareasDeFirestore()
//        binding.buttonSalir.setOnClickListener {
//            val intent = android.content.Intent(requireActivity(), MainActivity::class.java)
//            startActivity(intent)
//        }
//        obtenerTareasDeFirestore()

//        btn1.setOnClickListener {
//            findNavController().navigate(Fragment1Directions.actionFragment1ToFragment2(usr))
//        }
//
//        btnsalir.setOnClickListener {
//            val intent = Intent(requireActivity(), MainActivity::class.java)
//            startActivity(intent)
//        }
        }
// --- INICIA CÓDIGO A PEGAR (PASO 4) ---

    // Función para borrar (del PDF, página 23 [cite: 812])
    private fun borrarTarea(tarea: miTarea) {
        if (tarea.id.isEmpty()) {
            android.util.Log.e("Fragment1", "ID de tarea vacío, no se puede borrar")
            return
        }
        db.collection("Tareas").document(tarea.id)
            .delete()
            .addOnSuccessListener {
                android.util.Log.d("Fragment1", "Tarea con ID ${tarea.id} eliminada.")
            }
            .addOnFailureListener { e ->
                android.util.Log.w("Fragment1", "Error al eliminar la tarea", e)
            }
    }

    private fun  mostrarDialogoNuevaTarea(){
        val context = requireContext()
        val tituloInput = EditText(context).apply { hint = "Titulo de la tarea" }
        val descripcionInput = EditText(context).apply { hint = "Descripcion (Opcional)" }
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50,50,50,50)
            addView(tituloInput)
            addView(descripcionInput)
        }
        AlertDialog.Builder(context)
            .setTitle("Añador Nueva Tarea")
            .setView(layout)
            .setPositiveButton("Guardar"){ _, _ ->
                val titulo = tituloInput.text.toString().trim()
                val descripcion = descripcionInput.text.toString().trim()
                if(titulo.isNotEmpty()){
                    agregarNuevaTarea(titulo,descripcion)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun agregarNuevaTarea(titulo: String, descripcion: String){
        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            android.util.Log.e("Fragment1", "Usuario no autenticado. No se puede guardar la tarea. ")
            return
        }
        val nuevaTarea = miTarea(
            titulo = titulo,
            descripcion = descripcion,
            userId = currentUserId,
            creadoEn = System.currentTimeMillis()
        )

        db.collection("Tareas")
            .add(nuevaTarea)
            .addOnSuccessListener { android.util.Log.d("Fragment1","Tarea Añadidda con exito: ${it.id}") }
            .addOnFailureListener { e -> android.util.Log.e("Fragment1","Error al añadir la tarea", e)
            android.util.Log.e("Fragment1","Error al añadir", e)}

    }

    private fun obtenerTareasDeFirestore() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            android.util.Log.e("Fragment1", "Usuario no autenticado. No se pueden cargar tareas.")
            Toast.makeText(requireContext(), "Error: Debes iniciar sesion para ver las tereas", Toast.LENGTH_LONG).show()
            return
        }
        android.util.Log.d("Fragement1", "DIAGNOSITO 1: Usuario autenticado. UID: $userId. Iniciado escucha de Firestore...")

        db.collection("Tareas")
            //.whereEqualTo("userId", userId) // <-- ¡IMPORTANTE! El PDF te guía a hacer esto por seguridad
            .orderBy("creadoEn", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    android.util.Log.w("Fragment1", "Error al escuchar cambios en Firestore.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val nuevasTareas = mutableListOf<miTarea>()

                    android.util.Log.d("Fragment1","DIAGNOSTICO 3: Documentos recibidos en Snapshot: ${snapshot.size()}")

                    if (snapshot.isEmpty){
                        android.util.Log.d("Fragment1","DIAGNOSTICO 3: Snapshot vacio No haytarea en la BD o el filtro es muy restrictivo.")
                    }

                    for (document in snapshot.documents) {
                        val documentId = document.id
                        val tarea = document.toObject(miTarea::class.java)
                        if (tarea != null) {
                            // Asignamos el ID del documento a nuestro objeto
                            val tareaConId = tarea.copy(id = document.id)
                            nuevasTareas.add(tareaConId)
                            android.util.Log.d("Fragment1", "DIAGNOSTICO 4: Tarea OK. ID doc: $documentId, Titulo: ${tareaConId.titulo}" )
                        }else{
                            android.util.Log.d("Fragment1", "DIAGNOSTICO 5: FALLO EL toObject() Campos del documento NO COINCIDEN con miTarea.kt. ID Documento Fallido: ${documentId}")
                            android.util.Log.d("Fragment1", "DIAGNOSTICO 5: Contenido del documento fallido: ${document.data}")

                        }
                    }
                    android.util.Log.d("Fragment1", "DIAGNOSTICO 6: Carga completa. ${nuevasTareas.size} tareas.")
                    if(::tareaAdapter.isInitialized) {
                        tareaAdapter.updateData(nuevasTareas)
                    }
                } else {
                    android.util.Log.d("Fragment1", "Snapshot es nulo.")
                }
            }
    }

    // Esta clase debe estar ANIDADA dentro de Fragment1
    class TareaAdapter(
        private val tareas: MutableList<miTarea>,
        private val onItemClicked: (miTarea) -> Unit, // para editar
        private val onDeleteClicked: (miTarea) -> Unit  // para borrar
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

        // ViewHolder interno
        // Necesitarás importar 'ItemBinding' (Alt + Enter)
        inner class TareaViewHolder(private val binding: com.example.actividad1.databinding.ItemBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

            fun bind(tarea: miTarea) {
                binding.TVTitulo.text = tarea.titulo
                binding.TVDescripcion.text = tarea.descripcion

                // Clic para editar
                binding.root.setOnClickListener {
                    onItemClicked(tarea)
                }

                // Clic para borrar
                // ¡ERROR CORREGIDO!
                // Tu XML 'item.xml' usa android:id="@+id/borrar" [cite: 572]
                // El PDF usaba 'btnAdd'[cite: 660, 727]. Lo corregí a 'borrar'.
                binding.borrar.setOnClickListener {
                    onDeleteClicked(tarea)
                }
            }
        }

        // Métodos obligatorios del Adapter
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
            val binding = com.example.actividad1.databinding.ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TareaViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
            holder.bind(tareas[position])
        }

        override fun getItemCount(): Int {
            return tareas.size
        }

        // Función para actualizar los datos
        fun updateData(newTareas: List<miTarea>) {
            tareas.clear()
            tareas.addAll(newTareas)
            notifyDataSetChanged()
        }
    }

    // Buena práctica para ViewBinding en Fragments (del PDF, página 29 [cite: 977])
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
