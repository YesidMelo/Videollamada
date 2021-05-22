package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.webrtc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrearVideoRemoto {

    val TAG = "CrearVideoRemoto"
    val VIDEOTRACK_ID= "102"
    private var videoTrack: VideoTrack? = null
    @Inject lateinit var estaticosVideollamada: EstaticosVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    ///Metodos publico

    fun destruir() {
        videoTrack?.dispose()
        videoTrack = null
    }

    fun inicializarVideoRemoto(
        activity: AppCompatActivity,
        surfaceViewRenderer: SurfaceViewRenderer
    ) {
        estaticosVideollamada.inicializarComponentes(activity)
        if (estaticosVideollamada.traerPeerConnectionFactory() == null) {
            Log.e(TAG, "peerconnection factory es nulo")
            return
        }

        val remoteSurfaceTextureHelper = SurfaceTextureHelper.create(TAG, estaticosVideollamada.traerRootEglBaseContext())
        val remoteVideoCapturer = createCameraCapturer()

        val remoteVideoSource = estaticosVideollamada.traerPeerConnectionFactory()!!.createVideoSource(remoteVideoCapturer!!.isScreencast)
        remoteVideoCapturer.initialize(remoteSurfaceTextureHelper, activity.applicationContext, remoteVideoSource.capturerObserver)
        remoteVideoCapturer.startCapture(estaticosVideollamada.ANCHO, estaticosVideollamada.ALTO, estaticosVideollamada.FPS)

        surfaceViewRenderer.setMirror(false)
        surfaceViewRenderer.init(estaticosVideollamada.traerRootEglBaseContext(), null)
        videoTrack = estaticosVideollamada.traerPeerConnectionFactory()!!.createVideoTrack(VIDEOTRACK_ID, remoteVideoSource)

        //Muestra video remoto
        //videoTrack.addSink(surfaceViewRenderer)

    }

    fun traerVideoTrack() = videoTrack

    ///Metodos privados

    private fun createCameraCapturer() : VideoCapturer? {
        val enumerator = Camera1Enumerator(false)
        val nombresDispositivos = enumerator.deviceNames

        for (nombreDispositivo in nombresDispositivos) {
            if(enumerator.isBackFacing(nombreDispositivo)) {
                val videoCapturer = enumerator.createCapturer(nombreDispositivo,null)
                if (videoCapturer != null){
                    return videoCapturer
                }
            }
        }

        return null
    }
}