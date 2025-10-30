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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.actividad1.databinding.Fragment2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.actividad1.R

class Fragment2 : Fragment() {
    val args: Fragment2Args by navArgs()

    data class imgTarea(
        val id: String = "",
        val titulo: String = "",
        val descripcion: String = "",
        val userId: String = "",
        val creadoEn: Long = 0L,
        val imgId: Long = 0L

    )

    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!
    private lateinit var tareaAdapter: AdaptadorTarea
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var imagenSeleccionada: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_2, container, false)
        _binding = Fragment2Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment2_to_fragmentIgm)
        }
        //aqui se selecciona la imagen y se obtiene el id de la imagen seleccionada
        setFragmentResultListener("imagen seleccionada"){ _, bundle ->
            val resId = bundle.getInt("resId", 0)
            imagenSeleccionada = resId.toLong()
            android.util.Log.d("Fragment2", "ImagenSeleccionada: $imagenSeleccionada")
            mostrarDiaologoNuevaTarea()
        }

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

        val onItemClickAction: (imgTarea) -> Unit = { tarea ->
            android.util.Log.d("Fragment3", "Clic en la tarea (Editar): ${tarea.titulo}!")
        }

        val onDeleteClickAction: (imgTarea) -> Unit = {tarea ->
            android.util.Log.d("Fragment3", "Solicitando borrar tarea: ${tarea.titulo}")
            borrarTarea(tarea)

        }

        tareaAdapter = AdaptadorTarea(
            mutableListOf(),
            onItemClickAction,
            onDeleteClickAction
        )

        binding
    }

    private fun  mostrarDiaologoNuevaTarea(){
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

    private fun borrarTarea(tarea: imgTarea) {
        if (tarea.id.isEmpty()) {
            android.util.Log.e("Fragment2", "ID de tarea vacío, no se puede borrar")
            return
        }
        db.collection("Tareas").document(tarea.id)
            .delete()
            .addOnSuccessListener {
                android.util.Log.d("Fragment2", "Tarea con ID ${tarea.id} eliminada.")
            }
            .addOnFailureListener { e ->
                android.util.Log.w("Fragment2", "Error al eliminar la tarea", e)
            }
    }

    private fun agregarNuevaTarea(titulo: String, descripcion: String){
        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            android.util.Log.e("Fragment2", "Usuario no autenticado. No se puede guardar la tarea. ")
            return
        }
        val nuevaTarea = imgTarea(
            titulo = titulo,
            descripcion = descripcion,
            userId = currentUserId,
            creadoEn = System.currentTimeMillis(),
            imgId = (if(imagenSeleccionada != 0L) imagenSeleccionada else R.drawable.imagen1.toLong())
        )

        db.collection("Tareas")
            .add(nuevaTarea)
            .addOnSuccessListener { android.util.Log.d("Fragment2","Tarea Añadidda con exito: ${it.id}") }
            .addOnFailureListener { e -> android.util.Log.e("Fragment2","Error al añadir la tarea", e)
                android.util.Log.e("Fragment2","Error al añadir", e)}

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

    private fun obtenerTareasDeFirestore() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            android.util.Log.e("Fragment2", "Usuario no autenticado. No se pueden cargar tareas.")
            Toast.makeText(requireContext(), "Error: Debes iniciar sesion para ver las tereas", Toast.LENGTH_LONG).show()
            return
        }
        android.util.Log.d("Fragement2", "DIAGNOSITO 1: Usuario autenticado. UID: $userId. Iniciado escucha de Firestore...")

        db.collection("Tareas")
            //.whereEqualTo("userId", userId) // <-- ¡IMPORTANTE! El PDF te guía a hacer esto por seguridad
            .orderBy("creadoEn", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    android.util.Log.w("Fragment1", "Error al escuchar cambios en Firestore.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val nuevasTareas = mutableListOf<imgTarea>()

                    android.util.Log.d("Fragment2","DIAGNOSTICO 3: Documentos recibidos en Snapshot: ${snapshot.size()}")

                    if (snapshot.isEmpty){
                        android.util.Log.d("Fragment2","DIAGNOSTICO 3: Snapshot vacio No haytarea en la BD o el filtro es muy restrictivo.")
                    }

                    for (document in snapshot.documents) {
                        val documentId = document.id
                        val tarea = document.toObject(imgTarea::class.java)
                        if (tarea != null) {
                            // Asignamos el ID del documento a nuestro objeto
                            val tareaConId = tarea.copy(id = document.id)
                            nuevasTareas.add(tareaConId)
                            android.util.Log.d("Fragment2", "DIAGNOSTICO 4: Tarea OK. ID doc: $documentId, Titulo: ${tareaConId.titulo}" )
                        }else{
                            android.util.Log.d("Fragment2", "DIAGNOSTICO 5: FALLO EL toObject() Campos del documento NO COINCIDEN con miTarea.kt. ID Documento Fallido: ${documentId}")
                            android.util.Log.d("Fragment2", "DIAGNOSTICO 5: Contenido del documento fallido: ${document.data}")

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


}