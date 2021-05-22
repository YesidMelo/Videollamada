package com.mi_tiempo.myapplication.di.component

import com.mi_tiempo.myapplication.di.module.ModuleApplication
import dagger.Component

@Component( modules = [
    ModuleApplication::class
])
interface ComponentApplication {
}