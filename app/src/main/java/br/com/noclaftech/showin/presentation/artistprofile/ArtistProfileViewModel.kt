package br.com.noclaftech.showin.presentation.artistprofile

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.*
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ArtistProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val userUseCase: UserUseCase,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val watchUseCase: WatchUseCase,
    private val router: ArtistProfileRouter,
    application: Application) : BaseViewModel(application){

    private val detail = MutableLiveData<String?>().apply { value = null }
    private val showsFuture = MutableLiveData<List<Show>?>().apply { value = null }
    private val id = MutableLiveData<Int>().apply { value = null }
//    val isArtist = MutableLiveData<Boolean>().apply { value = null }
    val whatsapp = MutableLiveData<String?>().apply { value = null }
    val instagram = MutableLiveData<String?>().apply { value = null }
    val facebook = MutableLiveData<String?>().apply { value = null }
    val linkedin = MutableLiveData<String?>().apply { value = null }
    val twitter = MutableLiveData<String?>().apply { value = null }
    val user = MutableLiveData<User>().apply { value = null }

    private val openWhatsapp = MutableLiveData<Boolean>().apply { value = false }
    private val openFacebook = MutableLiveData<Boolean>().apply { value = false }
    private val openInstagram = MutableLiveData<Boolean>().apply { value = false }
    private val openLinkedin = MutableLiveData<Boolean>().apply { value = false }
    private val openTwitter = MutableLiveData<Boolean>().apply { value = false }
    private val messages = MutableLiveData<List<Message>>().apply { value = null }

    fun getShowsFuture(): LiveData<List<Show>?> = showsFuture
    fun getDetail() : LiveData<String?> = detail
    fun getOpenWhatsapp(): LiveData<Boolean> = openWhatsapp
    fun getOpenFacebook(): LiveData<Boolean> = openFacebook
    fun getOpenInstagram(): LiveData<Boolean> = openInstagram
    fun getOpenLinkedin(): LiveData<Boolean> = openLinkedin
    fun getOpenTwitter(): LiveData<Boolean> = openTwitter
    fun getMessages() : LiveData<List<Message>> = messages
    fun getUserOb() : LiveData<User?> = user

    fun bound(id : Int){
        this.id.value = id
        request()
    }

//    fun onClickMessages(){
//        val bundle = Bundle()
//        bundle.putSerializable("user", user.value)
//        router.navigate(ArtistProfileRouter.Route.ARTIST_MESSAGES, bundle)
//    }

//    fun onClickFollowers(){
//        val bundle = Bundle()
//
//        bundle.putString(FollowersActivity.FOLLOWERS, FollowersActivity.FOLLOWERS)
//        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
//        router.navigate(ArtistProfileRouter.Route.FOLLOWERS, bundle )
//    }
//
//    fun onClickFollowing(){
//        val bundle = Bundle()
//
//        bundle.putString(FollowersActivity.FOLLOWING, FollowersActivity.FOLLOWING)
//        bundle.putInt(FollowersActivity.USERID, user.value!!.id)
//        router.navigate(ArtistProfileRouter.Route.FOLLOWERS, bundle )
//    }

//    fun onClickSocials(){
//        val bundle = Bundle()
//
//        bundle.putSerializable("user", user.value)
//        router.navigate(ArtistProfileRouter.Route.SOCIALS, bundle)
//    }

    fun onBackClicked(){
        router.goBack()
    }

    override fun onClickItem(item: Any){
        if(item is Show){
            router.navigate(ArtistProfileRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(
                ShowDetailsActivity.EXTRA_SHOW, item.id) } )
        }
    }

    fun onButtonClicked(item: Any, deviceId: String?=null){
        if(item is Show){
            if (item.isPurchased){
                watchUseCase.execute(item.id, deviceId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {handleWatchResult(it, item.id)}
                    .addTo(disposables)
            }
            else {
                router.navigate(
                    ArtistProfileRouter.Route.BUY,
                    Bundle().apply { putSerializable(BuyTicketActivity.EXTRA_SHOW_HOME, item) })
            }
        }
    }

    fun onClickFollow(){
        if(user.value!!.isFollowed){
            unfollowUseCase.execute(user.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it, isUnfollow = true)}
                .addTo(disposables)
        } else {
            followUseCase.execute(user.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it)}
                .addTo(disposables)
        }
    }

    fun onResume(){
        request()
    }

    private fun request(){
        getUserUseCase.execute(id.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetUserResult(it)}
            .addTo(disposables)
    }

    private fun handleFollowResult(result: WorkedResult, isUnfollow: Boolean = false){
        when(result){
            is WorkedResult.Success ->{
                user.mutation {
                    if(!isUnfollow) {
                        user.value!!.followers += 1
                        user.value!!.isFollowed = true
                    } else {
                        user.value!!.followers -= 1
                        user.value!!.isFollowed = false
                    }
                }
            }
        }
    }

    private fun handleGetUserResult(result : UserResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()
                user.postValue(result.user)

                showsFuture.postValue(result.user.artist?.nextShows)
                messages.postValue(result.user.userMessages)
                whatsapp.postValue(result.user.whatsapp)
                facebook.postValue(result.user.facebook)
                linkedin.postValue(result.user.linkedin)
                twitter.postValue(result.user.twitter)
                instagram.postValue(result.user.instagram)
            }

            is UserResult.Failure ->{
                hideDialog()
            }

            is UserResult.Loading ->{
                showDialog()
            }
        }
    }

    private fun handleWatchResult(result : WatchUseCase.Result, id: Int){
        when(result){
            is WatchUseCase.Result.Success ->{
                hideDialog()
                router.navigate(ArtistProfileRouter.Route.WATCH, Bundle().apply {
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
}