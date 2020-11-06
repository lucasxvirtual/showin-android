package br.com.noclaftech.showin.presentation.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class RegisterRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        MAIN
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.MAIN -> { showNextScreen(MainActivity::class.java, bundle) }
        }
    }

    override fun showNextScreen(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activityRef.get(), clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
        intent.putExtras(bundle!!)
        activityRef.get()?.startActivity(intent)
    }
}