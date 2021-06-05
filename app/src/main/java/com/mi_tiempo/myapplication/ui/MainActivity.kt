package com.mi_tiempo.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.engineio.client.Socket
import org.json.JSONObject
import org.webrtc.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    //region Elementos socket

    val usuario1 = "usuario1"
    val usuario2 = "usuario2"
    var usuarioActual = usuario1
    val sala = "${usuario1}_$usuario2"


    //endregion

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adicionarEscuchadores()
        ManejadorSocket.inicializarInstancia()
        ManejadorSocket.traerInstancia()?.registrarConexion()

    }

    override fun onDestroy() {
        ManejadorSocket.traerInstancia()?.desconectarDelServidor(usuarioActual)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode) {
            VideollamadaActivity.RECEPTOR_SALIO_SALA -> {
                Thread{
                    Thread.sleep(500)
                    runOnUiThread {
                        iniciarVideollamada()
                    }
                }.start()

            }
            else -> {
                Log.e(TAG, "Finalizo con codigo $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    ///Metodos privados

    private fun adicionarEscuchadores() {
        binding.botonUsuario1.setOnClickListener {
            usuarioActual = usuario1
            ManejadorSocket.traerInstancia()?.registrarUsuario(usuarioActual)
        }
        binding.botonUsuario2.setOnClickListener {
            usuarioActual = usuario2
            ManejadorSocket.traerInstancia()?.registrarUsuario(usuarioActual)
        }
        binding.botonIniciarLlamada.setOnClickListener {
            iniciarVideollamada()
        }
    }

    private fun iniciarVideollamada() {
        val intent = Intent(this, VideollamadaActivity::class.java)
        intent.putExtra(VideollamadaActivity.extrasUsuarioActual, usuarioActual)
        intent.putExtra(VideollamadaActivity.extrasSala, sala)
        intent.putExtra(VideollamadaActivity.extrasReceptor, if(usuarioActual == usuario1) usuario2 else usuario1)
        startActivityForResult(intent,VideollamadaActivity.REQUEST_CODE)
    }

    ///Manejo de respuetas para el socket

    enum class EtiquetasJsonObject (private val etiqueta : String) {
        answer("answer"),
        candidate("candidate"),
        emisor("emisor"),
        id("id"),
        label("label"),
        got_user_media("got user media"),
        offer("offer"),
        receptor("receptor"),
        sala("Sala"),
        sdp("sdp"),
        type("type"),
        usuario("usuario"),

        ;
        fun traerNombre() = etiqueta
    }


}
