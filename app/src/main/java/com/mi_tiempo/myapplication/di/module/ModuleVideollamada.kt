package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.*
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.uses_cases.videollamada.DesvincularDelSocketDeVideollamadaCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.FinalizarVideollamadaWebRTCCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.IniciarVideollamadaWebRTCasoUso
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
    @Singleton @ApplicationScope  @Provides fun providesInteraccionEntreWebRTCYSocketVideollamada() = InteraccionEntreWebRTCYSocketVideollamada()

    //casos de uso
    @Provides @ApplicationScope fun providesDesvincularDelSocketDeVideollamadaCasoUso() = DesvincularDelSocketDeVideollamadaCasoUso()
    @Provides @ApplicationScope fun providesFinalizarVideollamadaWebRTCCasoUso() = FinalizarVideollamadaWebRTCCasoUso()
    @Provides @ApplicationScope fun providesIniciarVideollamadaWebRTCasoUso() = IniciarVideollamadaWebRTCasoUso()
    @Provides @ApplicationScope fun providesRegistrarUsuarioVideollamadaEnServidorCasoUso() = RegistrarUsuarioVideollamadaEnServidorCasoUso()


}