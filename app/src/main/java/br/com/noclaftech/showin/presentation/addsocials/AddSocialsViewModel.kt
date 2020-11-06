package br.com.noclaftech.showin.presentation.addsocials

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.PutSocialsUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class AddSocialsViewModel @Inject constructor(
    private val router: AddSocialsRouter,
    private val putSocialsUseCase: PutSocialsUseCase,
    application: Application) : BaseViewModel(application) {

    private val user = MutableLiveData<User>().apply { value = Singleton.instance.getUser()?.blockingGet() }

    val whatsapp = MutableLiveData<String?>().apply { value = Singleton.instance.getUser()?.blockingGet()?.whatsapp }
    private val whatsappError = MutableLiveData<Boolean>().apply { value = false }
    fun getWhatsappError(): LiveData<Boolean> = whatsappError

    val instagram = MutableLiveData<String>().apply { value = Singleton.instance.getUser()?.blockingGet()?.instagram }
    private val instagramError = MutableLiveData<Boolean>().apply { value = false }
    fun getInstagramError(): LiveData<Boolean> = instagramError

    val facebook = MutableLiveData<String?>().apply { value = Singleton.instance.getUser()?.blockingGet()?.facebook }
    private val facebookError = MutableLiveData<Boolean>().apply { value = false }
    fun getFacebookError(): LiveData<Boolean> = facebookError

    val linkedin = MutableLiveData<String?>().apply { value = Singleton.instance.getUser()?.blockingGet()?.linkedin }
    private val linkedinError = MutableLiveData<Boolean>().apply { value = false }
    fun getLinkedinError(): LiveData<Boolean> = linkedinError

    val twitter = MutableLiveData<String?>().apply { value = Singleton.instance.getUser()?.blockingGet()?.twitter }
    private val twitterError = MutableLiveData<Boolean>().apply { value = false }
    fun getTwitterError(): LiveData<Boolean> = twitterError

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail


    fun onBackClick(){
        router.goBack()
    }

    fun onSaveClicked(){

        if (!whatsapp.value.isNullOrBlank()){
            whatsappError.value = whatsapp.value!!.length < 15
            if (whatsappError.value!!){
                return
            }
        }

        putSocialsUseCase.execute(user.value!!.id, whatsapp.value, instagram.value, facebook.value, linkedin.value, twitter.value)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutSocialsResult(it)}
            .addTo(disposables)
    }

    private fun handlePutSocialsResult(result : UserResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()
                router.goBack()
            }
            is UserResult.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }

            is UserResult.Loading ->{
                showDialog()
            }
        }
    }
}