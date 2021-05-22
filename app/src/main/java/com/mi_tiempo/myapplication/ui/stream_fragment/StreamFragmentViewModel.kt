package com.mi_tiempo.myapplication.ui.stream_fragment

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.InicializarVideoLocalCasoUso
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    @Inject lateinit var inicializarVideoLocalCasoUso: InicializarVideoLocalCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal:SurfaceViewRenderer
    ) {
        inicializarVideoLocalCasoUso.invoke(activity, renderLocal)
    }
}