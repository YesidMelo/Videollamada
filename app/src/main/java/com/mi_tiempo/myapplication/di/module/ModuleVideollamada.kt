package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.LogicaVideollamada
import com.mi_tiempo.myapplication.data_access.videollamada.PeerConnectionAdapter
import com.mi_tiempo.myapplication.data_access.videollamada.SDPObserverAdapter
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleVideollamada {

    //DataAccess
    @ApplicationScope @Provides fun providesSDPObserverAdapter() = SDPObserverAdapter()
    @ApplicationScope @Provides fun providesPeerConnectionAdapter() = PeerConnectionAdapter()

    @Singleton @ApplicationScope  @Provides fun providesLogicaVideollamada() = LogicaVideollamada()


}