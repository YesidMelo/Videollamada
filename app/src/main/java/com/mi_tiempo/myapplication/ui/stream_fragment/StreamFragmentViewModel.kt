package com.mi_tiempo.myapplication.ui.stream_fragment

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaWebRTC
import com.mi_tiempo.myapplication.uses_cases.videollamada.FinalizarVideollamadaWebRTCCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.IniciarVideollamadaWebRTCasoUso
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    @Inject lateinit var iniciarVideollamadaWebRTCasoUso: IniciarVideollamadaWebRTCasoUso
    @Inject lateinit var finalizarVideollamadaWebRTCCasoUso: FinalizarVideollamadaWebRTCCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        finalizarVideollamadaWebRTCCasoUso.invoke()
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        iniciarVideollamadaWebRTCasoUso.invoke(activity, renderLocal, renderRemoto)
    }
}