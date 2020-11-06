package br.com.noclaftech.showin.presentation.changeartistaccountstep2

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.ChangeArtistAccount
import br.com.noclaftech.domain.usecase.ChangeArtistAccountUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

class ChangeArtistAccountStep2ViewModel @Inject constructor(
    private val router: ChangeArtistAccountStep2Router,
    private val changeArtistAccountUseCase: ChangeArtistAccountUseCase,
    application: Application) : BaseViewModel(application){

    val showPerformed = MutableLiveData<Boolean>().apply { value = true }
    val showNumberFans = MutableLiveData<Boolean>().apply { value = true }
    val accept = MutableLiveData<Boolean>().apply { value = false }
    val bio = MutableLiveData<String>().apply { value = "" }
    private var openTermsUse = MutableLiveData<Boolean>().apply { value = false }
    private val notAccept = MutableLiveData<Boolean>().apply { value = false }
    private val bioError = MutableLiveData<Boolean>().apply { value = false }
    private var changeArtist : ChangeArtistAccount = ChangeArtistAccount()

    fun getOpenTermsUse() : LiveData<Boolean> = openTermsUse
    fun getNotAccept(): LiveData<Boolean> = notAccept
    fun getBioError(): LiveData<Boolean> = bioError

    fun setChangeArtist(serializable: Serializable){
        changeArtist = serializable as ChangeArtistAccount
    }

    fun onTermsUseClicked(){
        openTermsUse.postValue(true)
    }

    fun onChangeClicked(){
        bioError.value = bio.value.isNullOrBlank()
        if (bioError.value!!)
            return

        notAccept.value = !accept.value!!
        if (notAccept.value!!)
            return

        changeArtist.biography = bio.value
        changeArtist.displayOldShows = showPerformed.value
        changeArtist.displayFans = showNumberFans.value


        changeArtistAccountUseCase.execute(changeArtist)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleChangeResult(it)}
            .addTo(disposables)

    }

    private fun handleChangeResult(result : UserResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()

                router.navigate(ChangeArtistAccountStep2Router.Route.SUCESS_CHANGE)
            }
            is UserResult.Failure ->{
                hideDialog()
            }
            is UserResult.Loading -> {
                showDialog()
            }
        }
    }

    fun onBackClicked(){
        router.goBack()
    }
}