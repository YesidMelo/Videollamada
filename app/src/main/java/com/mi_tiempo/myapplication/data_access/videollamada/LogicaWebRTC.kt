package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.stream.ConfiguracionStreamLocal
import com.mi_tiempo.myapplication.data_access.videollamada.stream.ConfiguracionStreamRemoto
import org.json.JSONObject
import org.webrtc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogicaWebRTC {

    val TAG = "LogicaVideollamada"
    lateinit var eglBaseContext: EglBase.Context
    lateinit var options: PeerConnectionFactory.Options
    lateinit var defaultVideoEncoderFactory: DefaultVideoEncoderFactory
    lateinit var defaultVideoDecoderFactory: DefaultVideoDecoderFactory
    lateinit var peerConnectionFactory: PeerConnectionFactory
    lateinit var configuracionStreamLocal: ConfiguracionStreamLocal
    lateinit var configuracionStreamRemoto: ConfiguracionStreamRemoto

    private var soyReceptor : Boolean = false

    private var escuchadorEnviarASocket : ((JSONObject?)->Unit)? = null

    @Inject lateinit var sdpObserverAdapter: SDPObserverAdapter
    @Inject lateinit var peerConnectionAdapter: PeerConnectionAdapter

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    /// Metodos publicos

    fun conEscuchadorEnviarASocket(escuchadorEnviarASocket : ((JSONObject?)->Unit)?) : LogicaWebRTC{
        this.escuchadorEnviarASocket = escuchadorEnviarASocket
        return this
    }


    fun destruirVideollamada(){
        configuracionStreamLocal.destruir()
        configuracionStreamRemoto.destruir()
    }

    fun iniciarVideollamada(
        activity: AppCompatActivity,
        videoLocal: SurfaceViewRenderer,
        videoRemoto: SurfaceViewRenderer,
    ) {
        configurarVideollamada(activity, videoLocal, videoRemoto)
    }

    fun traerMensajeLocalAEnviar() = configuracionStreamLocal.traerMensaje()
    fun traerMensajeRemotoAEnviar() = configuracionStreamRemoto.traerMensaje()

    fun recibirOferta(jsonObject: JSONObject) = configuracionStreamRemoto.recibirOferta(jsonObject)


    ///Metodos privados

    //Configuracion Principal WebRTC
    private fun configurarVideollamada(
        activity: AppCompatActivity,
        videoLocal: SurfaceViewRenderer,
        videoRemoto: SurfaceViewRenderer,
    ) {
        eglBaseContext = EglBase.create().eglBaseContext


        //create peerConnection factory
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(activity).createInitializationOptions())
        options = PeerConnectionFactory.Options()
        defaultVideoDecoderFactory = DefaultVideoDecoderFactory(eglBaseContext)
        defaultVideoEncoderFactory = DefaultVideoEncoderFactory(eglBaseContext, true, true)
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .setVideoEncoderFactory(defaultVideoEncoderFactory)
            .setVideoDecoderFactory(defaultVideoDecoderFactory)
            .createPeerConnectionFactory()

        //VideoLocal
        configuracionStreamLocal = ConfiguracionStreamLocal(
                activity = activity,
                eglBaseContext= eglBaseContext,
                escuchadorEnviarASocket= escuchadorEnviarASocket,
                peerConnectionFactory= peerConnectionFactory,
                videoLocal= videoLocal
        )
        configuracionStreamLocal.iniciarVideollamada()

        //VideoRemoto
        configuracionStreamRemoto = ConfiguracionStreamRemoto(
                activity = activity,
                eglBaseContext= eglBaseContext,
                escuchadorEnviarASocket= escuchadorEnviarASocket,
                peerConnectionFactory= peerConnectionFactory,
                videoRemoto= videoRemoto
        )
        configuracionStreamRemoto.iniciarVideollamada()

    }

}