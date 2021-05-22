package com.mi_tiempo.myapplication.di.module

import com.mi_tiempo.myapplication.di.scope.ApplicationScope
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragmentViewModel
import dagger.Module
import dagger.Provides

@Module
class ModuleViewModel {

    @ApplicationScope
    @Provides
    fun providesStreamFragmentViewModel() = StreamFragmentViewModel()
}