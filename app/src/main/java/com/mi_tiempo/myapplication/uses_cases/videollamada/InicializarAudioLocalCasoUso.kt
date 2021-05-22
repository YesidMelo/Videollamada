package com.mi_tiempo.myapplication.uses_cases.videollamada

import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.CrearAudioLocal
import javax.inject.Inject

class InicializarAudioLocalCasoUso {

    @Inject lateinit var crearAudioLocal: CrearAudioLocal

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(activity: AppCompatActivity) {
        crearAudioLocal.inicializarAudioTack(activity)
    }
}