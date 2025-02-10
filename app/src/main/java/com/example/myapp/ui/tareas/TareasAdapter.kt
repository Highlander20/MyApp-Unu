package com.example.myapp.ui.tareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R

// Modelo de datos para una tarea
data class Tarea(val titulo: String, val descripcion: String)

class TareasAdapter(private val tareasList: List<Tarea>) : RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {

    // ViewHolder que contiene las vistas de cada ítem
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tv_titulo_tarea)
        val descripcionTextView: TextView = itemView.findViewById(R.id.tv_descripcion_tarea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        // Inflar el diseño del ítem
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        // Obtener la tarea actual
        val tarea = tareasList[position]

        // Asignar los datos de la tarea a las vistas
        holder.tituloTextView.text = tarea.titulo ?: "Sin título" // Manejo de nulos
        holder.descripcionTextView.text = tarea.descripcion ?: "Sin descripción" // Manejo de nulos
    }

    override fun getItemCount(): Int {
        // Número de elementos en la lista
        return tareasList.size
    }

}