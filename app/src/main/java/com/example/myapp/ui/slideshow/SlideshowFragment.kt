package com.example.myapp.ui.slideshow

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapp.R
import java.text.SimpleDateFormat
import java.util.*

class SlideshowFragment : Fragment() {

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        val buttonAddReportes: Button = root.findViewById(R.id.buttonAddReportes)
        buttonAddReportes.setOnClickListener {
            showAddReportDialog()
        }

        return root
    }

    private fun showAddReportDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_reporte, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        // Referencias a las vistas del diálogo
        val editDescripcionProblema = dialogView.findViewById<EditText>(R.id.editDescripcionProblema)
        val editEquipoAfectado = dialogView.findViewById<EditText>(R.id.editEquipoAfectado)
        val textFecha = dialogView.findViewById<TextView>(R.id.textFecha)
        val buttonSeleccionarFoto = dialogView.findViewById<Button>(R.id.buttonSeleccionarFoto)
        val imagenSeleccionada = dialogView.findViewById<ImageView>(R.id.imagenSeleccionada)
        val buttonGuardarReporte = dialogView.findViewById<Button>(R.id.buttonGuardarReporte)
        val buttonCloseDialog = dialogView.findViewById<ImageView>(R.id.buttonCloseDialog) // Botón de cerrar

        // Fecha actual
        textFecha.text = "Fecha: ${getCurrentDateTime()}"

        // Configurar el botón de cerrar
        buttonCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        // Seleccionar una foto de la galería
        buttonSeleccionarFoto.setOnClickListener {
            selectImageFromGallery()
        }

        // Guardar el reporte
        buttonGuardarReporte.setOnClickListener {
            val descripcion = editDescripcionProblema.text.toString()
            val equipo = editEquipoAfectado.text.toString()
            val fecha = textFecha.text.toString()

            if (descripcion.isEmpty() || equipo.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Guardar datos o enviar al backend
                Toast.makeText(context, "Reporte guardado correctamente.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun selectImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            val imageView: ImageView? = view?.findViewById(R.id.imagenSeleccionada)
            imageView?.setImageURI(selectedImageUri)
            imageView?.visibility = View.VISIBLE
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }



}