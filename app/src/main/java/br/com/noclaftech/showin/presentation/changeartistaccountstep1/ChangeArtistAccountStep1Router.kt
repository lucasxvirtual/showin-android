package br.com.noclaftech.showin.presentation.changeartistaccountstep1

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistaccount.ArtistAccountActivity
import br.com.noclaftech.showin.presentation.changeartistaccountstep2.ChangeArtistAccountStep2Activity
import java.lang.ref.WeakReference

class ChangeArtistAccountStep1Router(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        CHANGE_ARTISTI_ACCOUNT_STEP_2,
        SUCESS_CHANGE
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.CHANGE_ARTISTI_ACCOUNT_STEP_2 -> { showNextScreen(ChangeArtistAccountStep2Activity::class.java, bundle)}
            Route.SUCESS_CHANGE -> { showNextScreenClearTask(ArtistAccountActivity::class.java, bundle) }
        }
    }

}