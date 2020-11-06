package br.com.noclaftech.showin.presentation.confirmbuyticket

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class ConfirmBuyTicketRouter(activityRef: WeakReference<Activity>): BaseRouter(activityRef) {

    enum class Route {
        MY_TICKETS
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.MY_TICKETS -> showNextScreenClearTask(MainActivity::class.java, bundle)
        }
    }

}