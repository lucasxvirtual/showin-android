package br.com.noclaftech.showin.presentation.about

import android.app.Application
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import storage.Singleton
import javax.inject.Inject

class AboutViewModel @Inject constructor(
    private val router: AboutRouter,
    application: Application) : BaseViewModel(application){

    val about =  MutableLiveData<String>().apply { value = Singleton.instance.getConstants()?.blockingGet()!!.config.about }


    fun onBackClicked(){
        router.goBack()
    }
}