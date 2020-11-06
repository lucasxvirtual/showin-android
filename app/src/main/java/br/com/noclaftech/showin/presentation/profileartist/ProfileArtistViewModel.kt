package br.com.noclaftech.showin.presentation.profileartist

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.MessageResult
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.*
import io.reactivex.rxkotlin.addTo
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class ProfileArtistViewModel @Inject constructor(
    private val putImageUseCase: PutImageUseCase,
    private val watchUseCase: WatchUseCase,
    private val postMessageUseCase: PostMessageUseCase,
    private val userUseCase: UserUseCase,
    private val router: ProfileArtistRouter,
    application: Application): BaseViewModel(application) {

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }
    private var open = MutableLiveData<Boolean>().apply { value = false }

    val whatsapp = MutableLiveData<String?>().apply { value = null }
    val instagram = MutableLiveData<String?>().apply { value = null }
    val facebook = MutableLiveData<String?>().apply { value = null }
    val linkedin = MutableLiveData<String?>().apply { value = null }
    val twitter = MutableLiveData<String?>().apply { value = null }
    var noSocials = MutableLiveData<Boolean>().apply { value = false }

    val message = MutableLiveData<String>().apply { value = "" }

    private val messages = MutableLiveData<List<Message>>().apply { value = user.value?.userMessages}
    fun getMessages() : LiveData<List<Message>> = messages

    private val messageError = MutableLiveData<Boolean>().apply { value = false }
    fun getMessageError() : LiveData<Boolean> = messageError

    val showsFuture = MutableLiveData<List<Show>?>().apply { value = Singleton.instance.getArtist()?.blockingGet()?.nextShows }
    fun getShowsFuture(): LiveData<List<Show>?> = showsFuture

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail

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

    fun bound(){

    }

    fun getUser(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleUserResult(it)}
            .addTo(disposables)
    }

    fun getArtist(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetUserResult(it)}
            .addTo(disposables)
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

    fun onClickFollowers(){
        val bundle = Bundle()

        bundle.putString(FollowersActivity.FOLLOWERS, FollowersActivity.FOLLOWERS)
        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
        router.navigate(ProfileArtistRouter.Route.FOLLOWERS, bundle )
    }

    fun onClickFollowing(){
        val bundle = Bundle()

        bundle.putString(FollowersActivity.FOLLOWING, FollowersActivity.FOLLOWING)
        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
        router.navigate(ProfileArtistRouter.Route.FOLLOWERS, bundle )
    }

    fun onClickMySocials(){
        router.navigate(ProfileArtistRouter.Route.MY_SOCIALS)
    }

    fun onNotificationsClick(){
        router.navigate(ProfileArtistRouter.Route.NOTIFICATIONS)
    }

    fun getUserOb() : LiveData<User?> = user
//    fun getArtistOb() : LiveData<Artist?> = artist
    fun getOpen() : LiveData<Boolean> = open

//    fun onClickShowBio(){
//        showBio.postValue(!showBio.value!!)
//    }

    fun onClickEditImage(){
        open.postValue(true)
    }

    fun onClickMessages(){
        router.navigate(ProfileArtistRouter.Route.MESSAGES, Bundle().apply { putSerializable("user", user.value) })
    }

    fun onMoreClicked(){
        router.navigate(ProfileArtistRouter.Route.MORE)
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

    fun onButtonClicked(item: Any, deviceId: String){
        if(item is Show){
            if (item.isPurchased){
                watchUseCase.execute(item.id, deviceId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {handleWatchResult(it, item.id)}
                    .addTo(disposables)
            }
        }
    }

    override fun onClickItem(item: Any){
        if(item is Show){
            router.navigate(ProfileArtistRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(
                ShowDetailsActivity.EXTRA_SHOW, item.id) } )
        }
    }

    fun setImageThumb(image : String?){
        putImageUseCase.execute(user.value!!.id, image!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutImageResult(it)}
            .addTo(disposables)
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
            }
            is UserUseCase.Result.Failure ->{

            }
        }
    }

    private fun handleGetUserResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                user.value = result.user
                Singleton.instance.setUser(result.user)

                showsFuture.postValue(result.user.artist?.nextShows)
//                showsOld.postValue(result.user.artist?.oldShows)
                getNoSocials(result.user)
            }

            is UserUseCase.Result.Failure ->{

            }

            is UserUseCase.Result.Loading ->{

            }
        }
    }

    private fun handleWatchResult(result : WatchUseCase.Result, id: Int){
        when(result){
            is WatchUseCase.Result.Success ->{
                hideDialog()
                router.navigate(ProfileArtistRouter.Route.WATCH, Bundle().apply {
                    putSerializable(WatchActivity.WATCH, result.watch)
                    putInt(WatchActivity.SHOWID, id)
                })
            }

            is WatchUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is WatchUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }
}