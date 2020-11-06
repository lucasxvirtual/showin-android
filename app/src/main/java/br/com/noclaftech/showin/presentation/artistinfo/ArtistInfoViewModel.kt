package br.com.noclaftech.showin.presentation.artistinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.EditArtistUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class ArtistInfoViewModel @Inject constructor(
    private val editArtistUseCase: EditArtistUseCase,
    private val artistInfoRouter: ArtistInfoRouter,
    application: Application): BaseViewModel(application){

    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }
    private var user = MutableLiveData<User>().apply { value = null }

    val artisticName = MutableLiveData<String>().apply { value = artist.value!!.artisticName }
    private val artisticNameError = MutableLiveData<Boolean>().apply { value = false }

    val biography = MutableLiveData<String>().apply { value = user.value!!.bio }
    private val biographyError = MutableLiveData<Boolean>().apply { value = false }

    //TODO Faltando displayOldShows e displayFans
//    val oldShowsDisplay = MutableLiveData<Boolean>().apply { value = artist.value!!.displayOldShows }
//    val fansDisplay = MutableLiveData<Boolean>().apply { value = artist.value!!.displayFans }

    private val success = MutableLiveData<Boolean>().apply { value = false }
    private val detail = MutableLiveData<String?>().apply { value = null }

    fun getArtisticNameError(): LiveData<Boolean> = artisticNameError
    fun getBiographyError(): LiveData<Boolean> = biographyError

    fun getSuccess(): LiveData<Boolean> = success
    fun getDetail() : LiveData<String?> = detail

    fun onSaveClicked(){

        artisticNameError.value = artisticName.value.isNullOrBlank()
        biographyError.value = biography.value.isNullOrBlank()

        if (artisticNameError.value!! ||
            biographyError.value!!)
            return

        request()
    }

//    TODO Esperar para saber se o oldShowsDisplay e o fansDisplay vão ficar no User ou no Artist
    private fun request(){
//        editArtistUseCase.execute(artist.value!!.id, artisticName.value!!, biography.value!!, oldShowsDisplay.value!!, fansDisplay.value!!)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {handleEditArtistResult(it)}
//            .addTo(disposables)
    }

    private fun handleEditArtistResult(result : EditArtistUseCase.Result){
        when(result){
            is EditArtistUseCase.Result.Success ->{
                hideDialog()
                success.postValue(true)
            }
            is EditArtistUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is EditArtistUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    fun onBackClicked(){
        artistInfoRouter.goBack()
    }
}