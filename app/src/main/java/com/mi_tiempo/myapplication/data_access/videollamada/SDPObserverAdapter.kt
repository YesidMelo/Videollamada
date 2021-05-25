package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

class SDPObserverAdapter : SdpObserver {

    val TAG = "SDPObserverAdapter"
    private var escuchador : ((Respuestas, Any?)->Unit)? = null

    fun conEscuchador(escuchador : ((Respuestas, Any?)->Unit)?) : SDPObserverAdapter {
        this.escuchador = escuchador
        return this
    }

    override fun onCreateSuccess(p0: SessionDescription?) {
        Log.e(TAG,"onCreateSuccess")
        escuchador?.invoke(Respuestas.onCreateSuccess,p0)
    }

    override fun onSetSuccess() {
        Log.e(TAG,"onSetSuccess")
        escuchador?.invoke(Respuestas.onSetSuccess, null)
    }

    override fun onCreateFailure(p0: String?) {
        Log.e(TAG,"onCreateFailure")
        escuchador?.invoke(Respuestas.onCreateFailure, p0)
    }

    override fun onSetFailure(p0: String?) {
        Log.e(TAG,"onSetFailure")
        escuchador?.invoke(Respuestas.onSetFailure, p0)
    }

    enum class Respuestas {
        onCreateSuccess,
        onSetSuccess,
        onCreateFailure,
        onSetFailure,
    }
}