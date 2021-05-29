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
    private var escuchadorNegociacion: ((CanalesNegociacion, Any?)->Unit)? = null
    private var escuchadorUnidoASala : (()-> Unit)? = null
    private var escuchadorUnidoSalirSala : (()-> Unit)? = null
    private val labelUsuarioActual = "usuario"
    private val labelUsuarioEmisor = "emisor"
    private val labelUsuarioReceptor = "receptor"
    private val labelSala = "Sala"
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

    fun conEscuchadorNegociacion(escuchadorNegociacion : ((CanalesNegociacion, Any?)->Unit)?) {
        this.escuchadorNegociacion = escuchadorNegociacion
    }

    fun conUsuarioActual(usuarioActual : String?) {
        this.usuarioActual = usuarioActual
    }

    //Canales conexion

    fun finalizarConexion() {
        socket.emit(CanalesConexion.finalizarRegistroUsuario.traerNombre(), JSONObject().apply { put(labelUsuarioActual, usuarioActual) })
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
            json.put(labelUsuarioActual, usuarioActual)
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
        json.put(labelUsuarioEmisor, usuarioActual)
        json.put(labelUsuarioReceptor, nombreReceptor)
        json.put(labelSala, nombreSala)
        socket.emit(CanalesNegociacion.unirASala.traerNombre(), json)

    }

    fun salirDeSala(
        nombreSala: String,
        escuchadorUnidoSalirSala: ()->Unit
    ) {
        this.escuchadorUnidoSalirSala = escuchadorUnidoSalirSala
        val json = JSONObject()
        json.put(labelSala, nombreSala)
        socket.emit(CanalesNegociacion.salirDeSala.traerNombre(), json)
    }

    /// Metodos privados

    //CanalesConexion
    private fun adicionarCanales() {
        adicionarCanalRegistroUsuario()
        adicionarCanalSalirSala()
        adicionarCanalUnirseASala()
    }

    private fun adicionarCanalRegistroUsuario() {
        socket.on(CanalesConexion.registrarUsuario.traerNombre()) {
            if (it.isEmpty()) { return@on }
            val objeto = it.first() as JSONObject
            socketId = objeto.getString("socketId")
            Log.e(TAG, "Usuario registrado")
        }
    }

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
        ;

        fun traerNombre() = nombre
    }
}