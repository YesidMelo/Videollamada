package com.mi_tiempo.myapplication.uses_cases.videollamada

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoRemoto
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class InicializarVideoRemotoCasoUso {

    @Inject lateinit var crearVideoRemoto : CrearVideoRemoto

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(
        activity: AppCompatActivity,
        surfaceViewRenderer: SurfaceViewRenderer
    ){
        crearVideoRemoto.inicializarVideoRemoto(activity, surfaceViewRenderer)
    }

}