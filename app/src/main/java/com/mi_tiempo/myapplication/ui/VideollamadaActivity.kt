package com.mi_tiempo.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mi_tiempo.myapplication.databinding.ActivityVideollamadaBinding

class VideollamadaActivity : AppCompatActivity() {
    companion object {
        const val extrasUsuarioActual = "UsuarioActual"
        const val extrasReceptor = "receptor"
        const val extrasSala = "Sala"
        const val RECEPTOR_SALIO_SALA = 1
        const val REQUEST_CODE = 1

    }


    lateinit var binding: ActivityVideollamadaBinding
    lateinit var intermediarioSocketWebRTC: IntermediarioSocketWebRTC
    lateinit var usuarioActual :String
    lateinit var receptor : String
    lateinit var sala :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideollamadaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuracionIntermediario()
        intermediarioSocketWebRTC.iniciarVideollamada()
        ponerEscuchadores()

    }

    private fun ponerEscuchadores() {
        binding.botonFinalizarVideollamada.setOnClickListener {
            finish()
        }
    }

    private fun configuracionIntermediario() {
        if (intent.extras != null && intent.extras!!.getString(extrasUsuarioActual) != null ) {
            usuarioActual = intent.extras!!.getString(extrasUsuarioActual)!!
        }

        if (intent.extras != null && intent.extras!!.getString(extrasSala) != null ) {
            sala = intent.extras!!.getString(extrasSala)!!
        }

        if (intent.extras != null && intent.extras!!.getString(extrasReceptor) != null ) {
            receptor = intent.extras!!.getString(extrasReceptor)!!
        }

        intermediarioSocketWebRTC = IntermediarioSocketWebRTC(
            this.applicationContext,
            binding.videoLocal,
            binding.videoRemoto,
            sala,
            usuarioActual,
            receptor,
            ::reiniciarVideollamada
        )
    }

    override fun onDestroy() {
        intermediarioSocketWebRTC.finalizarVideollamada()
        super.onDestroy()
    }

    private fun reiniciarVideollamada() {
        setResult(RECEPTOR_SALIO_SALA)
        finish()
    }


}