package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.*
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.uses_cases.videollamada.*
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
    @Singleton @ApplicationScope @Provides fun providesCrearVideoRemoto() = CrearVideoRemoto()

    //tercer nivel

    @ApplicationScope @Provides fun providesDestruirAudioLocalCasoUso() = DestruirAudioLocalCasoUso()
    @ApplicationScope @Provides fun providesDestruirVideoLocalCasoUso() = DestruirVideoLocalCasoUso()
    @ApplicationScope @Provides fun providesDestruirVideoRemotoCasoUso() = DestruirVideoRemotoCasoUso()
    @ApplicationScope @Provides fun providesInicializarAudioLocalCasoUso() = InicializarAudioLocalCasoUso()
    @ApplicationScope @Provides fun providesInicializarVideoLocalCasoUso() = InicializarVideoLocalCasoUso()
    @ApplicationScope @Provides fun providesInicializarVideoRemotoCasoUso() = InicializarVideoRemotoCasoUso()
    @ApplicationScope @Provides fun providesTraerAudioTrackLocalCasoUso() = TraerAudioTrackLocalCasoUso()
    @ApplicationScope @Provides fun providesTraerVideoTrackLocalCasoUso() = TraerVideoTrackLocalCasoUso()

    //cuarto nivel

    @Singleton @ApplicationScope @Provides fun providesCrearStreamLocal() = CrearStreamLocal()

    //quinto nivel
    @ApplicationScope @Provides fun providesCrearStreamLocalCasoUso() = CrearStreamLocalCasoUso()
    @ApplicationScope @Provides fun providesDestruirStreamLocalCasoUso() = DestruirStreamLocalCasoUso()

}