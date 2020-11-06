package br.com.noclaftech.showin.presentation.buyticket

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.buycoins.BuyCoinsActivity
import br.com.noclaftech.showin.presentation.confirmbuyticket.ConfirmBuyTicketActivity
import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.presentation.payment.PaymentActivity
import java.lang.ref.WeakReference

class BuyTicketRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        HOME,
        BUY_COINS,
        CONFIRM_BUY_TICKET
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.HOME -> {
                showNextScreenClearTask(MainActivity::class.java, bundle)
            }
            Route.BUY_COINS -> { showNextScreen(PaymentActivity::class.java, bundle)}
            Route.CONFIRM_BUY_TICKET -> { showNextScreen(ConfirmBuyTicketActivity::class.java, bundle) }
        }
    }

//    override fun showNextScreen(clazz: Class<*>, bundle: Bundle?) {
//        val intent = Intent(activityRef.get(), clazz)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
//        intent.putExtras(bundle!!)
//        activityRef.get()?.startActivity(intent)
//    }
}