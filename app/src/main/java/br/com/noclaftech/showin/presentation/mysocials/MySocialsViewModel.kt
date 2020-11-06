package br.com.noclaftech.showin.presentation.mysocials

import android.app.Application
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.presentation.BaseViewModel
import storage.Singleton
import javax.inject.Inject

class MySocialsViewModel @Inject constructor(
    private val router : MySocialsRouter,
    application: Application) : BaseViewModel(application) {

    fun onClickGoBack(){
        router.goBack()
    }

    fun bound(){

    }
}