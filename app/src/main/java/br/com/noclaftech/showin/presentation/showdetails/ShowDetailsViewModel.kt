package br.com.noclaftech.showin.presentation.showdetails

import android.app.Application
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.DeepLinkResult
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.ShowDetails
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.*
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.streaming.OtherToolActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class ShowDetailsViewModel @Inject constructor(
    private val showDetailsRouter: ShowDetailsRouter,
    private val getShowDetailsUseCase : ShowDetailsUseCase,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    private val watchUseCase: WatchUseCase,
    private val rememberUseCase: RememberUseCase,
    private val constantsUseCase: GetConstantsUseCase,
    private val deepLinkUseCase: DeepLinkUseCase,
    application: Application) : BaseViewModel(application) {

    val show = MutableLiveData<ShowDetails>().apply { value = null }
    private val detail = MutableLiveData<String?>().apply { value = null }
    val showBuy = MutableLiveData<Boolean>().apply { value = true }
    val showTicketLimit = MutableLiveData<Boolean>().apply { value = true }
    val isNullLimit = MutableLiveData<Boolean>().apply { value = false }
    val isCanFollow = MutableLiveData<Boolean>().apply { value = true }
    private val isHimself = MutableLiveData<Boolean>().apply { value = false }
    val buttonName = MutableLiveData<Int?>().apply { value = null }
    private val user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }

    private val deepLink = MutableLiveData<String?>().apply { value = null }
    val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    private val alert = MutableLiveData<String?>().apply { value = null }

    fun getDetail() : LiveData<String?> = detail
    fun getIsHimSelf() : MutableLiveData<Boolean> = isHimself
    fun getDeepLink(): LiveData<String?> = deepLink
    fun getAlert(): LiveData<String?> = alert

    fun bound(id : Int) {
        getUserLogged()
        if(id != -1)
            getShowDetailsUseCase.execute(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleGetShowDetailsResult(it)}
                .addTo(disposables)
    }

    fun onClickAnotherTool(){
        showDetailsRouter.navigate(ShowDetailsRouter.Route.OTHER_TOOL, Bundle().apply { putSerializable(
            OtherToolActivity.EXTRA_SHOW, show.value!!.toShow()) })
    }

    fun onClickStream(){
        showDetailsRouter.navigate(ShowDetailsRouter.Route.STREAM, Bundle().apply {
            putSerializable(StreamingActivity.EXTRA_SHOW, show.value!!.toShow())
        })
    }

    private fun checkUserLogged() = user.value != null

    fun onEditClick(){
        if (!checkUserLogged()){
            showDetailsRouter.navigate(ShowDetailsRouter.Route.LOGIN)
            return
        }
        showDetailsRouter.navigate(ShowDetailsRouter.Route.EDIT, Bundle().apply { putSerializable("show", show.value) })
    }

    fun share(){
        deepLinkUseCase.execute(show.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleDeepLinkResult(it)}
            .addTo(disposables)
    }

    fun onClickArtist(){
        if (!checkUserLogged()){
            showDetailsRouter.navigate(ShowDetailsRouter.Route.LOGIN)
            return
        }

        val bundle = Bundle()
        bundle.putInt(ArtistProfileActivity.EXTRA_ARTIST, show.value!!.artist.user!!.id)

        showDetailsRouter.navigate(ShowDetailsRouter.Route.ARTIST, bundle )
    }

    fun onClickFollowers(){
        if (!checkUserLogged()){
            showDetailsRouter.navigate(ShowDetailsRouter.Route.LOGIN)
            return
        }
        val bundle = Bundle()

        bundle.putString(FollowersActivity.FOLLOWERS, FollowersActivity.FOLLOWERS)
        bundle.putInt(FollowersActivity.ARTISTID, show.value!!.artist.id)
        showDetailsRouter.navigate(ShowDetailsRouter.Route.FOLLOWERS, bundle )
    }

    fun onBuyClicked(deviceId: String){
        if (!checkUserLogged()){
            showDetailsRouter.navigate(ShowDetailsRouter.Route.LOGIN)
            return
        }
        when {
            isHimself.value!! -> {
                if(listOf("CONFIG", "LIVE").contains(show.value!!.status))
                    alert.postValue(show.value!!.name)
                else
                    setToast(1)
            }
            show.value!!.isPurchased -> {
                watchUseCase.execute(show.value!!.id, deviceId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {handleWatchResult(it, show.value!!.id)}
                    .addTo(disposables)
            }
            else -> {
                showDetailsRouter.navigate(ShowDetailsRouter.Route.BUY_TICKET, Bundle().apply { putSerializable(BuyTicketActivity.EXTRA_SHOW, show.value!!) })
            }
        }
    }

    fun getUser() : LiveData<ShowDetails?> = show

    fun onBackClicked(){
        showDetailsRouter.goBack(user.value != null)
    }

    fun getHour() : String {
        if (show.value?.date != null){
            return "${Helper.getHour(show.value!!.date)} - ${Helper.getHour(show.value!!.dateFinish)}"
        }
        return ""
    }

    fun getDate() : String {
        if (show.value?.date != null){
            return Helper.getDateShow(show.value!!.date)
        }
        return ""
    }

    private fun handleWatchResult(result : WatchUseCase.Result, id: Int){
        when(result){
            is WatchUseCase.Result.Success ->{
                hideDialog()
                showDetailsRouter.navigate(ShowDetailsRouter.Route.WATCH, Bundle().apply {
                    putSerializable(WatchActivity.WATCH, result.watch)
                    putInt(WatchActivity.SHOWID, id)
                })
            }

            is WatchUseCase.Result.Failure ->{
                hideDialog()
                    val st = (result.throwable as HttpException).response().errorBody()!!.string()
                    try {
                        val json = JSONObject(st.toString())
                        detail.postValue(json["detail"].toString())
                    } catch (e: JSONException){
                    }
            }
            is WatchUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    private fun handleDeepLinkResult(result : DeepLinkResult){
        nonBlockingLoading.postValue(result is DeepLinkResult.Loading)
        when(result){
            is DeepLinkResult.Success ->{
                deepLink.postValue(result.deepLink.deeplink)
            }
        }
    }

    fun onFollowClicked(){
        if (!checkUserLogged()){
            showDetailsRouter.navigate(ShowDetailsRouter.Route.LOGIN)
            return
        }
        val user = show.value!!.artist.user
        if(user!!.isFollowed){
            unfollowUseCase.execute(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it, isUnfollow = true)}
                .addTo(disposables)
        } else {
            followUseCase.execute(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it)}
                .addTo(disposables)
        }
    }

    private fun handleFollowResult(result: WorkedResult, isUnfollow: Boolean = false){
        when(result){
            is WorkedResult.Success ->{
                show.mutation {
                    if(!isUnfollow) {
                        show.value!!.artist.user!!.followers += 1
                        show.value!!.artist.user!!.isFollowed = true
                    } else {
                        show.value!!.artist.user!!.followers -= 1
                        show.value!!.artist.user!!.isFollowed = false
                    }
                }
            }
        }
    }

    private fun handleGetShowDetailsResult(result : ShowDetailsUseCase.Result){
        when(result){
            is ShowDetailsUseCase.Result.Success ->{
                hideDialog()
                show.value = result.showDetails

                isNullLimit.value =  show.value?.ticketLimit == null
                showTicketLimit.postValue(show.value?.ticketLimit == null)

                checkStatus()

                if(Singleton.instance.getUser()?.blockingGet() != null)
                    this.isHimself.value = show.value!!.artist.user!!.id == Singleton.instance.getUser()!!.blockingGet().id
                else
                    this.isHimself.value = false
                isCanFollow.value = !isHimself.value!!

                buttonName.postValue(checkButtonName())
            }

            is ShowDetailsUseCase.Result.Failure ->{
                hideDialog()
            }

            is ShowDetailsUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    private fun checkButtonName() : Int{
        return if (isHimself.value!!) { 1 }
        else if (show.value!!.isPurchased && show.value!!.status != "WAITING") { 2 }
        else if (show.value!!.isPurchased && show.value!!.status == "WAITING") { 3 }
        else { 4 }
    }

    private fun checkStatus(){
        if(show.value!!.status == "CANCELED" || show.value!!.status == "DONE"){
            showBuy.postValue(false)
            showTicketLimit.postValue(false)
        }
    }

    private fun getUserLogged() {
        if(user.value == null)
            rememberUseCase.execute()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {handleLoginResult(it)}
                ?.addTo(disposables)

        if(Singleton.instance.getConstants()?.blockingGet() == null)
            constantsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleConstantsResult(it)}
                .addTo(disposables)
    }

    private fun handleConstantsResult(result : GetConstantsUseCase.Result){
        when(result){
            is GetConstantsUseCase.Result.Success ->{
                disposables.clear()
            }
        }
    }

    private fun handleLoginResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                user.value = Singleton.instance.getUser()?.blockingGet()
            }
        }
    }

    fun onClickBuyers(){
        showDetailsRouter.navigate(ShowDetailsRouter.Route.BUYERS, bundleOf("showId" to show.value!!.id))
    }
}