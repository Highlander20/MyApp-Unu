package com.example.myapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapp.ui.tareas.TareasActivity
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.R
import com.example.myapp.databinding.FragmentHomeBinding
import com.example.myapp.ui.slideshow.SlideshowFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el diseño del fragmento sin configurar el TextView antiguo
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Configurar el botón de gestión de tareas
        binding.buttonGestionTareas.setOnClickListener {
            val intent = Intent(requireContext(), TareasActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}