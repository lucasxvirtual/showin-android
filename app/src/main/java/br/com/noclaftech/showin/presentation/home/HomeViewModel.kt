package br.com.noclaftech.showin.presentation.home

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.usecase.GetShowAndArtistUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.domain.usecase.WatchUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsRouter
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject


class HomeViewModel  @Inject constructor(
    private val getShowAndArtistUseCase: GetShowAndArtistUseCase,
    private val homeRouter: HomeRouter,
    private val watchUseCase: WatchUseCase,
    private val userUseCase: UserUseCase,
    application: Application): BaseViewModel(application){

    private val shows = MutableLiveData<List<ListTypeShow>?>().apply { value = null }
    private val artists = MutableLiveData<List<ListTypeArtist>?>().apply { value = null }
    private val banners = MutableLiveData<List<Banner>?>().apply { value = null }

    private val detail = MutableLiveData<String?>().apply { value = null }
    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }

    fun getDetail() : LiveData<String?> = detail
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    fun bound(){
        getShowAndArtistUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetListTypeShowResult(it)}
            .addTo(disposables)
    }

    fun getShows(): LiveData<List<ListTypeShow>?> = shows
    fun getArtists(): LiveData<List<ListTypeArtist>?> = artists
    fun getBanners(): LiveData<List<Banner>?> = banners

    override fun onClickItem(item: Any){
        when(item){
            is Show -> homeRouter.navigate(HomeRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(ShowDetailsActivity.EXTRA_SHOW, item.id) } )
            is Artist -> homeRouter.navigate(HomeRouter.Route.ARTIST, Bundle().apply { putInt(ArtistProfileActivity.EXTRA_ARTIST, item.user!!.id) } )
            is Banner -> {
                if(item.show != null)
                    homeRouter.navigate(HomeRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(ShowDetailsActivity.EXTRA_SHOW, item.show!!.id) } )
                else if(item.link != null){
                    homeRouter.navigate(HomeRouter.Route.WEB, Bundle().apply { putString("link", item.link) } )
                }
            }
        }
    }

    fun onSearchClick(){
        homeRouter.navigate(HomeRouter.Route.SEARCH)
    }

    fun onTestClick(){
//        homeRouter.navigate(HomeRouter.Route.WATCH, Bundle().apply {
//            putSerializable(WatchActivity.WATCH, Watch("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"))
//            putInt(WatchActivity.SHOWID, 355)
//        })
    }

    fun onButtonClicked(item: Any, deviceId: String? = null){
        if(item is Show){
            if (item.isPurchased){
                watchUseCase.execute(item.id, deviceId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {handleWatchResult(it, item.id)}
                    .addTo(disposables)
            }
            else {
                homeRouter.navigate(
                    HomeRouter.Route.BUY,
                    Bundle().apply { putSerializable(BuyTicketActivity.EXTRA_SHOW_HOME, item) })
            }
        }
    }

    fun getUser(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleUserResult(it)}
            .addTo(disposables)
    }

    private fun handleGetListTypeShowResult(result: GetShowAndArtistUseCase.Result){
        nonBlockingLoading.postValue(result == GetShowAndArtistUseCase.Result.Loading)
        when(result){
            is GetShowAndArtistUseCase.Result.Success ->{
                shows.postValue(result.showAndArtist.shows)
                artists.postValue(result.showAndArtist.artists)
                banners.postValue(result.showAndArtist.banners)
            }
        }
    }

    private fun handleWatchResult(result : WatchUseCase.Result, id: Int){
        when(result){
            is WatchUseCase.Result.Success ->{
                hideDialog()
                homeRouter.navigate(HomeRouter.Route.WATCH, Bundle().apply {
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

    private fun handleUserResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                if (result.user.isArtist){
                    userUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {handleGetArtistResult(it)}
                        .addTo(disposables)
                }
            }
            is UserUseCase.Result.Failure ->{

            }
        }
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

    fun onClickSchedule(){
        val user = Singleton.instance.getUser()?.blockingGet()
        if(user?.isArtist!!){
            homeRouter.navigate(HomeRouter.Route.SCHEDULE)
        } else {
            homeRouter.navigate(HomeRouter.Route.CHANGE_TO_ARTIST_ACCOUNT)
        }
    }
}