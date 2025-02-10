package com.example.myapp.ui.inventario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.databinding.ItemInventarioBinding

// Clase de datos para un ítem del inventario
data class InventarioItem(
    val nombre: String,
    val cantidad: Int,
    val estado: String,
    val codigo: String = "",
    val descripcion: String = "",
    val fecha: String = ""
)

// Adaptador para el RecyclerView
class InventarioAdapter(
    private val inventarioList: List<InventarioItem>,
    private val onItemClick: (InventarioItem) -> Unit // Callback para manejar clics en los ítems
) : RecyclerView.Adapter<InventarioAdapter.ViewHolder>() {

    // ViewHolder con View Binding
    inner class ViewHolder(private val binding: ItemInventarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InventarioItem) {
            binding.textNombre.text = item.nombre
            binding.textCantidad.text = "Cantidad: ${item.cantidad}"
            binding.textEstado.text = "Estado: ${item.estado}"

            // Configurar clic en el ítem para editarlo
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    // Crear vistas para los ítems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInventarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Vincular datos a las vistas
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = inventarioList[position]
        holder.bind(item)
    }

    // Número de ítems en la lista
    override fun getItemCount(): Int = inventarioList.size

}