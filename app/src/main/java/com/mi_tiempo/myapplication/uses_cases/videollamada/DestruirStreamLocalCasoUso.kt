package com.mi_tiempo.myapplication.uses_cases.videollamada

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearStreamLocal
import javax.inject.Inject

class DestruirStreamLocalCasoUso {

    @Inject lateinit var crearStreamLocal : CrearStreamLocal

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke() {
        crearStreamLocal.destruir()
    }
}