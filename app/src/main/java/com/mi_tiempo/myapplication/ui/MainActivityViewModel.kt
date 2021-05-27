package com.mi_tiempo.myapplication.ui

import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.uses_cases.videollamada.DesvincularDelSocketDeVideollamadaCasoUso
import com.mi_tiempo.myapplication.uses_cases.videollamada.RegistrarUsuarioVideollamadaEnServidorCasoUso
import javax.inject.Inject

class MainActivityViewModel {

    @Inject lateinit var desvincularDelSocketDeVideollamadaCasoUso: DesvincularDelSocketDeVideollamadaCasoUso
    @Inject lateinit var registrarUsuarioVideollamadaEnServidorCasoUso: RegistrarUsuarioVideollamadaEnServidorCasoUso

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun finalizarConexion() {
        desvincularDelSocketDeVideollamadaCasoUso.invoke()
    }

    fun registrarUsuario(usuarioActual: String) {
        registrarUsuarioVideollamadaEnServidorCasoUso.invoke(usuarioActual)
    }

}