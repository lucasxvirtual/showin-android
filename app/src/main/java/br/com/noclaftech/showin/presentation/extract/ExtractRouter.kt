package br.com.noclaftech.showin.presentation.extract

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class ExtractRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {

    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {

        }
    }
}