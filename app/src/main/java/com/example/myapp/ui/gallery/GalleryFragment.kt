package com.example.myapp.ui.gallery

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.FragmentGalleryBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class GalleryFragment : Fragment() {


    private var _binding: FragmentGalleryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private val imageList = mutableListOf<Uri>()


    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar RecyclerView para la galería
        recyclerView = binding.galleryRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3) // Tres columnas
        adapter = GalleryAdapter(imageList)
        recyclerView.adapter = adapter

        // Configuración del botón para tomar fotos
        binding.btnTakePhoto.setOnClickListener {
            openCamera()
        }

        // Inicializar el launcher de la cámara
        setupCameraLauncher()

        return root
    }

    private fun openCamera() {

        val photoFile = createImageFile()
        if (photoFile != null) {
            photoUri = Uri.fromFile(photoFile)
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "No se pudo crear el archivo de imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {

        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile("IMG_$timeStamp", ".jpg", storageDir)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun setupCameraLauncher() {

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                imageList.add(photoUri!!)
                adapter.notifyItemInserted(imageList.size - 1)
                Toast.makeText(context, "Foto guardada en galería", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al tomar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}