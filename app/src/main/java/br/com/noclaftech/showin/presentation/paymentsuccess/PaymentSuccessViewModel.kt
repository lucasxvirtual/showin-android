package br.com.noclaftech.showin.presentation.paymentsuccess

import android.app.Application
import br.com.noclaftech.showin.presentation.BaseViewModel
import javax.inject.Inject

class PaymentSuccessViewModel @Inject constructor(
    private val router: PaymentSuccessRouter,
    application: Application) : BaseViewModel(application){

    fun back(){
        router.navigate(PaymentSuccessRouter.Route.BACK_HOME)
    }
}