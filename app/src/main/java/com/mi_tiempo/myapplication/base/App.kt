package com.mi_tiempo.myapplication.base

import android.app.Application
import android.content.Context
import com.mi_tiempo.myapplication.di.component.ComponentApplication
import com.mi_tiempo.myapplication.di.component.DaggerComponentApplication
import com.mi_tiempo.myapplication.di.module.ModuleApplication

class App : Application() {

    private var componentApplication : ComponentApplication? = null

    override fun onCreate() {
        inicializarComponentesDagger()
        super.onCreate()
        context = this
    }

    fun traerComponenteAplicacion() = componentApplication

    private fun inicializarComponentesDagger() {
        componentApplication = DaggerComponentApplication
            .builder()
            .moduleApplication(ModuleApplication(this))
            .build()
    }

    companion object {
        private var context : Context?= null
        fun getContext () = context!!
    }

}