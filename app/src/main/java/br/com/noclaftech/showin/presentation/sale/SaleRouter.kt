package br.com.noclaftech.showin.presentation.sale

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.buycoins.BuyCoinsActivity
import br.com.noclaftech.showin.presentation.donatecoins.DonateCoinsActivity
import br.com.noclaftech.showin.presentation.extract.ExtractActivity
import br.com.noclaftech.showin.presentation.extractartist.ExtractArtistActivity
import java.lang.ref.WeakReference

class SaleRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        EXTRACT,
        EXTRACT_ARTIST,
        DONATE_COINS,
        BUY_COINS
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.EXTRACT -> { showNextScreen(ExtractActivity::class.java, bundle) }
            Route.EXTRACT_ARTIST -> { showNextScreen(ExtractArtistActivity::class.java, bundle) }
            Route.DONATE_COINS -> { showNextScreen(DonateCoinsActivity::class.java, bundle) }
            Route.BUY_COINS -> { showNextScreen(BuyCoinsActivity::class.java, bundle)}
        }
    }
}