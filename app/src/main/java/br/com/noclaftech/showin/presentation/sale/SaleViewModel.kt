package br.com.noclaftech.showin.presentation.sale

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ArtistExtractResult
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.usecase.DonateUseCase
import br.com.noclaftech.domain.usecase.GetArtistExtractUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.usecase.GetExtractUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import br.com.noclaftech.showin.ext.mutation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class SaleViewModel @Inject constructor(
    private val saleRouter: SaleRouter,
    private val userUseCase: UserUseCase,
    private val getExtractUserCase : GetExtractUseCase,
    private val getArtistExtractUseCase: GetArtistExtractUseCase,
    private val donateUseCase: DonateUseCase,
    application: Application): BaseViewModel(application) {

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }
    private val artistExtract = MutableLiveData<Pagination<ArtistExtract>>().apply { value = null }
    private val extract = MutableLiveData<List<Extract>?>().apply { value = null }
    private val displayingExtract = MutableLiveData<Int>().apply { value = 0 }
    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    private val donatedWinnsSuccess = MutableLiveData<Boolean>().apply { value = null }

    fun getDonatedWinnsSuccess() : LiveData<Boolean> = donatedWinnsSuccess
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading
    fun getArtistExtract(): LiveData<Pagination<ArtistExtract>?> = artistExtract
    fun getExtract(): LiveData<List<Extract>?> = extract
    fun getDisplayingExtract() : LiveData<Int?> = displayingExtract

    private var extractPage : Int = 1
    private var artistExtractPage : Int = 1
    val extractPageSize = 30
    val artistExtractPageSize = 30
    var isLastExtractPage : Boolean = true
    var isLastArtistExtractPage : Boolean = true

    fun bound() {
        apiGetExtract()
    }

    private fun apiGetExtract() {
        getExtractUserCase.execute(extractPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleExtractResult(it)}
            .addTo(disposables)
    }

    private fun apiGetArtistExtract() {
        getArtistExtractUseCase.execute(artist.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleArtistExtractResult(it)}
            .addTo(disposables)
    }

    fun onClickExtract() {
        displayingExtract.postValue(0)
    }

    fun onClickArtistExtract() {
        apiGetArtistExtract()
    }

//    fun onClickNextDonateCoins(){
//        saleRouter.navigate(SaleRouter.Route.DONATE_COINS, Bundle())
//    }

    fun onClickBuyCoins(){
        saleRouter.navigate(SaleRouter.Route.BUY_COINS, Bundle())
    }

    fun onTransferClicked(triple : Triple<Int, String, String>) {
        donateUseCase.execute(
            id = user.value!!.balance?.id!!,
            value = triple.first,
            user = triple.second,
            password = triple.third
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleDonateWinnsResult(it) }
            .addTo(disposables)
    }

    fun getUserOb() : LiveData<User?> = user

    fun getUser(){
        userUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleUserResult(it)}
            .addTo(disposables)
    }

    private fun handleUserResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                user.postValue(result.user)
            }
            is UserUseCase.Result.Failure ->{

            }
        }
    }

    private fun handleDonateWinnsResult(result : DonateUseCase.Result) {
        when(result) {
            is DonateUseCase.Result.Success -> {
                donatedWinnsSuccess.postValue(true)
            }
            is DonateUseCase.Result.Failure -> {
                donatedWinnsSuccess.postValue(false)
            }
            is DonateUseCase.Result.Loading -> {

            }
        }
    }

    private fun handleExtractResult(result : GetExtractUseCase.Result){
        nonBlockingLoading.postValue(result is GetExtractUseCase.Result.Loading)
        when(result){
            is GetExtractUseCase.Result.Success ->{
                isLastExtractPage = result.paginationExtract.next != null

                if (extractPage == 1){
                    extract.postValue(null)
                    extract.postValue(result.paginationExtract.results)
                }
                else{
                    val list = arrayListOf<Extract>()
                    list.addAll(extract.value as ArrayList)
                    list.addAll(result.paginationExtract.results)

                    extract.postValue(list)
                }

                extractPage++

            }
            is GetExtractUseCase.Result.Failure ->{
            }

        }
    }

    private fun handleArtistExtractResult(result: ArtistExtractResult){
        nonBlockingLoading.postValue(result is ArtistExtractResult.Loading)
        when(result){
            is ArtistExtractResult.Success ->{
                isLastArtistExtractPage = result.paginationArtistExtract.next != null

                if (artistExtractPage == 1){
                    artistExtract.postValue(null)
                    artistExtract.postValue(result.paginationArtistExtract)
                }
                else{
                    artistExtract.mutation {
                        val list = ArrayList<ArtistExtract>(value!!.results)
                        list.addAll(result.paginationArtistExtract.results)
                        value!!.results = list
                    }
                }

                artistExtractPage++

                displayingExtract.postValue(1)
            }
            is ArtistExtractResult.Failure ->{
            }
        }
    }

    enum class ToastMessage(val value: Int) {
        SOON(1)
    }


}