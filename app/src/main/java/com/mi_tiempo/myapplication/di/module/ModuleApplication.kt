package com.mi_tiempo.myapplication.di.module

import android.app.Application
import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ModuleApplication (private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesApplication() = application

    @ApplicationScope
    @Provides
    fun providesContext() = application.applicationContext

}