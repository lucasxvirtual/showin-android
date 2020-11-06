package br.com.noclaftech.showin.presentation.extractartist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ArtistExtractResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.ArtistExtract
import br.com.noclaftech.domain.model.Pagination
import br.com.noclaftech.domain.usecase.GetArtistExtractUseCase
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExtractArtistViewModel @Inject constructor(
    private val extractArtistRouter: ExtractArtistRouter,
    private val getArtistExtractUseCase: GetArtistExtractUseCase,
    application: Application) : BaseViewModel(application) {

    private val extract = MutableLiveData<Pagination<ArtistExtract>>().apply { value = null }
    fun getExtract(): LiveData<Pagination<ArtistExtract>?> = extract

    private var isLastPage: Boolean = false
    private var page: Int = 1

    private val artist = MutableLiveData<Artist?>().apply { value = null }

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    fun bound(artist: Artist) {
        getArtistExtractUseCase.execute(artist.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleArtistExtractResult(it)}
            .addTo(disposables)
    }

    private fun handleArtistExtractResult(result: ArtistExtractResult){
        nonBlockingLoading.postValue(result == ArtistExtractResult.Loading)
        when(result){
            is ArtistExtractResult.Success ->{
                isLastPage = result.paginationArtistExtract.next != null

                if (page == 1){
                    extract.postValue(null)
                    extract.postValue(result.paginationArtistExtract)
                }
                else{
                    extract.mutation {
                        val list = ArrayList<ArtistExtract>(value!!.results)
                        list.addAll(result.paginationArtistExtract.results)
                        value!!.results = list
                    }
                }

                page++

            }
            is ArtistExtractResult.Failure ->{
            }
        }
    }

    fun onBackClicked(){
        extractArtistRouter.goBack()
    }
}