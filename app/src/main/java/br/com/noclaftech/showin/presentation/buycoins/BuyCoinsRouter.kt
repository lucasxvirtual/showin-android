package br.com.noclaftech.showin.presentation.buycoins

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.payment.PaymentActivity
import java.lang.ref.WeakReference

class BuyCoinsRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        PAYMENT
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.PAYMENT -> { showNextScreen(PaymentActivity::class.java, bundle) }
        }
    }
}