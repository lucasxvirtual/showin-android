package br.com.noclaftech.showin.presentation.splash

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.login.LoginActivity
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class SplashRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        MAIN,
        LOGIN
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.MAIN -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
            Route.LOGIN -> { showNextScreenClearTask(LoginActivity::class.java, bundle)}
        }
    }
}