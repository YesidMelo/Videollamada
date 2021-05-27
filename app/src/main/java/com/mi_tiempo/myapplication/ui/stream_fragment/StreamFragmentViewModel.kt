package com.mi_tiempo.myapplication.ui.stream_fragment

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaWebRTC
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    val TAG = "StreamFragmentViewModel"

    @Inject lateinit var logicaWebRTC: LogicaWebRTC

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        logicaWebRTC.destruirVideollamada()
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        logicaWebRTC.iniciarVideollamada(activity, renderLocal, renderRemoto)
        logicaWebRTC.conEscuchadorEnviarASocket {
            objeto->
            Log.e(TAG, "objeto: ${objeto?.toString()}")
        }
    }
}