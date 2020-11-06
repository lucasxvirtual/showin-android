package br.com.noclaftech.showin.presentation.artistmoreoptions

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.LogoutUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class ArtistMoreOptionsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val router: ArtistMoreOptionsRouter,
//    private val getArtistUseCase: GetUserUseCase,
    private val userUseCase: UserUseCase,
    application: Application) : BaseViewModel(application){
    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }


    private var openHowToCreateShow = MutableLiveData<Boolean>().apply { value = false }
    private var openStreamTips = MutableLiveData<Boolean>().apply { value = false }
    private var openMarketingTips = MutableLiveData<Boolean>().apply { value = false }

    fun getOpenHowToCreateShow() : LiveData<Boolean> = openHowToCreateShow
    fun getOpenStreamTips() : LiveData<Boolean> = openStreamTips
    fun getOpenMarketingTips() : LiveData<Boolean> = openMarketingTips

    fun onClickHowToCreateShow(){
        openHowToCreateShow.postValue(true)
    }

    fun onclickStreamTips(){
        openStreamTips.postValue(true)
    }

    fun onClickMarketingTips(){
        openMarketingTips.postValue(true)
    }

    fun logout(){
        logoutUseCase.execute()
        router.navigate(ArtistMoreOptionsRouter.Route.LOGIN)
    }

    fun onClickInformationRegistration(){
        router.navigate(ArtistMoreOptionsRouter.Route.REGISTRATION_INFORMATION)
    }

    fun onClickAddSocials(){
        router.navigate(ArtistMoreOptionsRouter.Route.ADD_SOCIALS)
    }

    fun onClickContactUs(){
        router.navigate(ArtistMoreOptionsRouter.Route.CONTACT_US)
    }

    fun onClickAbout(){
        router.navigate(ArtistMoreOptionsRouter.Route.ABOUT)
    }

    fun onClickSettings(){
        router.navigate(ArtistMoreOptionsRouter.Route.SETTINGS)
    }

    fun onClickTutorials(){
        router.navigate(ArtistMoreOptionsRouter.Route.TUTORIALS)
    }

    fun onClickArtistInformation(){
        router.navigate(ArtistMoreOptionsRouter.Route.ARTIST_INFORMATION)
    }

    fun onCLickPaymentInfo(){
        router.navigate(ArtistMoreOptionsRouter.Route.PAYMENT_INFO)
    }

    fun onClickChangePassword(){
        router.navigate(ArtistMoreOptionsRouter.Route.CHANGE_PASSWORD)
    }

    fun goBack(){
        router.goBack()
    }

    fun getUser(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetArtistResult(it)}
            .addTo(disposables)
    }

    private fun handleGetArtistResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                Singleton.instance.setUser(result.user)
            }
            is UserUseCase.Result.Failure ->{
            }
            is UserUseCase.Result.Loading ->{
            }
        }
    }
}