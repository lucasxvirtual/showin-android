package br.com.noclaftech.showin.presentation.category

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import java.lang.ref.WeakReference

class CategoryRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        BACK,
        SHOW,
        BUY
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.BACK -> { goBack() }
            Route.SHOW -> showNextScreen(ShowDetailsActivity::class.java, bundle)
            Route.BUY -> { showNextScreen(BuyTicketActivity::class.java, bundle) }
        }
    }

}