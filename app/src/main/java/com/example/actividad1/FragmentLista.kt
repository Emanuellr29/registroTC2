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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.actividad1.Fragment2.imgTarea
import com.example.actividad1.databinding.Fragment2Binding
import com.example.actividad1.databinding.FragmentListaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.String
import kotlin.getValue


class FragmentLista : Fragment() {

    data class imgGasto(
        val id: String = "",
        val titulo: String = "",
        val descripcion: String = "",
        val cantidad: Double = 0.0,
        val categoria: String = "",
        val creadoEn: Long = 0L,
        val userId: String = "",
        val imgId: Long = 0L

    )
    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tareaAdapterGastos: FragmentAdapterGastos
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var imagenSeleccionada: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLista_to_fragmentImagenes)
        }
        //aqui se selecciona la imagen y se obtiene el id de la imagen seleccionada
        setFragmentResultListener("imagenSeleccionada"){ _, bundle ->
            val resId = bundle.getInt("resId", 0)
            imagenSeleccionada = resId.toLong()
            android.util.Log.d("FragmentLista", "ImagenSeleccionada: $imagenSeleccionada")
            mostrarDialogoNuevaTarea()
        }

        //var datousuario = args.usuario
        //val txtusuario = view.findViewById<TextView>(R.id.textViewNombreUser)
        //val btnatras = view.findViewById<Button>(R.id.buttonInicio)
        //al btnsalir = view.findViewById<Button>(R.id.buttonSalir)
        //txtusuario.text = datousuario
        //val btnfrag3 = view.findViewById<Button>(R.id.bottonFragment3)
        val numero5: String = "5"


        val onItemClickAction: (imgGasto) -> Unit = { tarea ->
            android.util.Log.d("FragmentLista", "Clic en la tarea (Editar): ${tarea.titulo}!")
        }

        val onDeleteClickAction: (imgGasto) -> Unit = {tarea ->
            android.util.Log.d("FragmentLista", "Solicitando borrar tarea: ${tarea.titulo}")
            borrarTarea(tarea)

        }

        tareaAdapterGastos = FragmentAdapterGastos(
            mutableListOf(),
            onItemClickAction,
            onDeleteClickAction
        )

        // Esto configura el RecyclerView
        binding.recycleViewImgGastos.apply {
            adapter = tareaAdapterGastos
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        obtenerTareasDeFirestore()
    }


    private fun borrarTarea(tarea: imgGasto) {
        if (tarea.id.isEmpty()) {
            android.util.Log.e("FragmentLista", "ID de tarea vacío, no se puede borrar")
            return
        }
        db.collection("GastosPersonales").document(tarea.id)
            .delete()
            .addOnSuccessListener {
                android.util.Log.d("FragmentLista", "Tarea con ID ${tarea.id} eliminada.")
            }
            .addOnFailureListener { e ->
                android.util.Log.w("FragmentLista", "Error al eliminar la tarea", e)
            }
    }

    private fun agregarNuevaTarea(titulo: String, descripcion: String, cantidad: Double, categoria: String){
        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            android.util.Log.e("FragmentLista", "Usuario no autenticado. No se puede guardar la tarea. ")
            return
        }
        val nuevaTarea = imgGasto(
            titulo = titulo,
            descripcion = descripcion,
            cantidad = cantidad,
            categoria = categoria,
            userId = currentUserId,
            creadoEn = System.currentTimeMillis(),
            imgId = (if (imagenSeleccionada != 0L) imagenSeleccionada else R.drawable.gastos.toLong())

        )

        db.collection("GastosPersonales")
            .add(nuevaTarea)
            .addOnSuccessListener { android.util.Log.d("FragmentLista","Tarea Añadidda con exito: ${it.id}") }
            .addOnFailureListener { e -> android.util.Log.e("FragmentLista","Error al añadir la tarea", e)
                android.util.Log.e("FragmentLista","Error al añadir", e)}

    }

    private fun  mostrarDialogoNuevaTarea(){
        val context = requireContext()
        val tituloInput = EditText(context).apply { hint = "Titulo de la tarea" }
        val descripcionInput = EditText(context).apply { hint = "Descripcion (Opcional)" }
        val cantidadInput = EditText(context).apply { hint = "Cantidad" }
        val categoriaInput = EditText(context).apply { hint = "Categoria" }
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50,50,50,50)
            addView(tituloInput)
            addView(descripcionInput)
            addView(cantidadInput)
            addView(categoriaInput)
        }
        AlertDialog.Builder(context)
            .setTitle("Añadir Nueva Tarea")
            .setView(layout)
            .setPositiveButton("Guardar"){ _, _ ->
                val titulo = tituloInput.text.toString().trim()
                val descripcion = descripcionInput.text.toString().trim()
                val cantidadString = cantidadInput.text.toString().trim()
                val categoria = categoriaInput.text.toString().trim()

                val cantidad = cantidadString.toDoubleOrNull() ?: 0.0

                if(titulo.isNotEmpty()){
                    agregarNuevaTarea(titulo,descripcion, cantidad, categoria)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun obtenerTareasDeFirestore() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            android.util.Log.e("FragmentLista", "Usuario no autenticado. No se pueden cargar tareas.")
            Toast.makeText(requireContext(), "Error: Debes iniciar sesion para ver las tereas", Toast.LENGTH_LONG).show()
            return
        }
        android.util.Log.d("FragmentLista", "DIAGNOSITO 1: Usuario autenticado. UID: $userId. Iniciado escucha de Firestore...")

        db.collection("GastosPersonales")
            //.whereEqualTo("userId", userId) // <-- ¡IMPORTANTE! El PDF te guía a hacer esto por seguridad
            .orderBy("creadoEn", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    android.util.Log.w("FragmentLista", "Error al escuchar cambios en Firestore.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val nuevasTareas = mutableListOf<imgGasto>()

                    android.util.Log.d("FragmentLista","DIAGNOSTICO 3: Documentos recibidos en Snapshot: ${snapshot.size()}")

                    if (snapshot.isEmpty){
                        android.util.Log.d("FragmentLista","DIAGNOSTICO 3: Snapshot vacio No haytarea en la BD o el filtro es muy restrictivo.")
                    }

                    for (document in snapshot.documents) {
                        val documentId = document.id
                        val tarea = document.toObject(imgGasto::class.java)
                        if (tarea != null) {
                            // Asignamos el ID del documento a nuestro objeto
                            val tareaConId = tarea.copy(id = document.id)
                            nuevasTareas.add(tareaConId)
                            android.util.Log.d("FragmentLista", "DIAGNOSTICO 4: Tarea OK. ID doc: $documentId, Titulo: ${tareaConId.titulo}" )
                        }else{
                            android.util.Log.d("FragmentLista", "DIAGNOSTICO 5: FALLO EL toObject() Campos del documento NO COINCIDEN con miTarea.kt. ID Documento Fallido: ${documentId}")
                            android.util.Log.d("FragmentLista", "DIAGNOSTICO 5: Contenido del documento fallido: ${document.data}")

                        }
                    }
                    android.util.Log.d("FragmentLista", "DIAGNOSTICO 6: Carga completa. ${nuevasTareas.size} tareas.")
                    if(::tareaAdapterGastos.isInitialized) {
                        tareaAdapterGastos.updateData(nuevasTareas)
                    }
                } else {
                    android.util.Log.d("FragmentLista", "Snapshot es nulo.")
                }
            }
    }
}