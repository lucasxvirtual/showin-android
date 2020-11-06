package br.com.noclaftech.showin.presentation.payment

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.paymentsuccess.PaymentSuccessActivity
import java.lang.ref.WeakReference

class PaymentRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        PAYMENT_SUCESS
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.PAYMENT_SUCESS -> { showNextScreen(PaymentSuccessActivity::class.java, bundle) }
        }
    }
}