package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class LogicaSocketVideollamada {

    private val TAG = "LogicaSocketVideollamada"
    private val URL_VIDEOLLAMADA = "http://192.168.1.3:3000/stream"

    private var escuchadorConexion : ((CanalesConexion, Any?)->Unit)? = null
    private var escuchadorUnidoASala : (()-> Unit)? = null
    private var escuchadorUnidoSalirSala : (()-> Unit)? = null
    private var recibirOferta : ((JSONObject)->Unit)? = null
    private var socket: Socket = IO.socket(URL_VIDEOLLAMADA)
    private var socketId: String? = null
    private var usuarioActual : String? = null

    init {
        adicionarCanales()
    }

    ///MEtodos publicos
    //Configuracion clase
    fun conEscuchadorConexion(escuchadorConexion : ((CanalesConexion, Any?)->Unit)?) {
        this.escuchadorConexion = escuchadorConexion
    }

    fun conUsuarioActual(usuarioActual : String?) {
        this.usuarioActual = usuarioActual
    }

    //Canales conexion

    fun finalizarConexion() {
        socket.emit(CanalesConexion.finalizarRegistroUsuario.traerNombre(), JSONObject().apply { put(ConstantesVideollamada.labelUsuarioActual, usuarioActual) })
        socket.on(CanalesConexion.finalizarRegistroUsuario.traerNombre()) {
            socket.disconnect()
            socketId = null
        }
    }

    fun registrarConexion() {
       Log.e(TAG,"registrar conexion")
        socket.connect()
        socket.on(CanalesConexion.conexionEstablecida.traerNombre()) {
            val json = JSONObject()
            json.put(ConstantesVideollamada.labelUsuarioActual, usuarioActual)
            socket.emit(CanalesConexion.registrarUsuario.traerNombre(), json)
            escuchadorConexion?.invoke(CanalesConexion.registrarUsuario, it)
        }
    }

    //Canales negociacion webrtc
    fun unirmeASala(
        nombreSala: String,
        nombreReceptor: String,
        escuchadorUnidoASala : ()-> Unit
    ) {
        this.escuchadorUnidoASala = escuchadorUnidoASala
        val json = JSONObject()
        json.put(ConstantesVideollamada.labelUsuarioEmisor, usuarioActual)
        json.put(ConstantesVideollamada.labelUsuarioReceptor, nombreReceptor)
        json.put(ConstantesVideollamada.labelSala, nombreSala)
        socket.emit(CanalesNegociacion.unirASala.traerNombre(), json)

    }

    fun salirDeSala(
        nombreSala: String,
        escuchadorUnidoSalirSala: ()->Unit
    ) {
        this.escuchadorUnidoSalirSala = escuchadorUnidoSalirSala
        val json = JSONObject()
        json.put(ConstantesVideollamada.labelSala, nombreSala)
        json.put(ConstantesVideollamada.labelUsuarioEmisor, usuarioActual)
        socket.emit(CanalesNegociacion.salirDeSala.traerNombre(), json)
    }

    fun enviarOferta(
            nombreSala: String,
            nombreReceptor: String,
            jsonObject: JSONObject,
            recibirOferta: (JSONObject) ->Unit
    ) {
        this.recibirOferta = recibirOferta
        jsonObject.put(ConstantesVideollamada.labelUsuarioEmisor, usuarioActual)
        jsonObject.put(ConstantesVideollamada.labelSala, nombreSala)
        jsonObject.put(ConstantesVideollamada.labelUsuarioReceptor, nombreReceptor)
        socket.emit(CanalesNegociacion.enviarOfertaReceptor.traerNombre(),jsonObject)
    }

    /// Metodos privados

    //CanalesConexion
    private fun adicionarCanales() {
        adicionarCanalRegistroUsuario()
        adicionarCanalSalirSala()
        adicionarCanalUnirseASala()
        adicionarCanalRecibirOferta()
    }

    private fun adicionarCanalRegistroUsuario() {
        socket.on(CanalesConexion.registrarUsuario.traerNombre()) {
            if (it.isEmpty()) { return@on }
            val objeto = it.first() as JSONObject
            socketId = objeto.getString("socketId")
            Log.e(TAG, "Usuario registrado")
        }
    }

    //Canales sala
    private fun adicionarCanalUnirseASala() {
        socket.on(CanalesNegociacion.unirASala.traerNombre()) {
            if (it.isEmpty()) { return@on }
            if (it.first() !is JSONObject) { return@on }
            escuchadorUnidoASala?.invoke()
        }
    }

    private fun adicionarCanalSalirSala() {
        socket.on(CanalesNegociacion.salirDeSala.traerNombre()) {
            if (it.isEmpty()) { return@on }
            if (it.first() !is JSONObject) { return@on }
            escuchadorUnidoSalirSala?.invoke()
        }
    }

    // Canales oferta
    private fun adicionarCanalRecibirOferta() {
        socket.on(CanalesNegociacion.enviarOfertaReceptor.traerNombre()){
            if (it.isEmpty()) { return@on }
            if (it.first() !is JSONObject) { return@on }
            recibirOferta?.invoke(it.first() as JSONObject)
            Log.e(TAG, "Ha llegado a recibir oferta")
        }
    }




    ///Enumeradores

    enum class CanalesConexion(private val nombre: String) {
        ///registro en servidor
        conexionEstablecida("conexionEstablecida"),
        finalizarRegistroUsuario("finalizarRegistroUsuario"),
        registrarUsuario("registrarUsuario"),
        ;
        fun traerNombre() = nombre
    }

    enum class CanalesNegociacion(private val nombre: String) {
        ///negociacion webrtc
        unirASala("unirASala"),
        salirDeSala("salirDeSala"),
        enviarOfertaReceptor("enviarOfertaReceptor"),
        ;

        fun traerNombre() = nombre
    }
}