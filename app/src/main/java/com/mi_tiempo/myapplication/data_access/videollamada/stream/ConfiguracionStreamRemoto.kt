package com.mi_tiempo.myapplication.data_access.videollamada.stream

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.data_access.videollamada.ConstantesVideollamada
import com.mi_tiempo.myapplication.data_access.videollamada.PeerConnectionAdapter
import com.mi_tiempo.myapplication.data_access.videollamada.SDPObserverAdapter
import org.json.JSONObject
import org.webrtc.*

class ConfiguracionStreamRemoto(
        activity: AppCompatActivity,
        eglBaseContext: EglBase.Context,
        escuchadorEnviarASocket : ((JSONObject?)->Unit)?,
        peerConnectionFactory: PeerConnectionFactory,
        val videoRemoto: SurfaceViewRenderer
) : BaseStream( activity, eglBaseContext, escuchadorEnviarASocket , peerConnectionFactory, videoRemoto){

    val TAG = "StreamRemoto"
    lateinit var peerConnectionRemoto: PeerConnection
    lateinit var mediaStreamRemoto: MediaStream
    lateinit var textureHelperRemoto : SurfaceTextureHelper
    lateinit var videoSourceRemoto : VideoSource
    lateinit var videoTrackRemoto: VideoTrack
    lateinit var audioTrackRemoto: AudioTrack
    lateinit var surfaceViewRendererRemoto: SurfaceViewRenderer
    private val peerConnectionAdapter = PeerConnectionAdapter()
    private val sdpObserverAdapter = SDPObserverAdapter()
    private var mensaje: JSONObject? = null

    fun destruir() {
        try {
            peerConnectionRemoto.removeStream(mediaStreamRemoto)
            peerConnectionRemoto.dispose()
            peerConnectionRemoto.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun iniciarVideollamada() {
        this.surfaceViewRendererRemoto = this.videoRemoto

        //videoRemoto
        textureHelperRemoto = SurfaceTextureHelper.create(ConstantesVideollamada.nombreHiloTextureHelperRemoto, eglBaseContext)
        val videoCapturerRemoto = createCameraCapturer(false)
        videoSourceRemoto = peerConnectionFactory.createVideoSource(videoCapturerRemoto!!.isScreencast)
        videoCapturerRemoto.initialize(textureHelperRemoto, activity.applicationContext, videoSourceRemoto.capturerObserver)
        videoCapturerRemoto.startCapture(ConstantesVideollamada.ANCHO, ConstantesVideollamada.ALTO, ConstantesVideollamada.FPS)
        videoRemoto.setMirror(false)
        videoRemoto.init(eglBaseContext,null)
        videoTrackRemoto = peerConnectionFactory.createVideoTrack(ConstantesVideollamada.IDVIDEOTRACKREMOTO, videoSourceRemoto)
        ///Muestra en pantalla la camara local
        //videoTrackRemoto.addSink(surfaceViewRendererRemoto)

        mediaStreamRemoto = peerConnectionFactory.createLocalMediaStream(ConstantesVideollamada.IDMEDIASTREAMREMOTO)
        mediaStreamRemoto.addTrack(videoTrackRemoto)

        inicializarPeerconnection()

    }

    fun recibirOferta(jsonObject: JSONObject) {
        if (jsonObject.getString(ConstantesVideollamada.type) == ConstantesVideollamada.candidate) {
            manejarCandidato(jsonObject)
            return
        }
    }

    fun traerMensaje() = mensaje

    ///Metodos privados

    private fun inicializarPeerconnection() {
        peerConnectionRemoto = peerConnectionFactory.createPeerConnection(
            iceServers,
            peerConnectionAdapter.conEscuchador {
                respuesta, any, any2 ->
                Log.e(TAG, "llego $respuesta")
            })!!

        peerConnectionRemoto.createOffer(
                sdpObserverAdapter.conEscuchador { respuestas, any ->
                    Log.e(TAG, "llego $respuestas")
                },
                MediaConstraints()
        )
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

    private fun manejarCandidato(jsonObject: JSONObject) {
        val candidate = IceCandidate(
                jsonObject.getString(ConstantesVideollamada.id),
                jsonObject.getInt(ConstantesVideollamada.label),
                jsonObject.getString(ConstantesVideollamada.candidate)
        )

        peerConnectionRemoto.addIceCandidate(candidate)
    }

}
