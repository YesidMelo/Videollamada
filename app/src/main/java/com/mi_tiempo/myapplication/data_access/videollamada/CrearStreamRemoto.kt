package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.DestruirVideoRemotoCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.InicializarVideoRemotoCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.TraerVideoTrackRemotoCasoUso
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class CrearStreamRemoto {

    val TAG = "CrearStreamRemoto"
    @Inject lateinit var destruirVideoRemotoCasoUso: DestruirVideoRemotoCasoUso
    @Inject lateinit var inicializarVideoRemotoCasoUso: InicializarVideoRemotoCasoUso
    @Inject lateinit var traerVideoTrackRemotoCasoUso : TraerVideoTrackRemotoCasoUso
    @Inject lateinit var estaticosVideollamada: EstaticosVideollamada

    private var mediaStream : MediaStream? = null

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        destruirVideoRemotoCasoUso.invoke()
        mediaStream?.dispose()
        mediaStream = null
    }

    fun crear(activity: AppCompatActivity , surfaceViewRenderer: SurfaceViewRenderer) {
        estaticosVideollamada.inicializarComponentes(activity)
        if (estaticosVideollamada.traerPeerConnectionFactory() == null) {
            Log.e(TAG, "el peerConnectionFactory es nulo")
            return
        }

        inicializarVideoRemotoCasoUso.invoke(activity, surfaceViewRenderer)
        mediaStream = estaticosVideollamada.traerPeerConnectionFactory()!!.createLocalMediaStream(TAG)
        mediaStream!!.addTrack(traerVideoTrackRemotoCasoUso.invoke())
    }

    fun traerMediaStream() = mediaStream

}