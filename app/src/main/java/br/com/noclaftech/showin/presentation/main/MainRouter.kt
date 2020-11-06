package br.com.noclaftech.showin.presentation.main

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.changeartistaccountstep1.ChangeArtistAccountStep1Activity
import java.lang.ref.WeakReference

class MainRouter (private val activityRef: WeakReference<Activity>): BaseRouter(activityRef) {

    enum class Route {
        CHANGE_TO_ARTIST_ACCOUNT
    }

    fun navigate (route: Route, bundle: Bundle = Bundle()){
        when (route) {
            Route.CHANGE_TO_ARTIST_ACCOUNT -> { showNextScreen(ChangeArtistAccountStep1Activity::class.java, bundle) }
        }
    }
}