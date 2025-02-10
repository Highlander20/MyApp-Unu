package com.example.myapp.ui.tareas

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapp.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.util.ArrayList
import java.util.List

class TareasActivity : AppCompatActivity(){

    // Lista mutable para almacenar las tareas
    private val tareasList = mutableListOf(
        Tarea("Materiales a revisar", "Próxima revisión: Viernes 31"),
        Tarea("Reunión con el equipo", "Próxima revisión: Jueves 30"),
        Tarea("Preparar informe final", "Próxima revisión: Miercoles 29"),
        Tarea("Compra de probeta numero 2", "Próxima compra: Lunes 3")
    )

    // Adaptador del RecyclerView
    private lateinit var tareasAdapter: TareasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        // Referencia al RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_tareas)

        // Configuración del RecyclerView
        tareasAdapter = TareasAdapter(tareasList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tareasAdapter

        // Referencia al botón flotante
        val fabAddTarea: FloatingActionButton = findViewById(R.id.fab_add_tarea)

        // Evento de clic para agregar una nueva tarea
        fabAddTarea.setOnClickListener {
            showAddTareaDialog()

        }

        // Referencia al botón de regresar
        val btnRegresar: ImageButton = findViewById(R.id.btn_regresar)

        // Configurar el botón para cerrar la actividad
        btnRegresar.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }
    }

    private fun showAddTareaDialog() {
        // Crear un Dialog personalizado
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_tarea, null)
        dialog.setContentView(dialogView)

        val etTitulo: EditText = dialogView.findViewById(R.id.et_tarea_titulo)
        val etDescripcion: EditText = dialogView.findViewById(R.id.et_tarea_descripcion)
        val btnGuardar: Button = dialogView.findViewById(R.id.btn_guardar_tarea)

        // Configurar el botón Guardar
        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                // Agregar la nueva tarea a la lista
                tareasList.add(Tarea(titulo, descripcion))
                tareasAdapter.notifyItemInserted(tareasList.size - 1)

                // Mostrar mensaje y cerrar el diálogo
                Toast.makeText(this, "Tarea guardada: $titulo", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
