package br.com.noclaftech.showin.presentation.changepassword

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.ChangePasswordUseCase
import io.reactivex.rxkotlin.addTo
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val changePasswordRouter: ChangePasswordRouter,
    application: Application
) : BaseViewModel(application) {

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }

    val newPassword = MutableLiveData<String>().apply { value = null }
    private val newPasswordError = MutableLiveData<Boolean>().apply { value = false }

    val confirmNewPassword = MutableLiveData<String>().apply { value = null }
    private val confirmPasswordError = MutableLiveData<Boolean>().apply { value = false }

    val currentPassword = MutableLiveData<String>().apply { value = null }
    private val currentPasswordError = MutableLiveData<Boolean>().apply { value = false }

    private val incorrectPasswordConfirmation = MutableLiveData<Boolean>().apply { value = false }

    private val success = MutableLiveData<Boolean>().apply { value = false }
    private val detail = MutableLiveData<String?>().apply { value = null }


    fun getPasswordError(): LiveData<Boolean> = newPasswordError
    fun confirmPasswordError(): LiveData<Boolean> = confirmPasswordError
    fun currentPasswordError(): LiveData<Boolean> = currentPasswordError
    fun getIncorrectPasswordConfirmation(): LiveData<Boolean> = incorrectPasswordConfirmation
    fun getSuccess(): LiveData<Boolean> = success
    fun getDetail() : LiveData<String?> = detail


    fun onEditClick(){

        newPasswordError.value = newPassword.value.isNullOrBlank()
        confirmPasswordError.value = confirmNewPassword.value.isNullOrBlank()
        currentPasswordError.value = currentPassword.value.isNullOrBlank()

        if(newPasswordError.value!! ||
            confirmPasswordError.value!! ||
            currentPasswordError.value!!)
            return

        incorrectPasswordConfirmation.value = newPassword.value != confirmNewPassword.value
        if (incorrectPasswordConfirmation.value!!)
            return

        changePasswordUseCase.execute(user.value!!.id, currentPassword.value!! ,newPassword.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutPasswordResult(it)}
            .addTo(disposables)
    }

    private fun handlePutPasswordResult(result : ChangePasswordUseCase.Result){
        when(result){
            is ChangePasswordUseCase.Result.Success ->{
                hideDialog()
                success.postValue(true)
            }
            is ChangePasswordUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is ChangePasswordUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    fun onBackClicked(){
        changePasswordRouter.goBack()
    }
}