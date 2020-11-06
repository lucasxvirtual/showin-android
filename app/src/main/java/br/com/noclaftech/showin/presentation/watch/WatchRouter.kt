package br.com.noclaftech.showin.presentation.watch

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.donatecoins.DonateCoinsActivity
import br.com.noclaftech.showin.presentation.report.ReportActivity
import java.lang.ref.WeakReference

class WatchRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        DONATE,
        USER,
        REPORT
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.DONATE -> startForResult(DonateCoinsActivity::class.java, bundle, WatchActivity.RESULT)
            Route.USER -> showNextScreen(ArtistProfileActivity::class.java, bundle)
            Route.REPORT-> { showNextScreen(ReportActivity::class.java, bundle) }
        }
    }
}