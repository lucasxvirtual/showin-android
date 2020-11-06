package br.com.noclaftech.showin.presentation.forgotpassword

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.login.LoginActivity
import java.lang.ref.WeakReference

class ForgotPasswordRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        LOGIN
    }

    fun navigate (route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.LOGIN -> { showNextScreenClearTask(LoginActivity::class.java, bundle) }

        }
    }
}