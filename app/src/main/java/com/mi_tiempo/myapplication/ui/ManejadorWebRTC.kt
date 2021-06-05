package com.mi_tiempo.myapplication.ui

import android.content.Context
import android.util.Log
import org.json.JSONObject
import org.webrtc.*

class ManejadorWebRTC(
    val applicationContext: Context,
    val videoLocal: SurfaceViewRenderer,
    val videoRemoto: SurfaceViewRenderer,
    val mensajesParASocket : (JSONObject)->Unit
    ) {

    val TAG = "ManejadorWebRTC"
    val eglBaseContext= EglBase.create().eglBaseContext
    val options = PeerConnectionFactory.Options()
    val defaultVideoDecoderFactory = DefaultVideoDecoderFactory(eglBaseContext)
    val defaultVideoEncoderFactory = DefaultVideoEncoderFactory(eglBaseContext, true, true)
    lateinit var peerConnectionFactory : PeerConnectionFactory
    val iceServers = arrayListOf(PeerConnection.IceServer("stun:stun.l.google.com:19302"))
    val ANCHO = 480
    val ALTO = 600
    val FPS = 30
    lateinit var peerConnection: PeerConnection
    lateinit var videoTrackLocal : VideoTrack
    lateinit var audioTrackLocal : AudioTrack
    val sdpAdapterAdicionarOferta = SdpAdapater()
    val sdpAdapterAdicionarRespuesta = SdpAdapater()
    val sdpAdapterCrearOferta = SdpAdapater()
    val sdpAdapterCrearRespuesta = SdpAdapater()

    //4. iniciar llamada
    fun iniciarllamada() {
        iniciarConfiguracionWebRTC()
        val mediaStream = peerConnectionFactory.createLocalMediaStream("ARDAMS")
        mediaStream.addTrack(videoTrackLocal)
        mediaStream.addTrack(audioTrackLocal)
        peerConnection.addStream(mediaStream)

        enviaMensaje(JSONObject().apply {
            put(MainActivity.EtiquetasJsonObject.type.traerNombre(), MainActivity.EtiquetasJsonObject.got_user_media.traerNombre())
        })
    }

    fun finalizarLlamada() {
        peerConnection.dispose()
    }

    fun enviaMensaje( jsonObject: JSONObject) {
        Log.e(TAG,"Objeto a enviar : $jsonObject")
        mensajesParASocket.invoke(jsonObject)
    }

    //1. primer paso de la negociacion
    fun crearOfertaVideollamada() {
        peerConnection.createOffer(
                sdpAdapterCrearOferta.conEscuchador(::escuchadorSDPAdapterCrearOferta),
                MediaConstraints()
        )
    }

    //2. segundo paso de la negociacion
    fun adicionarCandidato(jsonObject: JSONObject) {
        val iceCandidate = IceCandidate(
            jsonObject.getString(MainActivity.EtiquetasJsonObject.id.traerNombre()),
            jsonObject.getInt(MainActivity.EtiquetasJsonObject.label.traerNombre()),
            jsonObject.getString(MainActivity.EtiquetasJsonObject.candidate.traerNombre())
        )
        peerConnection.addIceCandidate(iceCandidate)
    }

    //3. tercer paso de la negociacion
    fun adicionarOferta(jsonObject: JSONObject) {
        peerConnection.setRemoteDescription(
            sdpAdapterAdicionarOferta.conEscuchador(::escuchadorSDPAdapterAdicionarOferta),
            SessionDescription(
                SessionDescription.Type.OFFER,
                jsonObject.getString(MainActivity.EtiquetasJsonObject.sdp.traerNombre())
            )
        )
        hacerRespuesta()
    }

    //4. cuarto paso en la negociacion
    fun adicionarRespuesta(jsonObject: JSONObject) {
        peerConnection.setRemoteDescription(
            sdpAdapterAdicionarRespuesta.conEscuchador(::escuchadorSDPAdapterAdicionarRespuesta),
            SessionDescription(SessionDescription.Type.ANSWER, jsonObject.getString(MainActivity.EtiquetasJsonObject.sdp.traerNombre()))
        )
    }

    ///Logica de la videollamada

    private fun iniciarConfiguracionWebRTC() {
        configuracionPeerConnectionFactory()
        configurarVideoLocal()
        configuracionAudioLocal()
        configurarPeerConnection()
    }

    //1. configurar peerConnectionFactory
    private fun configuracionPeerConnectionFactory() {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(applicationContext).createInitializationOptions())
        peerConnectionFactory = PeerConnectionFactory
            .builder()
            .setOptions(options)
            .setVideoDecoderFactory(defaultVideoDecoderFactory)
            .setVideoEncoderFactory(defaultVideoEncoderFactory)
            .createPeerConnectionFactory()
    }


    //2. configuracion tracks locales
    private fun configurarVideoLocal() {
        val textureHelper = SurfaceTextureHelper.create("texturehelper local", eglBaseContext)
        val videoCapturer = createCameraCapturer(true)
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer.initialize(textureHelper, this.applicationContext, videoSource.capturerObserver)
        videoCapturer.startCapture(ANCHO, ALTO, FPS)
        videoLocal.setMirror(true)
        videoLocal.init(eglBaseContext, null)
        videoTrackLocal = peerConnectionFactory.createVideoTrack("videolocal", videoSource)
        videoTrackLocal.addSink(videoLocal)
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

    private fun configuracionAudioLocal() {
        val audioConstraints = MediaConstraints()
        val audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
        audioTrackLocal = peerConnectionFactory.createAudioTrack("101", audioSource)
    }

    ///3. configurar Peerconnection
    private fun configurarPeerConnection() {
        val adapter = PeerConnectionAdapter()
        peerConnection = peerConnectionFactory.createPeerConnection(
            iceServers,
            adapter.conEscuchador(::escuchadorRespuestasPeerconnectionAdaper)
        )!!
    }

    private fun escuchadorRespuestasPeerconnectionAdaper(respuesta : RespuestasPeerConnectionObserver, objeto1:  Any?,objeto2: Any?) {
        Log.e(TAG, " respuesta Observer $respuesta")
        when(respuesta){
            RespuestasPeerConnectionObserver.onIceCandidate -> enviarOnIceCandidate(objeto1)
            RespuestasPeerConnectionObserver.onAddStream -> recibirStreamRemoto(objeto1, objeto2)
            else -> {}
        }
    }

    private fun enviarOnIceCandidate(objeto1: Any?) {
        if (objeto1 !is IceCandidate) {
            return
        }
        enviaMensaje(JSONObject().apply {
            put(MainActivity.EtiquetasJsonObject.type.traerNombre(), MainActivity.EtiquetasJsonObject.candidate.traerNombre())
            put(MainActivity.EtiquetasJsonObject.label.traerNombre(), objeto1.sdpMLineIndex)
            put(MainActivity.EtiquetasJsonObject.id.traerNombre(), objeto1.sdpMid)
            put(MainActivity.EtiquetasJsonObject.candidate.traerNombre(), objeto1.sdp)
        })
    }

    private fun escuchadorSDPAdapterCrearOferta(respuesta: RespuestasSDPAdapter, objeto:  Any?) {
        Log.e(TAG, "respuesta sdp $respuesta")
        when(respuesta){
            RespuestasSDPAdapter.onCreateSuccess -> {
                if (objeto !is SessionDescription) {
                    Log.e(TAG, "El objeto no es un Session description")
                    return
                }
                peerConnection.setLocalDescription(sdpAdapterCrearOferta, objeto)
                val jsonObject = JSONObject()
                jsonObject.put(MainActivity.EtiquetasJsonObject.type.traerNombre(), MainActivity.EtiquetasJsonObject.offer.traerNombre())
                jsonObject.put(MainActivity.EtiquetasJsonObject.sdp.traerNombre(), objeto.description)
                enviaMensaje(jsonObject)
            }
            else ->{}
        }
    }



    private fun hacerRespuesta() {
        peerConnection.createAnswer(
            sdpAdapterCrearRespuesta.conEscuchador(::escuchadorSDPAdapterAdicionarOferta),
            MediaConstraints()
        )
    }

    private fun escuchadorSDPAdapterAdicionarOferta(respuesta: RespuestasSDPAdapter, objeto:  Any?) {
        Log.e(TAG, "respuesta sdp $respuesta")
        when(respuesta){
            RespuestasSDPAdapter.onCreateSuccess -> {
                if (objeto !is SessionDescription) {
                    Log.e(TAG, "El objeto no es un Session description")
                    return
                }
                peerConnection.setLocalDescription(sdpAdapterCrearOferta, objeto)
                val jsonObject = JSONObject()
                jsonObject.put(MainActivity.EtiquetasJsonObject.type.traerNombre(), MainActivity.EtiquetasJsonObject.answer.traerNombre())
                jsonObject.put(MainActivity.EtiquetasJsonObject.sdp.traerNombre(), objeto.description)
                enviaMensaje(jsonObject)
            }
            else ->{}
        }
    }

    private fun escuchadorSDPAdapterAdicionarRespuesta(respuesta: RespuestasSDPAdapter, objeto:  Any?) {
        Log.e(TAG, "respuesta sdp $respuesta")
    }

    private fun recibirStreamRemoto(objeto1: Any?, objeto2: Any?) {
        if (objeto1 == null) {
            Log.e(TAG, "No hay media stream remoto")
            return
        }
        if (objeto1 !is MediaStream) {
            Log.e(TAG, "no es un media stream remoto")
            return
        }
        configurarVideoRemoto()
        adicionarStreamRemoto(objeto1)
        Log.e(TAG, "Me ha llegado la los streams")
    }

    private fun configurarVideoRemoto() {
        videoRemoto.post {
            videoRemoto.init(eglBaseContext,null)
            videoRemoto.setEnableHardwareScaler(true)
            videoRemoto.setMirror(true)
        }
    }

    private fun adicionarStreamRemoto(objeto1: MediaStream) {
        val videoTrackRemoto: VideoTrack? = if (objeto1.videoTracks.isEmpty()) null else objeto1.videoTracks.first()
        val audioTrackRemoto : AudioTrack? = if (objeto1.audioTracks.isEmpty()) null else objeto1.audioTracks.first()

        audioTrackRemoto?.setEnabled(true)
        videoTrackRemoto?.setEnabled(true)
        videoTrackRemoto?.addSink(videoRemoto)

    }

    inner class SdpAdapater : SdpObserver {

        private var escuchador: ((RespuestasSDPAdapter, Any?)->Unit)? = null

        fun conEscuchador(escuchador: ((RespuestasSDPAdapter, Any?)->Unit)?) : SdpAdapater {
            this.escuchador = escuchador
            return this
        }

        override fun onCreateSuccess(p0: SessionDescription?) {
            escuchador?.invoke(RespuestasSDPAdapter.onCreateSuccess, p0)
        }

        override fun onSetSuccess() {
            escuchador?.invoke(RespuestasSDPAdapter.onSetSuccess, null)
        }

        override fun onCreateFailure(p0: String?) {
            escuchador?.invoke(RespuestasSDPAdapter.onCreateFailure, p0)
        }

        override fun onSetFailure(p0: String?) {
            escuchador?.invoke(RespuestasSDPAdapter.onSetFailure, p0)
        }

    }

    enum class RespuestasSDPAdapter {
        onCreateSuccess,
        onSetSuccess,
        onCreateFailure,
        onSetFailure,
        ;
    }

    inner class PeerConnectionAdapter : PeerConnection.Observer {

        private var escuchador : ((RespuestasPeerConnectionObserver, Any?, Any?)->Unit)? = null

        fun conEscuchador(escuchador : ((RespuestasPeerConnectionObserver, Any?, Any?)->Unit)?) : PeerConnectionAdapter{
            this.escuchador = escuchador
            return this
        }

        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onSignalingChange, p0, null)
        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onIceConnectionChange, p0, null)
        }

        override fun onIceConnectionReceivingChange(p0: Boolean) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onIceConnectionReceivingChange, p0, null)
        }

        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onIceGatheringChange, p0, null)
        }

        override fun onIceCandidate(p0: IceCandidate?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onIceCandidate, p0, null)
        }

        override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onIceCandidatesRemoved, p0, null)
        }

        override fun onAddStream(p0: MediaStream?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onAddStream, p0, null)
        }

        override fun onRemoveStream(p0: MediaStream?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onRemoveStream, p0, null)
        }

        override fun onDataChannel(p0: DataChannel?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onDataChannel, p0, null)
        }

        override fun onRenegotiationNeeded() {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onRenegotiationNeeded, null, null)
        }

        override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
            escuchador?.invoke(RespuestasPeerConnectionObserver.onAddTrack, p0, p1)
        }
    }

    enum class RespuestasPeerConnectionObserver {
        onSignalingChange,
        onIceConnectionChange,
        onIceConnectionReceivingChange,
        onIceGatheringChange,
        onIceCandidate,
        onIceCandidatesRemoved,
        onAddStream,
        onRemoveStream,
        onDataChannel,
        onRenegotiationNeeded,
        onAddTrack,
        ;
    }
}