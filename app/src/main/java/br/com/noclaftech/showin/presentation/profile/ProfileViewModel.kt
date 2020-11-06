package br.com.noclaftech.showin.presentation.profile

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.MessageResult
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.PostMessageUseCase
import br.com.noclaftech.domain.usecase.PutImageUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.profileartist.ProfileArtistRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRouter: ProfileRouter,
    private val userUseCase: UserUseCase,
    private val postMessageUseCase: PostMessageUseCase,
    private val putImageUseCase: PutImageUseCase,
    application: Application) : BaseViewModel(application) {

    var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var open = MutableLiveData<Boolean>().apply { value = false }
    val whatsapp = MutableLiveData<String?>().apply { value = null }
    val instagram = MutableLiveData<String?>().apply { value = null }
    val facebook = MutableLiveData<String?>().apply { value = null }
    val linkedin = MutableLiveData<String?>().apply { value = null }
    val twitter = MutableLiveData<String?>().apply { value = null }
    var noSocials = MutableLiveData<Boolean?>().apply { value = false }

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    val message = MutableLiveData<String>().apply { value = "" }

    private val messages = MutableLiveData<List<Message>>().apply { value = user.value?.userMessages}
    fun getMessages() : LiveData<List<Message>> = messages

    private val messageError = MutableLiveData<Boolean>().apply { value = false }
    fun getMessageError() : LiveData<Boolean> = messageError

    private val openWhatsapp = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenWhatsapp(): LiveData<Boolean> = openWhatsapp

    private val openFacebook = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenFacebook(): LiveData<Boolean> = openFacebook

    private val openInstagram = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenInstagram(): LiveData<Boolean> = openInstagram

    private val openLinkedin = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenLinkedin(): LiveData<Boolean> = openLinkedin

    private val openTwitter = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenTwitter(): LiveData<Boolean> = openTwitter

    fun getUser(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleUserResult(it)}
            .addTo(disposables)
    }

    fun getUserOb() : LiveData<User?> = user
    fun getOpen() : LiveData<Boolean> = open

    fun onClickEditImage(){
        open.postValue(true)
    }

    fun setImageThumb(image : String?){
        putImageUseCase.execute(user.value!!.id, image!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutImageResult(it)}
            .addTo(disposables)
    }

    fun sendMessageClicked(){
        messageError.value = message.value.isNullOrBlank()

        if (messageError.value!!)
            return

        postMessageUseCase.execute(message.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostMessage(it)}
            .addTo(disposables)
    }

    fun onNotificationsClick(){
        profileRouter.navigate(ProfileRouter.Route.NOTIFICATIONS)
    }

    fun onClickFollowers(){
        val bundle = Bundle()

        bundle.putString(FollowersActivity.FOLLOWERS, FollowersActivity.FOLLOWERS)
        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
        profileRouter.navigate(ProfileRouter.Route.FOLLOWERS, bundle )
    }

    fun onClickFollowing(){
        val bundle = Bundle()

        bundle.putString(FollowersActivity.FOLLOWING, FollowersActivity.FOLLOWING)
        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
        profileRouter.navigate(ProfileRouter.Route.FOLLOWERS, bundle )
    }

    fun onClickMoreOptions(){
        profileRouter.navigate(ProfileRouter.Route.MORE_OPTIONS)
    }
    
    fun onClickMySocials(){
        profileRouter.navigate(ProfileRouter.Route.MY_SOCIALS)
    }

    fun onWhatsappClicked(){
        openWhatsapp.postValue(true)
    }

    fun onFacebookClicked(){
        openFacebook.postValue(true)
    }

    fun onInstagramClicked(){
        openInstagram.postValue(true)
    }

    fun onLinkedinCliked(){
        openLinkedin.postValue(true)
    }

    fun onTwitterCliked(){
        openTwitter.postValue(true)
    }

    private fun getNoSocials(user: User) {
        if (user.whatsapp.isNullOrBlank() &&
            user.instagram.isNullOrBlank() &&
            user.facebook.isNullOrBlank() &&
            user.linkedin.isNullOrBlank() &&
            user.twitter.isNullOrBlank()) {

            noSocials.postValue(true)
        } else {
            noSocials.postValue(false)
        }
    }

    private fun handlePostMessage(result : MessageResult){
        when(result){
            is MessageResult.Success ->{
                val list = arrayListOf<Message>()
                list.add(result.message)
                list.addAll(messages.value!!)

                messages.value = list

                message.value = ""

                hideDialog()
            }
            is MessageResult.Failure ->{
                hideDialog()
            }

            is MessageResult.Loading ->{
                showDialog()
            }
        }
    }

    private fun handlePutImageResult(result : UserResult){
        when(result){
            is UserResult.Success ->{
                user.postValue(result.user)
            }
            is UserResult.Failure ->{
            }
            is UserResult.Loading ->{
            }
        }
    }

    private fun handleUserResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                user.postValue(result.user)
                whatsapp.postValue(user.value?.whatsapp)
                instagram.postValue(user.value?.instagram)
                facebook.postValue(user.value?.facebook)
                linkedin.postValue(user.value?.linkedin)
                twitter.postValue(user.value?.twitter)

                getNoSocials(result.user)
            }
            is UserUseCase.Result.Failure ->{
            }
        }
    }
}