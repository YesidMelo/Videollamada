package com.mi_tiempo.myapplication.ui.stream_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mi_tiempo.myapplication.databinding.FragmentStreamBinding

class StreamFragment: Fragment() {

    lateinit var binding: FragmentStreamBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamBinding.inflate(inflater)
        return binding.root
    }
}