package br.com.noclaftech.showin.presentation.followers

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.FollowResult
import br.com.noclaftech.domain.model.Follow
import br.com.noclaftech.domain.usecase.GetFollowersUseCase
import br.com.noclaftech.domain.usecase.GetFollowingUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FollowersViewModel @Inject constructor(
    private val followersRouter: FollowersRouter,
    private val followersUseCase: GetFollowersUseCase,
    private val followingUseCase: GetFollowingUseCase,
    application: Application) : BaseViewModel(application) {

    private val userId = MutableLiveData<Int>().apply { value = null }
//    private val artistId = MutableLiveData<Int>().apply { value = null }
    val isFollowers = MutableLiveData<Boolean>().apply { value = false}

    private var page : Int = 1
    var isLastPage : Boolean = true
    val pageSize = 30

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    private val followers = MutableLiveData<List<Follow>>().apply { value = null }
    fun getFollowers(): LiveData<List<Follow>> = followers

    override fun onClickItem(item: Any){

        if(item is Follow){
            followersRouter.navigate(FollowersRouter.Route.ARTIST, Bundle().apply { putInt(
                ArtistProfileActivity.EXTRA_ARTIST, item.id) } )
        }
    }

    fun setUserIdGetFollowers(id : Int){
        userId.value = id
        isFollowers.postValue(true)
        requestGetFollowers()
    }

    fun setUserIdGetFollowing(id : Int){
        userId.value = id
        isFollowers.postValue(false)
        requestGetFollowings()
    }

    fun actionSwipe(){
        page = 1
        bound()
    }

    fun bound(){
        if (isFollowers.value!!){
            requestGetFollowers()
        }
        else{
            requestGetFollowings()
        }
    }

    fun onBackClicked(){
        followersRouter.goBack()
    }

    private fun requestGetFollowers(){
        followersUseCase.execute(userId.value!!, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleFollowResult(it)}
            .addTo(disposables)
    }

    private fun requestGetFollowings(){
        followingUseCase.execute(userId.value!!, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleFollowResult(it)}
            .addTo(disposables)
    }

    private fun handleFollowResult(result : FollowResult){
        nonBlockingLoading.postValue(result == FollowResult.Loading)
        when(result){
            is FollowResult.Success ->{
                isLastPage = result.paginationFollow.next != null

                if (page == 1){
                    followers.postValue(null)
                    followers.postValue(result.paginationFollow.results)
                }
                else{
                    val list = arrayListOf<Follow>()
                    list.addAll(followers.value as ArrayList)
                    list.addAll(result.paginationFollow.results)
                    followers.postValue(list)
                }
                page++
            }
            is FollowResult.Failure ->{

            }
        }
    }
}