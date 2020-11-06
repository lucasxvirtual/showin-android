package br.com.noclaftech.showin.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.StringResult
import br.com.noclaftech.domain.usecase.EditNotificationTokenUseCase
import br.com.noclaftech.domain.usecase.GetPopupDateUseCase
import br.com.noclaftech.domain.usecase.SetPopupDateUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper.Companion.getDateString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(private val editNotificationTokenUseCase: EditNotificationTokenUseCase,
                                        private val getPopupDateUseCase: GetPopupDateUseCase,
                                        private val router: MainRouter,
                                        private val setPopupDateUseCase: SetPopupDateUseCase,
                                        application: Application) : BaseViewModel(application) {

    val user = Singleton.instance.getUser()?.blockingGet()
    private val popUp = MutableLiveData<Boolean>().apply { value = false }

    fun getPopUp():LiveData<Boolean> = popUp

    fun bound() {
        getPopupDateUseCase.execute().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handlePopup(it) }
            .addTo(disposables)
    }

    private fun handlePopup(result: StringResult){
//        val strDate = getDateString(Date())
//        if(result is StringResult.Success){
//            if(result.string == null || result.string != strDate)
//                popUp.postValue(true)
//        }
//        if(result !is StringResult.Loading){
//            setPopupDateUseCase.execute(strDate)
//        }
    }

    fun goToChangeToArtistAccount(){
        router.navigate(MainRouter.Route.CHANGE_TO_ARTIST_ACCOUNT)
    }

    fun editNotificationToken(token : String){
        editNotificationTokenUseCase.execute(user!!.id, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Log.i("MainViewModel", token) }
            .addTo(disposables)
    }


}