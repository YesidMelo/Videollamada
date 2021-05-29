package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.base.App
import org.json.JSONObject
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject

class InteraccionEntreWebRTCYSocketVideollamada {

    private val TAG = "InteraccionEntreWebRTCYSocketVideollamada"
    @Inject lateinit var logicaWebRTC : LogicaWebRTC
    @Inject lateinit var logicaSocketVideollamada : LogicaSocketVideollamada
    private var nombreSala: String? = null

    init {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
    }

    ///Metodos publicos
    /// Metodos relacionados con la vinculacion con el servidor
    fun destruirVideollamadaWebRTC() {
        logicaWebRTC.destruirVideollamada()
    }

    fun desvincularSocketVideollamada() {
        logicaSocketVideollamada.finalizarConexion()
    }

    fun iniciarVideollamadaWebRTC(
        activity: AppCompatActivity,
        renderLocal: SurfaceViewRenderer,
        renderRemoto: SurfaceViewRenderer
    ) {
        logicaWebRTC.iniciarVideollamada(activity, renderLocal, renderRemoto)
        logicaWebRTC.conEscuchadorEnviarASocket {
            objeto->
            Log.e(TAG, "objeto: ${objeto?.toString()}")
        }
    }

    fun vincularSocketVideollamada(usuarioActual: String) {
        logicaSocketVideollamada.conUsuarioActual(usuarioActual)
        logicaSocketVideollamada.conEscuchadorConexion{
            canales, any ->
            Log.e(TAG, "se ha conectado $canales")
        }
        logicaSocketVideollamada.registrarConexion()
    }

    //Metodos relacionados con la negociacion

    fun unirmeASala(
        nombreSala: String,
        nombreReceptor: String
    ) {
        this.nombreSala = nombreSala
        logicaSocketVideollamada.unirmeASala(nombreSala, nombreReceptor){
            val json : JSONObject = logicaWebRTC.traerMensajeLocalAEnviar()
            logicaSocketVideollamada.enviarOferta(
                    nombreSala= nombreSala,
                    nombreReceptor= nombreReceptor,
                    jsonObject = json,
                    recibirOferta = logicaWebRTC::recibirOferta
            )
        }
    }

    fun salirDeSala(nombreSala: String) {
        logicaSocketVideollamada.salirDeSala(nombreSala){
            Log.e(TAG, "Ha salido de la sala $nombreSala");
        }
    }
}