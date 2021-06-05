package com.mi_tiempo.myapplication.ui

import android.util.Log
import io.socket.client.IO
import org.json.JSONObject

class ManejadorSocket private constructor(){
    val TAG = "ManejadorSocket"
    private val URL_VIDEOLLAMADA = "http://192.168.1.10:3000/stream"
    private val socket = IO.socket(URL_VIDEOLLAMADA)
    private var adicioneEscuchadoresDeCanales = false
    private var escuchadorCanalesVideollamada : ((CanalesConexionSocket, Any?)->Unit)? = null


    fun conEscuchadorCanalesVideollamada(escuchadorCanalesVideollamada : ((CanalesConexionSocket, Any?)->Unit)?) : ManejadorSocket {
        this.escuchadorCanalesVideollamada = escuchadorCanalesVideollamada
        return this
    }

    ///1. manejo de canales de conexion y desconexion al servidor
    fun registrarConexion() {
        socket.connect()
        socket.on(CanalesConexionSocket.conexionEstablecida.traerNombre()) {
            Log.e(TAG, "Se ha unido al servidor")
            if (adicioneEscuchadoresDeCanales) { return@on }
            adicioneEscuchadoresDeCanales = true
            adicionarEscuchadoresCanales()
        }

    }

    fun registrarUsuario(usuarioActual: String) {
        socket.emit(
            CanalesConexionSocket.registrarUsuario.traerNombre(),
            JSONObject().apply {
                put(MainActivity.EtiquetasJsonObject.usuario.traerNombre(), usuarioActual)
            }
        )
    }

    fun desconectarDelServidor(usuarioActual: String) {
        socket.emit(CanalesConexionSocket.finalizarRegistroUsuario.traerNombre(), JSONObject().apply {
            put(MainActivity.EtiquetasJsonObject.usuario.traerNombre(), usuarioActual)
        })
        socket.on(CanalesConexionSocket.finalizarRegistroUsuario.traerNombre()){
            socket.disconnect()
            socket.close()
            Log.e(TAG, "Se ha desconectado del servidor")
        }
    }

    //2. union y salida de la sala del usuario
    fun unirseASala(
            sala : String,
            emisor : String,
            receptor : String
    ) {
        socket.emit(CanalesConexionSocket.unirASala.traerNombre(), JSONObject().apply {
            put(MainActivity.EtiquetasJsonObject.sala.traerNombre(), sala)
            put(MainActivity.EtiquetasJsonObject.emisor.traerNombre(), emisor)
            put(MainActivity.EtiquetasJsonObject.receptor.traerNombre(), receptor)
        })
    }

    fun salirSala(sala  : String, usuarioActual: String, receptor: String) {
        socket.emit(CanalesConexionSocket.salirDeSala.traerNombre(), JSONObject().apply {
            put(MainActivity.EtiquetasJsonObject.emisor.traerNombre(), usuarioActual)
            put(MainActivity.EtiquetasJsonObject.receptor.traerNombre(), receptor)
            put(MainActivity.EtiquetasJsonObject.sala.traerNombre(),sala)
        })
    }

    //3. enviar Mensajes
    fun enviarMensaje(jsonObject: JSONObject) {
        socket.emit(CanalesConexionSocket.mensaje.traerNombre(), jsonObject)
    }

    ///->escuchadores de los canales

    private fun adicionarEscuchadoresCanales() {
        adicionarEsuchadorUsuarioRegistradoEnServidor()
        escuchadorMensaje()
        escuchadorSalirSala()
        escuchadorSalaLlena()
        escuchadorSalaCreada()
        escuchadorUnirSala()
        escuchadorReceptorSalioDeSala()
    }

    private fun adicionarEsuchadorUsuarioRegistradoEnServidor() {
        socket.on(CanalesConexionSocket.registrarUsuario.traerNombre()){
            Log.e(TAG, "Se ha registrado usuario en el servidor")
        }
    }

    private fun escuchadorUnirSala() {
        socket.on(CanalesConexionSocket.unirASala.traerNombre()){
            Log.e(TAG,"Me he unido a la sala")
            escuchadorCanalesVideollamada?.invoke(CanalesConexionSocket.unirASala, it)
        }
    }
    private fun escuchadorSalaCreada() {
        socket.on(CanalesConexionSocket.salaCreada.traerNombre()) {
            Log.e(TAG,"He creado la sala")
            escuchadorCanalesVideollamada?.invoke(CanalesConexionSocket.salaCreada, it)
        }
    }

    private fun escuchadorSalaLlena() {
        socket.on(CanalesConexionSocket.salaLlena.traerNombre()) {
            Log.e(TAG,"La sala esta llena")
            escuchadorCanalesVideollamada?.invoke(CanalesConexionSocket.salaLlena, it)
        }
    }

    private fun escuchadorSalirSala() {
        socket.on(CanalesConexionSocket.salirDeSala.traerNombre()) {
            Log.e(TAG,"Me he salido de la sala")
        }
    }

    private fun escuchadorMensaje() {
        socket.on(CanalesConexionSocket.mensaje.traerNombre()) {
            escuchadorCanalesVideollamada?.invoke(CanalesConexionSocket.mensaje, it)
        }
    }

    private fun escuchadorReceptorSalioDeSala() {
        socket.on(CanalesConexionSocket.receptorSalioDeSala.traerNombre()) {
            escuchadorCanalesVideollamada?.invoke(CanalesConexionSocket.receptorSalioDeSala,null)
        }
    }

    enum class CanalesConexionSocket(private val nombre : String) {
        //Conexion
        conexionEstablecida("conexionEstablecida"),
        registrarUsuario("registrarUsuario"),
        finalizarRegistroUsuario("finalizarRegistroUsuario"),
        //Sala
        salaCreada("salaCreada"),
        salaLlena("salaLlena"),
        unirASala("unirASala"),
        salirDeSala("salirDeSala"),
        receptorSalioDeSala("receptorSalioDeSala"),
        //mensaje
        mensaje("mensaje"),
        ;
        fun traerNombre() = nombre
    }

    companion object {
        private var instancia : ManejadorSocket? =null
        fun traerInstancia() = instancia

        fun inicializarInstancia() {
            if (instancia == null) {
                instancia = ManejadorSocket()
            }
        }
    }
}