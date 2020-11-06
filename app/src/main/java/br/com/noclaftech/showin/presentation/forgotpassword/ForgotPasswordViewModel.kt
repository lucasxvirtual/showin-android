package br.com.noclaftech.showin.presentation.forgotpassword

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.usecase.ForgotPasswordUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val forgotPasswordRouter: ForgotPasswordRouter,
    application: Application) : BaseViewModel(application) {

    val email = MutableLiveData<String>().apply {value = null}
    private val emailError = MutableLiveData<Boolean>().apply {value = false}

    private var success = MutableLiveData<Boolean>().apply { value = false }
    private var detail = MutableLiveData<String?>().apply { value = null }

    fun getEmailError(): LiveData<Boolean> = emailError

    fun getSuccess() : LiveData<Boolean> = success
    fun getDetail() : LiveData<String?> = detail

    fun onSendClick(){

        emailError.value = email.value.isNullOrBlank()

        if (emailError.value!!)
            return

        forgotPasswordUseCase.execute(email.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleForgotPasswordResult(it)}
            .addTo(disposables)

    }

    private fun handleForgotPasswordResult(result : ForgotPasswordUseCase.Result){
        when (result){
            is ForgotPasswordUseCase.Result.Success ->{
                hideDialog()
                onBackClicked()
                success.postValue(true)
            }
            is ForgotPasswordUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }
        }
    }

    fun onBackClicked(){
        forgotPasswordRouter.goBack()
    }
}