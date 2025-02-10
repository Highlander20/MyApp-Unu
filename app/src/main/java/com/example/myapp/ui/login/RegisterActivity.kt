//package com.example.myapp.ui.login
package com.example.myapp.ui.login

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.myapp.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var requestQueue: RequestQueue // Cola de solicitudes Volley

    // URL de la API
    companion object {
        private const val API_URL = "http://10.0.2.2/myapp/api.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el diseño activity_register.xml
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa la cola de solicitudes Volley
        requestQueue = Volley.newRequestQueue(this)

        // Configura el botón de registro
        binding.btnRegister.setOnClickListener {
            val fullname = binding.fullNameEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            if (validateInput(fullname, email, password)) {
                performRegistration(fullname, email, password)
            }
        }

        // Configura el texto como enlace para ir a LoginActivity
        binding.tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza esta actividad para evitar regresar al registro
        }
    }

    // Valida los campos de entrada
    private fun validateInput(fullname: String, email: String, password: String): Boolean {
        return when {
            fullname.isEmpty() -> {
                binding.fullNameEt.error = "El nombre completo es obligatorio"
                false
            }
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailEt.error = "Ingresa un correo válido"
                false
            }
            password.isEmpty() || password.length < 6 -> {
                binding.passwordEt.error = "La contraseña debe tener al menos 6 caracteres"
                false
            }
            else -> true
        }
    }

    // Realizar el registro con la API
    private fun performRegistration(fullname: String, email: String, password: String) {
        // Muestra el indicador de carga
        binding.progressBar.visibility = View.VISIBLE

        // Crea un objeto JSON con los parámetros
        val jsonParams = JSONObject()
        jsonParams.put("fullname", fullname)
        jsonParams.put("email", email)
        jsonParams.put("password", password)

        // Realiza la solicitud POST con Volley utilizando JsonObjectRequest
        val jsonRequest = JsonObjectRequest(Request.Method.POST, API_URL, jsonParams,
            Response.Listener { response ->
                try {
                    // Oculta el indicador de carga
                    binding.progressBar.visibility = View.GONE

                    // Muestra la respuesta recibida en los logs
                    println("Server Response: $response")  // Línea agregada para depuración

                    // Procesa la respuesta del servidor
                    if (response.getBoolean("success")) {
                        // Guardar el nombre y el email en SharedPreferences
                        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("user_name", fullname)  // Guarda el nombre del usuario
                        editor.putString("user_email", email)   // Guarda el email
                        editor.apply()

                        Snackbar.make(binding.root, "Registro exitoso", Snackbar.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Snackbar.make(binding.root, response.getString("message"), Snackbar.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Error al procesar la respuesta", Snackbar.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Oculta el indicador de carga y muestra un error
                binding.progressBar.visibility = View.GONE

                // Muestra el contenido del error en los logs
                println("Volley Error: ${error.message}")  // Línea agregada para depuración

                Snackbar.make(binding.root, "Error de red: ${error.message}", Snackbar.LENGTH_LONG).show()
            })

        // Agrega la solicitud a la cola de Volley
        jsonRequest.tag = this // Asignar un tag para cancelar solicitudes si es necesario
        requestQueue.add(jsonRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancela todas las solicitudes pendientes para evitar fugas de memoria
        requestQueue.cancelAll(this)
    }
}