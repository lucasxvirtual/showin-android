package br.com.noclaftech.showin.presentation.buycoins

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.model.Pack
import storage.Singleton
import javax.inject.Inject

class BuyCoinsViewModel @Inject constructor(
    private val router: BuyCoinsRouter,
    application: Application) : BaseViewModel(application){

    private val packs = MutableLiveData<List<Pack>>().apply { value = Singleton.instance.getConstants()?.blockingGet()!!.packs }

    fun getPacks() : LiveData<List<Pack>> = packs

    fun onBackClicked(){
        router.goBack()
    }

    fun onClickBuyPack(item: Any){
        val pack = item as Pack

        router.navigate(BuyCoinsRouter.Route.PAYMENT, Bundle().apply { putSerializable("pack", pack) })
    }
}