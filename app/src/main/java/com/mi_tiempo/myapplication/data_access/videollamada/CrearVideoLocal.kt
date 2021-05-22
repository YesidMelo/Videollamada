package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.webrtc.*
import javax.inject.Inject
import javax.inject.Singleton

///Codigo basado en https://programmerclick.com/article/13101578132/
@Singleton
class CrearVideoLocal {

    val TAG = "CrearVideoLocal"
    private val ANCHO = 480
    private val ALTO = 640
    private val FPS = 60
    private val VIDEOTRACK_ID = "101"

    @Inject lateinit var estaticosVideollamada: EstaticosVideollamada
    private var surfaceTextureHelper: SurfaceTextureHelper? = null
    private var videoTrack: VideoTrack? = null

    init{
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        videoTrack?.dispose()
        videoTrack = null
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        surfaceViewRenderer: SurfaceViewRenderer
    ) {
        estaticosVideollamada.inicializarComponentes(activity)
        inicializarEspejoLocal(activity, surfaceViewRenderer)
    }

    fun traerVideoTrack() = videoTrack

    ///Metodos privados
    private fun inicializarEspejoLocal(activity: AppCompatActivity, surfaceViewRenderer: SurfaceViewRenderer) {
        val rootEglBase = estaticosVideollamada.traerRootEglBaseContext()
        if (rootEglBase == null) {
            Log.e(TAG,"rootEGl es nulo")
            return
        }

        surfaceTextureHelper = SurfaceTextureHelper.create(this.javaClass.name, estaticosVideollamada.traerRootEglBaseContext()!!)

        val videoCapturer = crearCameraCapturer()
        val videoSource = estaticosVideollamada.traerPeerConnectionFactory()!!.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer.initialize(surfaceTextureHelper, activity.applicationContext, videoSource.capturerObserver)
        videoCapturer.startCapture(ANCHO, ALTO, FPS)

        surfaceViewRenderer.setMirror(true)
        surfaceViewRenderer.init(estaticosVideollamada.traerRootEglBaseContext(), null)
        videoTrack = estaticosVideollamada.traerPeerConnectionFactory()!!.createVideoTrack(VIDEOTRACK_ID, videoSource)
        videoTrack!!.addSink(surfaceViewRenderer)

    }

    private fun crearCameraCapturer() : VideoCapturer? {
        val enumerator : Camera1Enumerator = Camera1Enumerator(false)
        val nombreDispositivos = enumerator.deviceNames


        for(nombreDispositivo in nombreDispositivos) {
            if (enumerator.isFrontFacing(nombreDispositivo)){
                val videoCapturer = enumerator.createCapturer(nombreDispositivo, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        for (nombreDispositivo in nombreDispositivos) {
            if (!enumerator.isFrontFacing(nombreDispositivo)) {
                val videoCapturer = enumerator.createCapturer(nombreDispositivo, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null
    }

}