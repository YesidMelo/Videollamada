package com.mi_tiempo.myapplication.uses_cases.videollamada

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoRemoto
import javax.inject.Inject

class TraerVideoTrackRemotoCasoUso {

    @Inject lateinit var crearVideoRemoto: CrearVideoRemoto

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke() = crearVideoRemoto.traerVideoTrack()
}