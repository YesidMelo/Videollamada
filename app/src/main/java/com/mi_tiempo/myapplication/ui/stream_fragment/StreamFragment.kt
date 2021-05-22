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

    @Inject lateinit var streamFragmentViewModel: StreamFragmentViewModel
    lateinit var binding: FragmentStreamBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
        binding = FragmentStreamBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        streamFragmentViewModel.inicializarVideoLocal(context as AppCompatActivity, binding.videoLocal)
    }

    override fun onDestroy() {
        streamFragmentViewModel.destruir()
        super.onDestroy()
    }
}