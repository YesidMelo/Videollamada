package com.mi_tiempo.myapplication.ui.stream_fragment

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.*
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class StreamFragmentViewModel {

    @Inject lateinit var destruirAudioLocalCasoUso: DestruirAudioLocalCasoUso
    @Inject lateinit var destruirVideoLocalCasoUso: DestruirVideoLocalCasoUso
    @Inject lateinit var destruirVideoRemotoCasoUso: DestruirVideoRemotoCasoUso
    @Inject lateinit var inicializarVideoLocalCasoUso: InicializarVideoLocalCasoUso
    @Inject lateinit var inicializarAudioLocalCasoUso: InicializarAudioLocalCasoUso
    @Inject lateinit var inicializarVideoRemotoCasoUso: InicializarVideoRemotoCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun destruir() {
        destruirAudioLocalCasoUso.invoke()
        destruirVideoLocalCasoUso.invoke()
        destruirVideoRemotoCasoUso.invoke()
    }

    fun inicializarVideoLocal(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        inicializarVideoLocalCasoUso.invoke(activity, renderLocal)
        inicializarAudioLocalCasoUso.invoke(activity)
        inicializarVideoRemotoCasoUso.invoke(activity, renderRemoto)
    }
}