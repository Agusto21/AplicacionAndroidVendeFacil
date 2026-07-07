package com.tuusuario.vendefacil

import android.app.Application
import com.tuusuario.vendefacil.di.AppContainer

class VendeFacilApp : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}