package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class InteraccionEntreWebRTCYSocketVideollamada {

    private val TAG = "InteraccionEntreWebRTCYSocketVideollamada"
    @Inject lateinit var logicaWebRTC : LogicaWebRTC
    @Inject lateinit var logicaSocketVideollamada : LogicaSocketVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruirVideollamadaWebRTC() {
        logicaWebRTC.destruirVideollamada()
    }

    fun desvincularSocketVideollamada() {
        logicaSocketVideollamada.finalizarConexion()
    }

    fun iniciarVideollamadaWebRTC(
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

    fun vincular(usuarioActual: String) {
        logicaSocketVideollamada.conUsuarioActual(usuarioActual)
        logicaSocketVideollamada.conEscuchadorConexion{
            canales, any ->
            Log.e(TAG, "se ha conectado $canales")
        }
        logicaSocketVideollamada.registrarConexion()
    }
}