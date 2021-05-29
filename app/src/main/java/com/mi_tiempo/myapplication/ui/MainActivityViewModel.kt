package com.mi_tiempo.myapplication.ui

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.DesvincularDelSocketDeVideollamadaCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.VincularUsuarioVideollamadaAServidorCasoUso
import javax.inject.Inject

class MainActivityViewModel {

    @Inject lateinit var desvincularDelSocketDeVideollamadaCasoUso: DesvincularDelSocketDeVideollamadaCasoUso
    @Inject lateinit var vincularUsuarioVideollamadaAServidorCasoUso: VincularUsuarioVideollamadaAServidorCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun finalizarConexion() {
        desvincularDelSocketDeVideollamadaCasoUso.invoke()
    }

    fun registrarUsuario(usuarioActual: String) {
        vincularUsuarioVideollamadaAServidorCasoUso.invoke(usuarioActual)
    }

}