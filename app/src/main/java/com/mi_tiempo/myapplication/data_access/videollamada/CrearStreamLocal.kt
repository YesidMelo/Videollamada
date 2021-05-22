package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.*
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrearStreamLocal {

    val TAG = "CrearStreamLocal"
    private var mediaStreamLocal : MediaStream? = null

    @Inject lateinit var destruirAudioLocalCasoUso: DestruirAudioLocalCasoUso
    @Inject lateinit var destruirVideoRemotoCasoUso: DestruirVideoRemotoCasoUso
    @Inject lateinit var inicializarVideoLocalCasoUso: InicializarVideoLocalCasoUso
    @Inject lateinit var inicializarAudioLocalCasoUso: InicializarAudioLocalCasoUso
    @Inject lateinit var traerAudioTrackLocalCasoUso: TraerAudioTrackLocalCasoUso
    @Inject lateinit var traerVideoTrackLocalCasoUso: TraerVideoTrackLocalCasoUso
    @Inject lateinit var estaticosVideollamada: EstaticosVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun crear(
        activity: AppCompatActivity,
        surfaceViewRenderer: SurfaceViewRenderer
    ) {
        estaticosVideollamada.inicializarComponentes(activity)

        if (estaticosVideollamada.traerPeerConnectionFactory() == null ){
            Log.e(TAG, "peerconnection factory es nulo")
            return
        }

        inicializarAudioLocalCasoUso.invoke(activity)
        inicializarVideoLocalCasoUso.invoke(activity, surfaceViewRenderer)
        mediaStreamLocal = estaticosVideollamada.traerPeerConnectionFactory()!!.createLocalMediaStream(TAG)
        mediaStreamLocal!!.addTrack(traerAudioTrackLocalCasoUso.invoke())
        mediaStreamLocal!!.addTrack(traerVideoTrackLocalCasoUso.invoke())
    }

    fun destruir() {
        destruirAudioLocalCasoUso.invoke()
        destruirVideoRemotoCasoUso.invoke()
        mediaStreamLocal?.dispose()
        mediaStreamLocal = null
    }

    fun traerMediaStreamLocal() = mediaStreamLocal




}