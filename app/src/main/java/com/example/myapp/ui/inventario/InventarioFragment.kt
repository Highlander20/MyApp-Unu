package com.example.myapp.ui.inventario

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.DialogAddEquipoBinding
import com.example.myapp.databinding.FragmentInventarioBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventarioFragment : Fragment() {

    private var _binding: FragmentInventarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: InventarioAdapter

    // Lista mutable para inventario
    private val inventarioList = mutableListOf(
        InventarioItem("Pipeta 1", 6, "Activo"),
        InventarioItem("Microscopio Monocular", 1, "En mantenimiento"),
        InventarioItem("Probeta 250ml", 21, "Activo"),
        InventarioItem("Balanza", 5, "Activo"),
        InventarioItem("Guantes", 20, "Activo")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventarioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupAddButton()

        return root
    }

    private fun setupRecyclerView() {
        binding.recyclerViewInventario.layoutManager = LinearLayoutManager(requireContext())
        adapter = InventarioAdapter(inventarioList) { item ->
            showAddOrUpdateDialog(item)
        }
        binding.recyclerViewInventario.adapter = adapter
    }

    private fun setupAddButton() {
        binding.buttonAddEquipo.setOnClickListener {
            showAddOrUpdateDialog(null)
        }
    }

    private fun showAddOrUpdateDialog(item: InventarioItem?) {
        val dialogBinding = DialogAddEquipoBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root).create()

        item?.let {
            dialogBinding.editNombre.setText(it.nombre)
            dialogBinding.editCodigo.setText(it.codigo)
            dialogBinding.editDescripcion.setText(it.descripcion)
            dialogBinding.editCantidad.setText(it.cantidad.toString())
            dialogBinding.editEstado.setText(it.estado)
            dialogBinding.textFecha.text = "Fecha: ${it.fecha}"
        } ?: run {
            dialogBinding.textFecha.text = "Fecha: ${getCurrentDateTime()}"
        }

        dialogBinding.buttonGuardar.setOnClickListener {
            val nombre = dialogBinding.editNombre.text.toString().trim()
            val codigo = dialogBinding.editCodigo.text.toString().trim()
            val descripcion = dialogBinding.editDescripcion.text.toString().trim()
            val cantidad = dialogBinding.editCantidad.text.toString().toIntOrNull() ?: 0
            val estado = dialogBinding.editEstado.text.toString().trim()
            val fecha = getCurrentDateTime()

            if (nombre.isEmpty() || estado.isEmpty()) {
                dialogBinding.editNombre.error = "Requerido"
                dialogBinding.editEstado.error = "Requerido"
                return@setOnClickListener
            }

            if (item == null) {
                inventarioList.add(InventarioItem(nombre, cantidad, estado, codigo, descripcion, fecha))
                adapter.notifyItemInserted(inventarioList.size - 1)
            } else {
                val index = inventarioList.indexOf(item)
                if (index != -1) {
                    inventarioList[index] = InventarioItem(nombre, cantidad, estado, codigo, descripcion, fecha)
                    adapter.notifyItemChanged(index)
                }
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}