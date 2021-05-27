package com.mi_tiempo.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mi_tiempo.myapplication.R
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaSocketVideollamada
import com.mi_tiempo.myapplication.databinding.ActivityMainBinding
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var streamFragment : StreamFragment
    @Inject lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        navigateToFragment(streamFragment)

        ponerEscuchadores()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //Private methods

    private fun ponerEscuchadores(){
        binding.botonUsuario1.setOnClickListener {
            mainActivityViewModel.registrarUsuario("usuario_1")
        }

        binding.botonUsuario2.setOnClickListener {
            mainActivityViewModel.registrarUsuario("usuario_2")
        }

        binding.botonFinalizarConexion.setOnClickListener {
            mainActivityViewModel.finalizarConexion()
        }
    }



    private fun navigateToFragment(fragment: Fragment) {
        /*
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

         */
    }
}