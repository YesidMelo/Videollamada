package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.json.JSONObject
import org.webrtc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogicaWebRTC {

    val TAG = "LogicaVideollamada"
    val ANCHO = 480
    val ALTO = 640
    val FPS = 30
    lateinit var eglBaseContext: EglBase.Context
    lateinit var options: PeerConnectionFactory.Options
    lateinit var defaultVideoEncoderFactory: DefaultVideoEncoderFactory
    lateinit var defaultVideoDecoderFactory: DefaultVideoDecoderFactory
    lateinit var peerConnectionFactory: PeerConnectionFactory
    lateinit var peerConnectionLocal: PeerConnection
    lateinit var mediaStreamLocal: MediaStream
    lateinit var mediaStreamRemoto: MediaStream
    lateinit var textureHelperLocal : SurfaceTextureHelper
    lateinit var textureHelperRemoto : SurfaceTextureHelper
    lateinit var videoSourceLocal : VideoSource
    lateinit var videoSourceRemoto : VideoSource
    lateinit var videoTrackLocal: VideoTrack
    lateinit var videoTrackRemoto: VideoTrack
    lateinit var audioTrackRemoto: AudioTrack
    lateinit var surfaceViewRendererLocal: SurfaceViewRenderer
    lateinit var surfaceViewRendererRemoto: SurfaceViewRenderer

    val nombreHiloTextureHelperLocal = "TextureHelperLocal"
    val nombreHiloTextureHelperRemoto = "TextureHelperRemoto"
    val IDVIDEOTRACKLOCAL = "videotracklocal"
    val IDVIDEOTRACKREMOTO = "videotrackRemoto"
    val IDMEDIASTREAMLOCAL= "mediaStreamLocal"
    val IDMEDIASTREAMREMOTO= "mediaStreamRemoto"

    private var mensaje : JSONObject? = null
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
        peerConnectionLocal.removeStream(mediaStreamLocal)
        peerConnectionLocal.dispose()
        peerConnectionLocal.close()
    }

    fun iniciarVideollamada(
        activity: AppCompatActivity,
        videoLocal: SurfaceViewRenderer,
        videoRemoto: SurfaceViewRenderer,
    ) {
        configurarVideollamada(activity, videoLocal, videoRemoto)
    }

    fun traerMensajeAEnviar() = mensaje


    ///Metodos privados

    private fun configurarVideollamada(
        activity: AppCompatActivity,
        videoLocal: SurfaceViewRenderer,
        videoRemoto: SurfaceViewRenderer,
    ) {
        eglBaseContext = EglBase.create().eglBaseContext
        this.surfaceViewRendererLocal = videoLocal
        this.surfaceViewRendererRemoto = videoRemoto

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
        textureHelperLocal = SurfaceTextureHelper.create(nombreHiloTextureHelperLocal, eglBaseContext)
        val videoCapturerLocal = createCameraCapturer(true)
        videoSourceLocal = peerConnectionFactory.createVideoSource(videoCapturerLocal!!.isScreencast)
        videoCapturerLocal.initialize(textureHelperLocal, activity.applicationContext, videoSourceLocal.capturerObserver)
        videoCapturerLocal.startCapture(ANCHO, ALTO, FPS)
        videoLocal.setMirror(true)
        videoLocal.init(eglBaseContext,null)
        videoTrackLocal = peerConnectionFactory.createVideoTrack(IDVIDEOTRACKLOCAL, videoSourceLocal)
        ///Muestra en pantalla la camara local
        videoTrackLocal.addSink(surfaceViewRendererLocal)

        //videoRemoto
        textureHelperRemoto = SurfaceTextureHelper.create(nombreHiloTextureHelperRemoto, eglBaseContext)
        val videoCapturerRemoto = createCameraCapturer(false)
        videoSourceRemoto = peerConnectionFactory.createVideoSource(videoCapturerRemoto!!.isScreencast)
        videoCapturerRemoto.initialize(textureHelperRemoto, activity.applicationContext, videoSourceRemoto.capturerObserver)
        videoCapturerRemoto.startCapture(ANCHO, ALTO, FPS)
        videoRemoto.setMirror(false)
        videoRemoto.init(eglBaseContext,null)
        videoTrackRemoto = peerConnectionFactory.createVideoTrack(IDVIDEOTRACKREMOTO, videoSourceRemoto)
        ///Muestra en pantalla la camara local
        //videoTrackRemoto.addSink(surfaceViewRendererRemoto)

        mediaStreamLocal = peerConnectionFactory.createLocalMediaStream(IDMEDIASTREAMLOCAL)
        mediaStreamLocal.addTrack(videoTrackLocal)

        mediaStreamRemoto = peerConnectionFactory.createLocalMediaStream(IDMEDIASTREAMREMOTO)
        mediaStreamRemoto.addTrack(videoTrackRemoto)

        call()

    }

    private fun createCameraCapturer(isFront: Boolean) : VideoCapturer?{
        val enumerator = Camera1Enumerator(false)
        val listaNombresDispositivos = enumerator.deviceNames

        for (nombreDispositivo in listaNombresDispositivos) {
            if (if (isFront) enumerator.isFrontFacing(nombreDispositivo) else enumerator.isBackFacing(nombreDispositivo)) {
                val videoCapturer = enumerator.createCapturer(nombreDispositivo, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null
    }


    private fun call() {
        val iceServers = arrayOf<PeerConnection.IceServer>(

            PeerConnection.IceServer("stun:stun.l.google.com:19302"),
            /*
                PeerConnection.IceServer("stun:stun1.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun2.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun3.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun4.l.google.com:19302")
            */
        ).toMutableList()

        peerConnectionLocal = peerConnectionFactory.createPeerConnection(
            iceServers,
            peerConnectionAdapter.conEscuchador {
                respuesta, any, any2 ->
                when(respuesta) {
                    PeerConnectionAdapter.Respuesta.onRenegotiationNeeded -> accionOnRenegotiationNeeded()
                    PeerConnectionAdapter.Respuesta.onIceCandidate -> accionOnIceCandidate(any)
                    PeerConnectionAdapter.Respuesta.onAddStream -> accionOnAddStream(any)
                    else -> {
                        Log.e(TAG, " llego a $respuesta")
                    }
                }
            }
        )!!
        peerConnectionLocal.addStream(mediaStreamLocal)
    }

    private fun accionOnAddStream(objeto: Any?) {
        if (objeto !is MediaStream) {
            return
        }

        videoTrackRemoto = objeto.videoTracks.first()
        audioTrackRemoto = objeto.audioTracks.first()

        videoTrackRemoto.setEnabled(true)
        audioTrackRemoto.setEnabled(true)

        videoTrackRemoto.addSink(surfaceViewRendererRemoto)
    }

    private fun accionOnIceCandidate(objeto: Any?) {

        if (objeto !is IceCandidate) {
            return
        }

        mensaje = JSONObject()
        mensaje?.put("type", "candidate")
        mensaje?.put("label", objeto.sdpMLineIndex)
        mensaje?.put("id", objeto.sdpMid)
        mensaje?.put("candidate", objeto.sdp)

        escuchadorEnviarASocket?.invoke(mensaje)
    }

    private fun accionOnRenegotiationNeeded() {
        peerConnectionLocal.createOffer(
            sdpObserverAdapter.conEscuchador {
                respuestas, any ->
                when(respuestas) {
                    SDPObserverAdapter.Respuestas.onCreateSuccess -> {
                        if (any !is SessionDescription) {
                            Log.e(TAG, "sessionDescription es nulo")
                            return@conEscuchador
                        }
                        peerConnectionLocal.setLocalDescription(sdpObserverAdapter, any)
                    }

                    else -> {
                        Log.e(TAG, "Recibio respuesta en $respuestas")
                    }
                }
            },
            MediaConstraints()
        )
    }

}