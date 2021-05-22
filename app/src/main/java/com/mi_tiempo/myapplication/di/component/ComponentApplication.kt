package com.mi_tiempo.myapplication.di.component

import com.mi_tiempo.myapplication.di.module.ModuleApplication
import com.mi_tiempo.myapplication.di.module.ModuleFragment
import com.mi_tiempo.myapplication.di.module.ModuleViewModel
import com.mi_tiempo.myapplication.ui.MainActivity
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragmentViewModel
import dagger.Component

@Component( modules = [
    ModuleApplication::class,
    ModuleFragment::class,
    ModuleViewModel::class
])
interface ComponentApplication {

    //viewmodels
    fun inject(streamFragmentViewModel: StreamFragmentViewModel)

    //fragments
    fun inject(streamFragment: StreamFragment)

    //activities
    fun inject(mainActivity: MainActivity)
}