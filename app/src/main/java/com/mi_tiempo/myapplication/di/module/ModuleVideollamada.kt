package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.CrearAudioLocal
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoLocal
import com.mi_tiempo.myapplication.data_access.videollamada.EstaticosVideollamada
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.uses_cases.videollamada.DestruirAudioLocalCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.DestruirVideoLocalCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.InicializarAudioLocalCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.InicializarVideoLocalCasoUso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleVideollamada {
    //Datasources
    //Primer nivel
    @ApplicationScope @Provides fun providesEstaticosVideollamada() = EstaticosVideollamada()

    //segundo nivel
    @Singleton @ApplicationScope @Provides fun providesCrearAudioLocal() = CrearAudioLocal()
    @Singleton @ApplicationScope @Provides fun providesCrearVideoLocal() = CrearVideoLocal()

    //CasosUso
    //PrimerNivel
    @ApplicationScope @Provides fun providesDestruirAudioLocalCasoUso() = DestruirAudioLocalCasoUso()
    @ApplicationScope @Provides fun providesDestruirVideoLocalCasoUso() = DestruirVideoLocalCasoUso()
    @ApplicationScope @Provides fun providesInicializarAudioLocalCasoUso() = InicializarAudioLocalCasoUso()
    @ApplicationScope @Provides fun providesInicializarVideoLocalCasoUso() = InicializarVideoLocalCasoUso()

}