package com.mi_tiempo.myapplication.ui.stream_fragment

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaVideollamada
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    val TAG = "StreamFragmentViewModel"

    @Inject lateinit var logicaVideollamada: LogicaVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        logicaVideollamada.destruirVideollamada()
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        logicaVideollamada.iniciarVideollamada(activity, renderLocal, renderRemoto)
        logicaVideollamada.conEscuchadorEnviarASocket {
            objeto->
            Log.e(TAG, "objeto: ${objeto?.toString()}")
        }
    }
}