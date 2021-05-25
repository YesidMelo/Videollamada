package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import org.webrtc.*

class PeerConnectionAdapter : PeerConnection.Observer {

    val TAG = "PeerConnectionAdapter"
    private var escuchador: ((Respuesta,Any?, Any?)->Unit)? = null

    fun conEscuchador(escuchador: ((Respuesta,Any?, Any?)->Unit)?) : PeerConnectionAdapter{
        this.escuchador = escuchador
        return this
    }

    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
        Log.e(TAG, "onSignalingChange")
        escuchador?.invoke(Respuesta.onSignalingChange, p0, null)
    }

    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
        Log.e(TAG, "onIceConnectionChange")
        escuchador?.invoke(Respuesta.onIceConnectionChange, p0, null)
    }

    override fun onIceConnectionReceivingChange(p0: Boolean) {
        Log.e(TAG, "onIceConnectionReceivingChange")
        escuchador?.invoke(Respuesta.onIceConnectionReceivingChange, p0, null)
    }

    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
        Log.e(TAG, "onIceGatheringChange")
        escuchador?.invoke(Respuesta.onIceGatheringChange, p0, null)
    }

    override fun onIceCandidate(p0: IceCandidate?) {
        Log.e(TAG, "onIceCandidate")
        escuchador?.invoke(Respuesta.onIceCandidate, p0, null)
    }

    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
        Log.e(TAG, "onIceCandidatesRemoved")
        escuchador?.invoke(Respuesta.onIceCandidatesRemoved, p0, null)
    }

    override fun onAddStream(p0: MediaStream?) {
        Log.e(TAG, "onAddStream")
        escuchador?.invoke(Respuesta.onAddStream, p0, null)
    }

    override fun onRemoveStream(p0: MediaStream?) {
        Log.e(TAG, "onRemoveStream")
        escuchador?.invoke(Respuesta.onRemoveStream, p0, null)
    }

    override fun onDataChannel(p0: DataChannel?) {
        Log.e(TAG, "onDataChannel")
        escuchador?.invoke(Respuesta.onDataChannel, p0, null)
    }

    override fun onRenegotiationNeeded() {
        Log.e(TAG, "onRenegotiationNeeded")
        escuchador?.invoke(Respuesta.onRenegotiationNeeded, null, null)
    }

    override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
        Log.e(TAG, "onAddTrack")
        escuchador?.invoke(Respuesta.onAddTrack, p0, p1)
    }


    enum class Respuesta {
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
    }

}