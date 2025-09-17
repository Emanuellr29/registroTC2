package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment

class principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        var usuario = intent.getStringExtra("usuario")
        //var text = findViewById<TextView>(R.id.textView2)
        //codigo para mandar el parametro
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.contenedorFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val parametro = bundleOf("usuario" to usuario.toString())
        navController.navigate(R.id.fragment1, parametro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        //text.text = ("Bienvenido " + usuario)
    }




}