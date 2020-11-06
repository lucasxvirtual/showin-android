package br.com.noclaftech.showin.presentation.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.usecase.PutNotificationAndEmailUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter,
    private val putNotificationAndEmailUseCase: PutNotificationAndEmailUseCase,
    application: Application) : BaseViewModel(application){

    private val user = Singleton.instance.getUser()?.blockingGet()
    private var openTermsUse = MutableLiveData<Boolean>().apply { value = false }
    private var openPrivacyPolicy = MutableLiveData<Boolean>().apply { value = false }
    val notification = MutableLiveData<Boolean>().apply { value = user!!.allowNotification }
    val artistEmail = MutableLiveData<Boolean>().apply { value = user!!.allowArtistEmail }
    val commercialEmail = MutableLiveData<Boolean>().apply { value = user!!.allowCommercialEmail }

    fun getOpenTermsUse() : LiveData<Boolean> = openTermsUse
    fun getOpenPrivacyPolicy() : LiveData<Boolean> = openPrivacyPolicy

    fun onTermsUseClicked(){
        openTermsUse.postValue(true)
    }

    fun onPrivacyPolicyClicked(){
        openPrivacyPolicy.postValue(true)
    }

    fun onBackClicked(){
        router.goBack()
    }

    fun onClickNotification(){
        request()
    }

    private fun request(){
        putNotificationAndEmailUseCase.execute(user!!.id, notification.value!!, artistEmail.value!!, commercialEmail.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleChange(it)}
            .addTo(disposables)
    }

    private fun handleChange(result : UserResult){
        when(result){
            is UserResult.Success ->{
//                hideDialog()
            }
            is UserResult.Failure ->{
//                hideDialog()
            }
            is UserResult.Loading -> {
//                showDialog()
            }
        }
    }
}