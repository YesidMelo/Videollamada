package com.mi_tiempo.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mi_tiempo.myapplication.R
import com.mi_tiempo.myapplication.base.App
import com.mi_tiempo.myapplication.ui.stream_fragment.StreamFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var streamFragment : StreamFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        (App.getContext() as App).traerComponenteAplicacion()?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToFragment(streamFragment)
    }

    //Private methods

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}