package com.mi_tiempo.myapplication.ui.stream_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.databinding.FragmentStreamBinding
import javax.inject.Inject

class StreamFragment: Fragment() {

    val TAG = "StreamFragment"

    @Inject lateinit var streamFragmentViewModel: StreamFragmentViewModel
    lateinit var binding: FragmentStreamBinding
    var usuarioActual: String? = null
    var usuarioALlamar: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
        binding = FragmentStreamBinding.inflate(inflater)

        streamFragmentViewModel.inicializarVideoLocal((context as AppCompatActivity), binding.videoLocal, binding.videoRemoto)
        return binding.root
    }

    override fun onDestroy() {
        streamFragmentViewModel.destruir()
        super.onDestroy()
    }
}