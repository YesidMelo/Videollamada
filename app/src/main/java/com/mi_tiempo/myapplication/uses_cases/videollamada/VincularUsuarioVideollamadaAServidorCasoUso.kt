package com.mi_tiempo.myapplication.uses_cases.videollamada

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.InteraccionEntreWebRTCYSocketVideollamada
import javax.inject.Inject

class VincularUsuarioVideollamadaAServidorCasoUso {

    @Inject lateinit var interaccionEntreWebRTCYSocketVideollamada : InteraccionEntreWebRTCYSocketVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(usuarioActual: String) {
        interaccionEntreWebRTCYSocketVideollamada.vincularSocketVideollamada(usuarioActual)
    }

}