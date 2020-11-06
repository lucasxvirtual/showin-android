package br.com.noclaftech.showin.presentation.paymentsuccess

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class PaymentSuccessRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        BACK_HOME
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.BACK_HOME -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
        }
    }
}