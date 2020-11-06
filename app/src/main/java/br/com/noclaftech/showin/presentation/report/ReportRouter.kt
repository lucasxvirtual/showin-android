package br.com.noclaftech.showin.presentation.report

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class ReportRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {

    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {

        }
    }
}