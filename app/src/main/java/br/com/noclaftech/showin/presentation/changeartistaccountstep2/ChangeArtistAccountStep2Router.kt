package br.com.noclaftech.showin.presentation.changeartistaccountstep2

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.artistaccount.ArtistAccountActivity
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class ChangeArtistAccountStep2Router(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SUCESS_CHANGE
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SUCESS_CHANGE -> showNextScreenClearTask(ArtistAccountActivity::class.java, bundle)
        }
    }
}