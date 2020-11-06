package br.com.noclaftech.showin.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.forgotpassword.ForgotPasswordActivity
import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.presentation.register.RegisterActivity
import java.lang.ref.WeakReference

class LoginRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        MAIN,
        REGISTER,
        FORGOT_PASSWORD
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.MAIN -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
            Route.REGISTER -> { showNextScreen(RegisterActivity::class.java, bundle)}
            Route.FORGOT_PASSWORD -> { showNextScreen(ForgotPasswordActivity::class.java, bundle)}
        }
    }
}