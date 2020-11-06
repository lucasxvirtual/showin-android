package br.com.noclaftech.showin.presentation.extract

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.model.Extract
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.GetExtractUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class ExtractViewModel @Inject constructor(
    private val extractRouter: ExtractRouter,
    private val getExtract : GetExtractUseCase,
    application: Application
    ): BaseViewModel(application) {

    private var page : Int = 1
    var isLastPage : Boolean = true
    val pageSize = 30
    private val extract = MutableLiveData<List<Extract>?>().apply { value = null }
    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    fun getUser() : LiveData<User?> = user
    fun getExtract(): LiveData<List<Extract>?> = extract

    fun bound() {
        executeExtract()
    }

    fun onBackClicked(){
        extractRouter.goBack()
    }

    fun actionSwipe(){
        page = 1
        executeExtract()
    }

    private fun executeExtract(){
        getExtract.execute(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleExtractResult(it)}
            .addTo(disposables)
    }

    private fun handleExtractResult(result : GetExtractUseCase.Result){
        nonBlockingLoading.postValue(result == GetExtractUseCase.Result.Loading)
        when(result){
            is GetExtractUseCase.Result.Success ->{
                isLastPage = result.paginationExtract.next != null

                if (page == 1){
                    extract.postValue(null)
                    extract.postValue(result.paginationExtract.results)
                }
                else{
                    val list = arrayListOf<Extract>()
                    list.addAll(extract.value as ArrayList)
                    list.addAll(result.paginationExtract.results)

                    extract.postValue(list)
                }

                page++

            }
            is GetExtractUseCase.Result.Failure ->{
            }
        }
    }
}