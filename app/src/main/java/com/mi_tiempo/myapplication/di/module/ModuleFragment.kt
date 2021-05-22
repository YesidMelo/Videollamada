package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import dagger.Module
import dagger.Provides

@Module
class ModuleFragment {

    @ApplicationScope
    @Provides
    fun providesStreamFragment() = StreamFragment()
}