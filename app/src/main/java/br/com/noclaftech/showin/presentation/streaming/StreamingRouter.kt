package br.com.noclaftech.showin.presentation.streaming

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class StreamingRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        BACK,
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.BACK -> { goBack() }
        }
    }

}