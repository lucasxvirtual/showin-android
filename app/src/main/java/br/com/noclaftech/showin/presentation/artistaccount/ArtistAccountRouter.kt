package br.com.noclaftech.showin.presentation.artistaccount

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.home.HomeFragment
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class ArtistAccountRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SCHEDULE_NOW,
        HOME
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SCHEDULE_NOW -> { showNextScreen(MainActivity::class.java, bundle)}
            Route.HOME -> {showNextScreen(HomeFragment::class.java, bundle)}
        }
    }

}