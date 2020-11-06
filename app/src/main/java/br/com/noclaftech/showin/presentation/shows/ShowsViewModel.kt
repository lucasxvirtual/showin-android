package br.com.noclaftech.showin.presentation.shows

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UnitResult
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.usecase.DeleteShowUseCase
import br.com.noclaftech.domain.usecase.GetUserUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.streaming.OtherToolActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.util.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class ShowsViewModel @Inject constructor(
    private val showsRouter: ShowsRouter,
    private val getUserUseCase: GetUserUseCase,
    private val userUseCase: UserUseCase,
    private val deleteShowUseCase: DeleteShowUseCase,
    application: Application): BaseViewModel(application){

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }

    private val showsOld = MutableLiveData<List<Show>?>().apply { value = Singleton.instance.getArtist()?.blockingGet()?.oldShows }
    val showsFuture = MutableLiveData<List<Show>?>().apply { value = null }

    private val alert = MutableLiveData<String?>().apply { value = null }
    private var show: Show? = null

    fun getShowsOld(): LiveData<List<Show>?> = showsOld
    fun getShowsFuture(): LiveData<List<Show>?> = showsFuture
    fun getAlert(): LiveData<String?> = alert

    fun bound(){

    }

    fun getArtist(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetUserResult(it)}
            .addTo(disposables)
    }

    private fun handleGetUserResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                user.value = result.user
                Singleton.instance.setUser(result.user)

                val artist = result.user.artist

                val combinedShows : ArrayList<Show>

                combinedShows = if(artist?.nextShows != null) {
                    ArrayList(artist.nextShows!!)
                } else {
                    ArrayList()
                }

                if(artist?.testNextShows != null){
                    combinedShows.addAll(artist.testNextShows!!)
                    combinedShows.sortBy {
                        Helper.strToDate(it.date)?.time
                    }
                }

                val combinedPastShows : ArrayList<Show>

                combinedPastShows = if(artist?.oldShows != null) {
                    ArrayList(artist.oldShows!!)
                } else {
                    ArrayList()
                }

                if(artist?.testOldShows != null){
                    combinedPastShows.addAll(artist.testOldShows!!)
                    combinedPastShows.sortBy {
                        Helper.strToDate(it.date)?.time
                    }
                }

                showsFuture.postValue(combinedShows)
                showsOld.postValue(combinedPastShows)

            }
            is UserUseCase.Result.Failure ->{
            }
            is UserUseCase.Result.Loading ->{
            }
        }
    }

    fun onClick(){
        showsRouter.navigate(ShowsRouter.Route.SCHEDULE_SHOW, Bundle())
    }

    fun onClickItem(show: Show){
        showsRouter.navigate(ShowsRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(ShowDetailsActivity.EXTRA_SHOW, show.id) })
    }

    fun onClickButton(show: Show){
        this.show = show
        when(show.status){
            "CANCELED", "DONE" -> {
                deleteShowUseCase.execute(show.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {if(it !is UnitResult.Loading) getArtist()}
                    .addTo(disposables)
            }
            "CONFIG", "LIVE" -> {
                alert.postValue(show.name)
            }
        }
    }

    fun onClickAnotherTool(){
        showsRouter.navigate(ShowsRouter.Route.OTHER_TOOL, Bundle().apply { putSerializable(OtherToolActivity.EXTRA_SHOW, show) })
    }

    fun onClickStream(){
        showsRouter.navigate(ShowsRouter.Route.STREAM, Bundle().apply {
            putSerializable(StreamingActivity.EXTRA_SHOW, show)
        })
    }
}