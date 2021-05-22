package com.mi_tiempo.myapplication.uses_cases.videollamada

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearVideoLocal
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class InicializarVideoLocalCasoUso {

    @Inject lateinit var crearVideoLocal : CrearVideoLocal

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(activity: AppCompatActivity, surfaceViewRenderer: SurfaceViewRenderer){
        crearVideoLocal.inicializarVideoLocal(activity, surfaceViewRenderer)
    }
}