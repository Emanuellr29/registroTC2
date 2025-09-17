package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ///Variables
        val btnlog = findViewById<Button>(R.id.botonInicio)
        val btnreg = findViewById<Button>(R.id.botonReg)
        val txtUsuario = findViewById<EditText>(R.id.Email)
        val txtPass = findViewById<EditText>(R.id.Password)
        val liga = findViewById<TextView>(R.id.textView)

        auth = FirebaseAuth.getInstance()


        btnlog.setOnClickListener {

            if (txtUsuario.text.toString().isEmpty() || txtPass.text.toString().isEmpty()) {
                Toast.makeText(this, "introduce usuario y/o contraseña", Toast.LENGTH_LONG)
                    .show()
            } else {

                login(txtUsuario.text.toString(), txtPass.text.toString())
                txtUsuario.text.clear()
                txtPass.text.clear()
                btnlog.isEnabled = true

            }
        }

        btnreg.setOnClickListener {

            if (txtUsuario.text.toString().isEmpty() || txtPass.text.toString().isEmpty()) {
                Toast.makeText(this, "introduce usuario y/o contraseña", Toast.LENGTH_LONG)
                    .show()
            } else {
                registro(txtUsuario.text.toString(), txtPass.text.toString())


                btnreg.isEnabled = true
                btnlog.isEnabled = true
                txtUsuario.text.clear()
                txtPass.text.clear()
            }
        }

        liga.setOnClickListener {

            btnreg.isEnabled = true
            btnlog.isEnabled = false
            txtUsuario.text.clear()
            txtPass.text.clear()
        }


    }

    fun login(usuario: String, pass: String) {
        ///logica de conexion
        auth.signInWithEmailAndPassword(usuario, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                val ventana2 = Intent(this, principal::class.java)
                ventana2.putExtra("idUsuario", usuario )
                startActivity(ventana2)

                Toast.makeText(this, "Conexion Exitosa", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error al Iniciar", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registro(usuario: String, pass: String){
        ///logica de registro
        auth.createUserWithEmailAndPassword(usuario,pass).addOnCompleteListener(this) {task -> 
            if(task.isSuccessful){
                Toast.makeText(this, "Usuario Creado", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Error al crear Usuario", Toast.LENGTH_LONG).show()
            }
        }
    }
}
