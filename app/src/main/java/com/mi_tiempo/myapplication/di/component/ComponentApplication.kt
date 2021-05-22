package com.mi_tiempo.myapplication.di.component

import com.mi_tiempo.myapplication.data_access.videollamada.CrearAudioLocal
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoLocal
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoRemoto
import com.mi_tiempo.myapplication.di.module.ModuleApplication
import com.mi_tiempo.myapplication.di.module.ModuleFragment
import com.mi_tiempo.myapplication.di.module.ModuleVideollamada
import com.mi_tiempo.myapplication.di.module.ModuleViewModel
import com.mi_tiempo.myapplication.ui.MainActivity
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragmentViewModel
import com.mi_tiempo.myapplication.uses_cases.videollamada.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [
    ModuleApplication::class,
    ModuleFragment::class,
    ModuleViewModel::class,
    ModuleVideollamada::class
])
interface ComponentApplication {

    //DataAccess
    fun inject(crearAudioLocal: CrearAudioLocal)
    fun inject(crearVideoLocal: CrearVideoLocal)
    fun inject(crearVideoRemoto: CrearVideoRemoto)

    //casos uso
    fun inject(destruirAudioLocalCasoUso: DestruirAudioLocalCasoUso)
    fun inject(destruirVideoLocalCasoUso: DestruirVideoLocalCasoUso)
    fun inject(destruirVideoRemotoCasoUso: DestruirVideoRemotoCasoUso)
    fun inject(inicializarAudioLocalCasoUso: InicializarAudioLocalCasoUso)
    fun inject(inicializarVideoLocalCasoUso: InicializarVideoLocalCasoUso)
    fun inject(inicializarVideoRemotoCasoUso: InicializarVideoRemotoCasoUso)

    //viewmodels
    fun inject(streamFragmentViewModel: StreamFragmentViewModel)

    //fragments
    fun inject(streamFragment: StreamFragment)

    //activities
    fun inject(mainActivity: MainActivity)
}