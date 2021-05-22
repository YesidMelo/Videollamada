package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoLocal
import com.mi_tiempo.myapplication.data_access.videollamada.EstaticosVideollamada
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.uses_cases.videollamada.InicializarVideoLocalCasoUso
import dagger.Module
import dagger.Provides

@Module
class ModuleVideollamada {
    //Datasources
    //Primer nivel
    @ApplicationScope @Provides fun providesEstaticosVideollamada() = EstaticosVideollamada()

    //segundo nivel
    @ApplicationScope @Provides fun providesCrearVideoLocal() = CrearVideoLocal()

    //CasosUso
    //PrimerNivel
    @ApplicationScope @Provides fun providesInicializarVideoLocalCasoUso() = InicializarVideoLocalCasoUso()

}