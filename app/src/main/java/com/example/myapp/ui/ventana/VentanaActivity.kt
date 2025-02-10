package com.example.myapp.ui.ventana

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.R
import com.example.myapp.ui.login.RegisterActivity

class VentanaActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana)

        // Esperar 5 segundos y luego pasar a RegisterActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish() // Finalizar la actividad actual para que no vuelva al presionar "Atrás"
        }, 5000) // 5000 ms = 5 segundos
    }
}