package com.mi_tiempo.myapplication.data_access.videollamada.stream

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.data_access.videollamada.ConstantesVideollamada
import com.mi_tiempo.myapplication.data_access.videollamada.PeerConnectionAdapter
import com.mi_tiempo.myapplication.data_access.videollamada.SDPObserverAdapter
import org.json.JSONObject
import org.webrtc.*

class ConfiguracionStreamLocal(
    activity: AppCompatActivity,
    eglBaseContext: EglBase.Context,
    escuchadorEnviarASocket : ((JSONObject?)->Unit)?,
    peerConnectionFactory: PeerConnectionFactory,
    videoLocal: SurfaceViewRenderer
) : BaseStream( activity, eglBaseContext, escuchadorEnviarASocket , peerConnectionFactory, videoLocal){

    lateinit var peerConnectionLocal: PeerConnection
    lateinit var mediaStreamLocal: MediaStream
    lateinit var textureHelperLocal : SurfaceTextureHelper
    lateinit var videoSourceLocal : VideoSource
    lateinit var videoTrackLocal: VideoTrack
    lateinit var surfaceViewRendererLocal: SurfaceViewRenderer
    private val peerConnectionAdapter = PeerConnectionAdapter()
    private val sdpObserverAdapter = SDPObserverAdapter()
    lateinit var mensaje : JSONObject

    val TAG = "StreamLocal"

    ///Metodos publicos
    fun destruir() {
        try {
            peerConnectionLocal.removeStream(mediaStreamLocal)
            peerConnectionLocal.dispose()
            peerConnectionLocal.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun iniciarVideollamada() {
        this.surfaceViewRendererLocal = videoLocal

        /// VideoLocal
        textureHelperLocal = SurfaceTextureHelper.create(ConstantesVideollamada.nombreHiloTextureHelperLocal, eglBaseContext)
        val videoCapturerLocal = createCameraCapturer(true)
        videoSourceLocal = peerConnectionFactory.createVideoSource(videoCapturerLocal!!.isScreencast)
        videoCapturerLocal.initialize(textureHelperLocal, activity.applicationContext, videoSourceLocal.capturerObserver)
        videoCapturerLocal.startCapture(ConstantesVideollamada.ANCHO, ConstantesVideollamada.ALTO, ConstantesVideollamada.FPS)
        videoLocal.setMirror(true)
        videoLocal.init(eglBaseContext,null)
        videoTrackLocal = peerConnectionFactory.createVideoTrack(ConstantesVideollamada.IDVIDEOTRACKLOCAL, videoSourceLocal)
        ///Muestra en pantalla la camara local
        videoTrackLocal.addSink(surfaceViewRendererLocal)

        mediaStreamLocal = peerConnectionFactory.createLocalMediaStream(ConstantesVideollamada.IDMEDIASTREAMLOCAL)
        mediaStreamLocal.addTrack(videoTrackLocal)

        call()
    }

    fun traerMensaje() = mensaje

    ///Metodos privados
    private fun call() {

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

    private fun accionOnAddStream(objeto: Any?) {
        if (objeto !is MediaStream) {
            return
        }
    }

    private fun accionOnIceCandidate(objeto: Any?) {

        if (objeto !is IceCandidate) {
            return
        }

        mensaje = JSONObject()
        mensaje.put(ConstantesVideollamada.type, ConstantesVideollamada.candidate)
        mensaje.put(ConstantesVideollamada.label, objeto.sdpMLineIndex)
        mensaje.put(ConstantesVideollamada.id, objeto.sdpMid)
        mensaje.put(ConstantesVideollamada.candidate, objeto.sdp)

        escuchadorEnviarASocket?.invoke(mensaje)
    }


}