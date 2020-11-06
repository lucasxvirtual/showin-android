package br.com.noclaftech.showin.presentation.tickets

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import java.lang.ref.WeakReference

class TicketsRouter (activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SHOW_DETAILS,
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SHOW_DETAILS -> { showNextScreen(ShowDetailsActivity::class.java, bundle) }
        }
    }
}