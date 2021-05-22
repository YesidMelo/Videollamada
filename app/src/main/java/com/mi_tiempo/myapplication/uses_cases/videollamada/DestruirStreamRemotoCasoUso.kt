package com.mi_tiempo.myapplication.uses_cases.videollamada

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearStreamRemoto
import javax.inject.Inject

class DestruirStreamRemotoCasoUso {

    @Inject lateinit var crearStreamRemoto : CrearStreamRemoto

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke() {
        crearStreamRemoto.destruir()
    }
}