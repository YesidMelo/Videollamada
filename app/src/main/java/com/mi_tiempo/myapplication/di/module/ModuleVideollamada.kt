package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.LogicaSocketVideollamada
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaWebRTC
import com.mi_tiempo.myapplication.data_access.videollamada.PeerConnectionAdapter
import com.mi_tiempo.myapplication.data_access.videollamada.SDPObserverAdapter
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.uses_cases.videollamada.DesvincularDelSocketDeVideollamadaCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.RegistrarUsuarioVideollamadaEnServidorCasoUso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleVideollamada {

    //DataAccess
    @ApplicationScope @Provides fun providesSDPObserverAdapter() = SDPObserverAdapter()
    @ApplicationScope @Provides fun providesPeerConnectionAdapter() = PeerConnectionAdapter()

    @Singleton @ApplicationScope  @Provides fun providesLogicaWebRTC() = LogicaWebRTC()
    @Singleton @ApplicationScope  @Provides fun providesLogicaSocketVideollamada() = LogicaSocketVideollamada()

    //casos de uso
    @Provides @ApplicationScope fun providesRegistrarUsuarioVideollamadaEnServidorCasoUso() = RegistrarUsuarioVideollamadaEnServidorCasoUso()
    @Provides @ApplicationScope fun providesDesvincularDelSocketDeVideollamadaCasoUso() = DesvincularDelSocketDeVideollamadaCasoUso()


}