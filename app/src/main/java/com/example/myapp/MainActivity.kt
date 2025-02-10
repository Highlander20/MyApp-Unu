package com.example.myapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.databinding.ActivityMainBinding
import com.example.myapp.ui.login.LoginActivity
import com.example.myapp.ui.login.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_inventario, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Cargar los datos del usuario en el encabezado del NavigationView
        loadUserData(navView)
    }

    // Método para cargar los datos del usuario desde SharedPreferences
    private fun loadUserData(navView: NavigationView) {
        // Recupera los datos del usuario desde SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "Usuario no registrado")
        val userEmail = sharedPreferences.getString("user_email", "email@example.com")

        // Actualiza el encabezado del NavigationView
        val headerView = navView.getHeaderView(0)
        val nameTextView: TextView = headerView.findViewById(R.id.navHeaderName) // Ajusta el ID según tu diseño
        val emailTextView: TextView = headerView.findViewById(R.id.navHeaderEmail) // Asegúrate de tener este TextView en tu diseño

        // Asigna el nombre y el correo electrónico a los TextViews
        nameTextView.text = userName
        emailTextView.text = userEmail
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Acción para cerrar sesión
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Finaliza la actividad actual para evitar volver atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}