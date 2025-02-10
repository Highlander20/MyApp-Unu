package com.example.myapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapp.MainActivity
import com.example.myapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var requestQueue: RequestQueue // Cola de solicitudes Volley

    // URL de la API
    companion object {
        private const val API_URL = "http://10.0.2.2/myapp/api.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el diseño activity_login.xml
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa la cola de solicitudes Volley
        requestQueue = Volley.newRequestQueue(this)

        // Configura el botón "Iniciar sesión"
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }

        // Configura el texto como enlace para ir a RegisterActivity
        binding.tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Valida los campos de entrada
    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailEt.error = "Ingresa un correo válido"
                false
            }
            password.isEmpty() -> {
                binding.passwordEt.error = "La contraseña es obligatoria"
                false
            }
            else -> true
        }
    }

    // Realiza el inicio de sesión con la API
    private fun performLogin(email: String, password: String) {
        binding.loginProgressBar.visibility = View.VISIBLE

        val jsonParams = JSONObject()
        jsonParams.put("email", email)
        jsonParams.put("password", password)

        val jsonRequest = JsonObjectRequest(Request.Method.POST, API_URL, jsonParams,
            Response.Listener { response ->
                binding.loginProgressBar.visibility = View.GONE
                try {
                    if (response.getBoolean("success")) {
                        val user = response.getJSONObject("user")
                        val userId = user.getInt("id")
                        val userName = user.getString("name")
                        val userEmail = user.getString("email")

                        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putInt("userId", userId)
                            putString("user_name", userName)  // Cambié userName a user_name
                            putString("user_email", userEmail)  // Cambié userEmail a user_email
                            apply()
                        }

                        Snackbar.make(binding.root, "Inicio de sesión exitoso", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Snackbar.make(binding.root, response.getString("message"), Snackbar.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Snackbar.make(binding.root, "Error al procesar la respuesta", Snackbar.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                binding.loginProgressBar.visibility = View.GONE
                Snackbar.make(binding.root, "Error de red: ${error.message}", Snackbar.LENGTH_LONG).show()
            })

        jsonRequest.tag = this
        requestQueue.add(jsonRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancela todas las solicitudes pendientes para evitar fugas de memoria
        requestQueue.cancelAll(this)
    }
}