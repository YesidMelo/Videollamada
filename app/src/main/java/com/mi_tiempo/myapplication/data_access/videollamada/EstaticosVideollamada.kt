package com.mi_tiempo.myapplication.data_access.videollamada

import androidx.appcompat.app.AppCompatActivity
import org.webrtc.EglBase
import org.webrtc.PeerConnectionFactory
import javax.inject.Singleton

@Singleton
class EstaticosVideollamada {

    private var rootEglBase : EglBase.Context? = null
    private var factory : PeerConnectionFactory? = null

    //metodos publicos
    fun inicializarComponentes(activity: AppCompatActivity) {
        if (rootEglBase == null) {
            rootEglBase = EglBase.create().eglBaseContext
        }
        if (factory == null) {
            val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(activity).createInitializationOptions()
            PeerConnectionFactory.initialize(initializationOptions)
            factory = PeerConnectionFactory.builder().createPeerConnectionFactory()
        }
    }

    fun destruir() {
        rootEglBase = null
        factory = null
    }

    fun traerRootEglBaseContext() = rootEglBase
    fun traerPeerConnectionFactory() = factory
}