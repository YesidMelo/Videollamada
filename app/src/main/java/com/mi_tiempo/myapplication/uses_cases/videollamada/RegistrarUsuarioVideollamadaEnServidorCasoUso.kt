package com.mi_tiempo.myapplication.uses_cases.videollamada

import android.util.Log
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.data_access.videollamada.LogicaSocketVideollamada
import javax.inject.Inject

class RegistrarUsuarioVideollamadaEnServidorCasoUso {

    private val TAG = "RegistrarUsuarioVideollamadaCU"
    @Inject lateinit var logicaSocketVideollamada : LogicaSocketVideollamada

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    fun invoke(usuarioActual: String) {
        logicaSocketVideollamada.conUsuarioActual(usuarioActual)
        logicaSocketVideollamada.conEscuchadorConexion{
            canales, any ->
            Log.e(TAG, "se ha conectado $canales")
        }
        logicaSocketVideollamada.registrarConexion()
    }

}