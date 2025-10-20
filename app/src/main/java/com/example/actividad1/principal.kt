package com.example.actividad1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class principal : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private var nombreUsuario : String? = null
    @SuppressLint("MissingInflatedId")
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

        //variable del menu
        drawerLayout = findViewById(R.id.main)
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        nombreUsuario = intent.getStringExtra("usuario")

        //el actionbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Menu"
        //para la navegacion =
        val appbar = AppBarConfiguration(setOf(R.id.fragment1, R.id.fragment2,R.id.fragmentexam),drawerLayout)
        //val appbar = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(toolbar, navController, appbar)
        NavigationUI.setupWithNavController(navView, navController)

        //navController.navigate(R.id.fragment1)

        navView.setNavigationItemSelectedListener { menuItem ->
            // Cerramos el menú lateral
            drawerLayout.closeDrawers()

            // Creamos un paquete para enviar los datos
            val bundle = bundleOf("usuario" to nombreUsuario)

            // Navegamos según la opción seleccionada
            when (menuItem.itemId) {
                R.id.fragment1 -> navController.navigate(R.id.fragment1, bundle)
                R.id.fragment2 -> navController.navigate(R.id.fragment2, bundle)
                R.id.fragmentexam -> {
                    // Para este fragment, el argumento se llama "cinco"
                    val bundleExamen = bundleOf("cinco" to "5")
                    navController.navigate(R.id.fragmentexam, bundleExamen)
                }
            }
            true
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        //text.text = ("Bienvenido " + usuario)
    }




}