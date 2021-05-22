package com.mi_tiempo.myapplication.ui.stream_fragment

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.*
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    @Inject lateinit var crearStreamLocalCasoUso: CrearStreamLocalCasoUso
    @Inject lateinit var destruirStreamLocalCasoUso: DestruirStreamLocalCasoUso
    @Inject lateinit var destruirVideoRemotoCasoUso: DestruirVideoRemotoCasoUso
    @Inject lateinit var inicializarVideoRemotoCasoUso: InicializarVideoRemotoCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        destruirVideoRemotoCasoUso.invoke()
        destruirStreamLocalCasoUso.invoke()
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        crearStreamLocalCasoUso.invoke(activity, renderLocal)
        inicializarVideoRemotoCasoUso.invoke(activity, renderRemoto)
    }
}