package br.com.noclaftech.showin.presentation.profilemoreoptions

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.LogoutUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import storage.Singleton
import javax.inject.Inject

class ProfileMoreOptionsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val userUseCase: UserUseCase,
    private val router: ProfileMoreOptionsRouter,
    application: Application) : BaseViewModel(application) {
    private val user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }

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

    fun onRegistrationInformationClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.REGISTRATION_INFORMATION)
    }

    fun onAddSocialsClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.ADD_SOCIALS)
    }

    fun onSwitchAccountClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.SWITCH_ACCOUNT)
    }

    fun onChangePasswordClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.CHANGE_PASSWORD)
    }

    fun onSettingsClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.SETTINGS)
    }

    fun onContactUsClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.CONTACT_US)
    }

    fun onAboutClick(){
        router.navigate(ProfileMoreOptionsRouter.Route.ABOUT)
    }

    fun onBackClick(){
        router.goBack()
    }

    fun logOut(){
        logoutUseCase.execute()
        router.navigate(ProfileMoreOptionsRouter.Route.EXIT)
    }

//    fun getUser(){
//        userUseCase.execute(user.value!!.)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {handleGetUserResult(it)}
//            .addTo(disposables)
//    }
//
//
//
//    private fun handleGetUserResult(result : ArtistResult){
//        when(result){
//            is ArtistResult.Success ->{
//                Singleton.instance.setArtist(result.artist)
//            }
//
//            is ArtistResult.Failure ->{
//
//            }
//
//            is ArtistResult.Loading ->{
//
//            }
//        }
//    }

}
