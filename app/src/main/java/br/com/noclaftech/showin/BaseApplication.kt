package br.com.noclaftech.showin

import android.app.Application
import br.com.noclaftech.showin.di.application.ApplicationComponent
import br.com.noclaftech.showin.di.application.ApplicationModule
import br.com.noclaftech.showin.di.application.DaggerApplicationComponent
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil

class BaseApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        FacebookUtil.facebookInit(this)
        inject()
    }

    fun inject() {
        component = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(this)
        ).build()
        component.inject(this )
    }
}