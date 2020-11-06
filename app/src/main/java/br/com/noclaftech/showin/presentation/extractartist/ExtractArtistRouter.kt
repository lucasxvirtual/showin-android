package br.com.noclaftech.showin.presentation.extractartist

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class ExtractArtistRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {

    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {

        }
    }
}