package br.com.noclaftech.showin.presentation.tutorials

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class TutorialsViewModel @Inject constructor(
    private val router: TutorialsRouter,
    application: Application) : BaseViewModel(application){

    private var openHowToCreateShow = MutableLiveData<Boolean>().apply { value = false }
    private var openStreamTips = MutableLiveData<Boolean>().apply { value = false }
    private var openMaketingTips = MutableLiveData<Boolean>().apply { value = false }

    fun getOpenHowToCreateShow() : LiveData<Boolean> = openHowToCreateShow
    fun getOpenStreamTips() : LiveData<Boolean> = openStreamTips
    fun getOpenMaketingTips() : LiveData<Boolean> = openMaketingTips
    fun onClickHowToCreateShow(){
        openHowToCreateShow.postValue(true)
    }

    fun onclickStreamTips(){
        openStreamTips.postValue(true)
    }

    fun onClickMaketingTips(){
        openMaketingTips.postValue(true)
    }

    fun goBack(){
        router.goBack()
    }
}