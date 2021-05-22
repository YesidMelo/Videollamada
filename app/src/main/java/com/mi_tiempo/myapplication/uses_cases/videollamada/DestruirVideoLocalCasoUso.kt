package com.mi_tiempo.myapplication.uses_cases.videollamada

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoLocal
import javax.inject.Inject

class DestruirVideoLocalCasoUso {

    @Inject lateinit var crearVideoLocal: CrearVideoLocal

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke() {
        crearVideoLocal.destruir()
    }

}