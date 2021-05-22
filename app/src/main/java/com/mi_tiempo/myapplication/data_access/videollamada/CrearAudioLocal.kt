package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.webrtc.AudioTrack
import org.webrtc.MediaConstraints
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrearAudioLocal {

    private val TAG = "CrearAudioLocal"
    private val AUDIOTRACK_ID = "101"

    @Inject lateinit var estaticosVideollamada: EstaticosVideollamada

    private var audioTrack : AudioTrack? = null

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        audioTrack?.dispose()
        audioTrack = null
    }

    fun inicializarAudioTack(activity: AppCompatActivity) {
        estaticosVideollamada.inicializarComponentes(activity)
        if (estaticosVideollamada.traerPeerConnectionFactory() == null) {
            Log.e(TAG, "perconnection factory es nulo")
            return
        }
        val audioSource = estaticosVideollamada.traerPeerConnectionFactory()!!.createAudioSource(MediaConstraints())
        audioTrack = estaticosVideollamada.traerPeerConnectionFactory()!!.createAudioTrack(AUDIOTRACK_ID, audioSource)

    }

    fun traerAudioTrack() = audioTrack
}