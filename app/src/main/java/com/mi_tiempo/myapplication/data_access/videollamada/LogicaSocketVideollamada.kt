package com.mi_tiempo.myapplication.data_access.videollamada

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class LogicaSocketVideollamada {

    private val TAG = "LogicaSocketVideollamada"
    private val URL_VIDEOLLAMADA = "http://192.168.1.10:3000/stream"

    private var escuchadorConexion : ((Canales, Any?)->Unit)? = null
    private val labelUsuarioActual = "usuario"
    private var socket: Socket = IO.socket(URL_VIDEOLLAMADA)
    private var socketId: String? = null
    private var usuarioActual : String? = null

    init {
        adicionarCanales()
    }

    ///MEtodos publicos

    fun conEscuchadorConexion(escuchadorConexion : ((Canales, Any?)->Unit)?) {
        this.escuchadorConexion = escuchadorConexion
    }

    fun conUsuarioActual(usuarioActual : String?) {
        this.usuarioActual = usuarioActual
    }

    fun finalizarConexion() {
        socket.emit(Canales.finalizarRegistroUsuario.traerNombre(), JSONObject().apply { put(labelUsuarioActual, usuarioActual) })
        socket.on(Canales.finalizarRegistroUsuario.traerNombre()) {
            socket.disconnect()
            socketId = null
        }
    }

    fun registrarConexion() {
       Log.e(TAG,"registrar conexion")
        socket.connect()
        socket.on(Canales.conexionEstablecida.traerNombre()) {
            val json = JSONObject()
            json.put(labelUsuarioActual, usuarioActual)
            socket.emit(Canales.registrarUsuario.traerNombre(), json)
            escuchadorConexion?.invoke(Canales.registrarUsuario, it)
        }
    }

    /// Metodos privados

    private fun adicionarCanales() {
        adicionarCanalRegistroUsuario()
    }

    private fun adicionarCanalRegistroUsuario() {
        socket.on(Canales.registrarUsuario.traerNombre()) {
            if (it.isEmpty()) { return@on }
            val objeto = it.first() as JSONObject
            socketId = objeto.getString("socketId")
            Log.e(TAG, "Usuario registrado")
        }
    }

    ///Enumeradores

    enum class Canales(private val nombre: String) {
        conexionEstablecida("conexionEstablecida"),
        finalizarRegistroUsuario("finalizarRegistroUsuario"),
        registrarUsuario("registrarUsuario"),
        ;

        fun traerNombre() = nombre
    }
}