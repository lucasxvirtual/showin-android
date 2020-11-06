package br.com.noclaftech.showin.presentation.followers

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import java.lang.ref.WeakReference

class FollowersRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        ARTIST,
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.ARTIST -> { showNextScreen(ArtistProfileActivity::class.java, bundle) }
        }
    }
}