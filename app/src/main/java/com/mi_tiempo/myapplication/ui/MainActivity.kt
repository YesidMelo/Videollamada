package com.mi_tiempo.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mi_tiempo.myapplication.R
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaSocketVideollamada
import com.mi_tiempo.myapplication.databinding.ActivityMainBinding
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import com.mi_tiempo.myapplication.utils.Constants
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var streamFragment : StreamFragment
    @Inject lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    private var usuarioActual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ponerEscuchadores()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //Private methods

    private fun ponerEscuchadores(){
        binding.botonUsuario1.setOnClickListener {
            mainActivityViewModel.registrarUsuario(Constants.usuario1)
            streamFragment.usuarioActual = Constants.usuario1
            streamFragment.usuarioALlamar = Constants.usuario2
            streamFragment.sala = "${Constants.usuario1}_${Constants.usuario2}"
            binding.botonUsuario1.visibility = View.GONE
            binding.botonUsuario2.visibility = View.GONE
            navigateToFragment(streamFragment)
        }

        binding.botonUsuario2.setOnClickListener {
            streamFragment.usuarioActual = Constants.usuario2
            streamFragment.usuarioALlamar = Constants.usuario1
            streamFragment.sala = "${Constants.usuario1}_${Constants.usuario2}"
            mainActivityViewModel.registrarUsuario(Constants.usuario2)
            binding.botonUsuario2.visibility = View.GONE
            binding.botonUsuario1.visibility = View.GONE
            navigateToFragment(streamFragment)
        }

        binding.botonFinalizarConexion.setOnClickListener {
            streamFragment.usuarioActual = null
            streamFragment.usuarioALlamar = null
            streamFragment.sala = null
            binding.botonUsuario1.visibility = View.VISIBLE
            binding.botonUsuario2.visibility = View.VISIBLE
            mainActivityViewModel.finalizarConexion()
        }
    }



    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}