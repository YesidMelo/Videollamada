package com.mi_tiempo.myapplication.uses_cases.videollamada

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.InteraccionEntreWebRTCYSocketVideollamada
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class IniciarVideollamadaWebRTCasoUso {

    @Inject lateinit var interaccionEntreWebRTCYSocketVideollamada: InteraccionEntreWebRTCYSocketVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(activity: AppCompatActivity,
               renderLocal: SurfaceViewRenderer,
               renderRemoto: SurfaceViewRenderer
    ) {
        interaccionEntreWebRTCYSocketVideollamada.iniciarVideollamadaWebRTC(activity, renderLocal, renderRemoto)
    }
}