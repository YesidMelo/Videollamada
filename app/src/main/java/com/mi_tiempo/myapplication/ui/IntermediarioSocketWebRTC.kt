package com.mi_tiempo.myapplication.ui

import android.content.Context
import android.util.Log
import org.json.JSONObject
import org.webrtc.SurfaceViewRenderer

class IntermediarioSocketWebRTC(
    applicationContext: Context,
    videoLocal: SurfaceViewRenderer,
    videoRemoto: SurfaceViewRenderer,
    val sala :String,
    val emisor:String,
    val receptor:String,
    val receptorSalioVideollamada: ()->Unit
) {

    val TAG = "InterSocketWebRTC"
    private var soyCreadorDeSala = false
    private var estoyLeyendoElCanal = false
    private var haIniciado = false

    private val manejadorWebRTC: ManejadorWebRTC = ManejadorWebRTC(applicationContext,videoLocal,videoRemoto,::emitirAtravezDelSocket)

    fun iniciarVideollamada() {
        ManejadorSocket.traerInstancia()?.unirseASala(sala, emisor, receptor)
        ManejadorSocket.traerInstancia()?.conEscuchadorCanalesVideollamada(::recibirRespuestasDelSocket)
        manejadorWebRTC.iniciarllamada()
    }

    fun finalizarVideollamada() {
        ManejadorSocket.traerInstancia()?.salirSala(sala, emisor, receptor)
        manejadorWebRTC.finalizarLlamada()
    }

    ///MEtodos privados
    private fun emitirAtravezDelSocket(jsonObject: JSONObject) {
        jsonObject.put(MainActivity.EtiquetasJsonObject.emisor.traerNombre(), emisor)
        jsonObject.put(MainActivity.EtiquetasJsonObject.receptor.traerNombre(), receptor)
        jsonObject.put(MainActivity.EtiquetasJsonObject.sala.traerNombre(), sala)
        ManejadorSocket.traerInstancia()?.enviarMensaje(jsonObject)
    }

    private fun recibirRespuestasDelSocket(canal : ManejadorSocket.CanalesConexionSocket, objeto : Any?) {
        Log.e(TAG, "canal responde $canal")
        when(canal) {
            ManejadorSocket.CanalesConexionSocket.mensaje -> {
                manejarRespuestasDelCanalMensaje(objeto)
            }
            ManejadorSocket.CanalesConexionSocket.salaCreada -> {
                soyCreadorDeSala = true
                Log.e(TAG, "Se ha creado la sala")
            }
            ManejadorSocket.CanalesConexionSocket.salirDeSala -> {
                Log.e(TAG, "Salio de la sala")
            }
            ManejadorSocket.CanalesConexionSocket.salaLlena -> {
                Log.e(TAG,"Sala llena")
            }
            ManejadorSocket.CanalesConexionSocket.unirASala -> {
                estoyLeyendoElCanal = true
            }
            ManejadorSocket.CanalesConexionSocket.receptorSalioDeSala-> {
                receptorSalioVideollamada()
                Log.e(TAG, "El receptor salio")
            }
            else -> {
                Log.e(TAG, "canal responde $canal")
            }
        }
    }

    private fun manejarRespuestasDelCanalMensaje(objeto : Any?) {
        if(!esUnObjetoValido(objeto)) {
            return
        }
        val jsonRespuesta = (objeto as Array<*>).first() as JSONObject
        Log.e(TAG, "Es un ${jsonRespuesta.getString(MainActivity.EtiquetasJsonObject.type.traerNombre())}")
        when(jsonRespuesta.getString(MainActivity.EtiquetasJsonObject.type.traerNombre())) {
            MainActivity.EtiquetasJsonObject.got_user_media.traerNombre() -> iniciarllamada() //Primero en llegar
            MainActivity.EtiquetasJsonObject.candidate.traerNombre() -> manejarCandidatoRemoto(objeto) // segundo en llegar
            MainActivity.EtiquetasJsonObject.offer.traerNombre() -> manejarOferta(objeto) //tercero en llegar
            MainActivity.EtiquetasJsonObject.answer.traerNombre() -> manejarRespuesta(objeto) // cuarto en llegar
            else -> {
                Log.e(TAG, "alternativa -------TMP ----")
            }
        }
    }

    private fun esUnObjetoValido(objeto : Any?) : Boolean {
        if(objeto == null) {
            Log.e(TAG, "objeto es nulo")
            return false
        }

        if (objeto !is Array<*>) {
            Log.e(TAG, "El objeto no es de tipo Array")
            return false
        }

        if (objeto.isEmpty()) {
            Log.e(TAG, "El objeto esta vacio")
            return false
        }

        if(objeto.first() !is JSONObject) {
            Log.e(TAG, "El objeto no es un jsonObject")
            return false
        }
        return true
    }

    private fun iniciarllamada() {
        if (haIniciado && estoyLeyendoElCanal) {
            return
        }
        haIniciado = true
        if (!soyCreadorDeSala) {
            return
        }
        manejadorWebRTC.crearOfertaVideollamada()
    }

    private fun manejarCandidatoRemoto(objeto: Any?) {

        if(!haIniciado) { return }
        if (!esUnObjetoValido(objeto)){ return }
        val objetoJSON = (objeto as Array<*>).first()  as JSONObject
        manejadorWebRTC.adicionarCandidato(objetoJSON)
        Log.e(TAG, "Recibiendo candidatos")

    }

    private fun manejarOferta(objeto: Any?) {
        if(!esUnObjetoValido(objeto)) {
            return
        }
        if (!soyCreadorDeSala && !haIniciado) {
            manejadorWebRTC.crearOfertaVideollamada()
        }
        manejadorWebRTC.adicionarOferta((objeto as Array<*>).first() as JSONObject)
        Log.e(TAG, "Me ha llegado una oferta")
    }

    private fun manejarRespuesta(objeto: Any?) {
        if (!haIniciado) { return }
        manejadorWebRTC.adicionarRespuesta((objeto as Array<*>).first() as JSONObject)
        Log.e(TAG, "Me ha llegado una respuesta")
    }
}