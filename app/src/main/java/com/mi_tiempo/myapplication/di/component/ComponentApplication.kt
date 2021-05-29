package com.mi_tiempo.myapplication.di.component

import com.mi_tiempo.myapplication.data_access.videollamada.*
import com.mi_tiempo.myapplication.di.module.ModuleApplication
import com.mi_tiempo.myapplication.di.module.ModuleFragment
import com.mi_tiempo.myapplication.di.module.ModuleVideollamada
import com.mi_tiempo.myapplication.di.module.ModuleViewModel
import com.mi_tiempo.myapplication.ui.MainActivity
import com.mi_tiempo.myapplication.ui.MainActivityViewModel
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
    // DataAccess
    fun inject(logicaWebRTC: LogicaWebRTC)
    fun inject(logicaSocketVideollamada: LogicaSocketVideollamada)
    fun inject(interaccionEntreWebRTCYSocketVideollamada: InteraccionEntreWebRTCYSocketVideollamada)

    //casos de uso
    fun inject(desvincularDelSocketDeVideollamadaCasoUso: DesvincularDelSocketDeVideollamadaCasoUso)
    fun inject(finalizarVideollamadaWebRTCCasoUso: FinalizarVideollamadaWebRTCCasoUso)
    fun inject(iniciarVideollamadaWebRTCasoUso: IniciarVideollamadaWebRTCasoUso)
    fun inject(salirDeSalaCasoUso: SalirDeSalaCasoUso)
    fun inject(unirmeASalaVideollamadaCasoUso: UnirmeASalaVideollamadaCasoUso)
    fun inject(vincularUsuarioVideollamadaAServidorCasoUso: VincularUsuarioVideollamadaAServidorCasoUso)

    //viewmodels
    fun inject(streamFragmentViewModel: StreamFragmentViewModel)
    fun inject(mainActivityViewModel: MainActivityViewModel)

    //fragments
    fun inject(streamFragment: StreamFragment)

    //activities
    fun inject(mainActivity: MainActivity)
}