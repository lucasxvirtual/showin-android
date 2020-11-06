package br.com.noclaftech.showin.presentation.buyers

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.FollowResult
import br.com.noclaftech.domain.model.Follow
import br.com.noclaftech.domain.usecase.GetShowBuyersUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BuyersViewModel @Inject constructor(
    private val buyersRouter: BuyersRouter,
    private val getShowBuyersUseCase: GetShowBuyersUseCase,
    application: Application
) : BaseViewModel(application) {

    private val showId = MutableLiveData<Int>().apply { value = null }

    private var page : Int = 1
    var isLastPage : Boolean = true
    val pageSize = 30

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    private val buyers = MutableLiveData<List<Follow>>().apply { value = null }
    fun getBuyers(): LiveData<List<Follow>> = buyers

    override fun onClickItem(item: Any){

        if(item is Follow){
            buyersRouter.navigate(BuyersRouter.Route.ARTIST, Bundle().apply { putInt(
                ArtistProfileActivity.EXTRA_ARTIST, item.id) } )
        }
    }

    fun bound(id : Int){
        showId.value = id
        requestGetFollowers()
    }

    fun actionSwipe(){
        page = 1
        bound(showId.value!!)
    }

    fun onBackClicked(){
        buyersRouter.goBack()
    }

    private fun requestGetFollowers(){
        getShowBuyersUseCase.execute(showId.value!!, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleBuyersResult(it)}
            .addTo(disposables)
    }

    private fun handleBuyersResult(result : FollowResult){
        nonBlockingLoading.postValue(result == FollowResult.Loading)
        when(result){
            is FollowResult.Success ->{
                isLastPage = result.paginationFollow.next != null

                if (page == 1){
                    buyers.postValue(null)
                    buyers.postValue(result.paginationFollow.results)
                }
                else{
                    val list = arrayListOf<Follow>()
                    list.addAll(buyers.value as ArrayList)
                    list.addAll(result.paginationFollow.results)
                    buyers.postValue(list)
                }
                page++
            }
            is FollowResult.Failure ->{

            }
        }
    }
}