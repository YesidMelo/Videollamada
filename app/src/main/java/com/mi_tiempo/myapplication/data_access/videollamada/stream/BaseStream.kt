package com.mi_tiempo.myapplication.data_access.videollamada.stream

import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import org.webrtc.*

open class BaseStream(
    val activity: AppCompatActivity,
    val eglBaseContext: EglBase.Context,
    val escuchadorEnviarASocket : ((JSONObject?)->Unit)? = null,
    val peerConnectionFactory: PeerConnectionFactory,
    val videoLocal: SurfaceViewRenderer
) {

    val iceServers = arrayOf<PeerConnection.IceServer>(

            PeerConnection.IceServer("stun:stun.l.google.com:19302"),
            /*
                PeerConnection.IceServer("stun:stun1.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun2.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun3.l.google.com:19302"),
                PeerConnection.IceServer("stun:stun4.l.google.com:19302")
            */
    ).toMutableList()

    fun createCameraCapturer(isFront: Boolean) : VideoCapturer?{
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
}