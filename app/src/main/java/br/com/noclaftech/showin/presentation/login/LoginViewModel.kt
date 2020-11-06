package br.com.noclaftech.showin.presentation.login

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.usecase.*
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookResult
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.GoogleResult
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.GoogleUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val getAuthUseCase: AuthUseCase,
    private val loginRouter: LoginRouter,
    private val logoutUseCase: LogoutUseCase,
    private val socialNetworkUseCase: SocialNetworkUseCase,
    application: Application): BaseViewModel(application){

    val username = MutableLiveData<String>().apply { value = "" }
    val password = MutableLiveData<String>().apply { value = "" }
    val remember = MutableLiveData<Boolean>().apply { value = false }

    private val usernameError = MutableLiveData<Boolean>().apply { value = false }
    private val passwordError = MutableLiveData<Boolean>().apply { value = false }

    fun getUsernameError() : LiveData<Boolean> = usernameError
    fun getPasswordError() : LiveData<Boolean> = passwordError

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail

    fun bound() {
        logoutUseCase.execute()
    }

    fun onLoginClicked(){
        usernameError.value = username.value.isNullOrBlank()
        passwordError.value = password.value.isNullOrBlank()

        if (usernameError.value!! ||
                passwordError.value!!)
            return

        getAuthUseCase.execute(username.value!!, password.value!!, remember.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleLoginResult(it)}
            .addTo(disposables)
    }

    fun onForgotPasswordClicked(){
        loginRouter.navigate(LoginRouter.Route.FORGOT_PASSWORD)
    }

    fun onRegisterClicked(){
        loginRouter.navigate(LoginRouter.Route.REGISTER)
    }

    fun loginSocialNetworkFacebook(resultFacebook: FacebookResult, socialNetwork: String){
        socialNetworkUseCase.execute(resultFacebook.accessToken!!.accessToken.token, socialNetwork, remember.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleSocialNetworkFacebookResult(it, resultFacebook)}
            .addTo(disposables)
    }

    fun loginSocialNetworkGoogle(resultGoogle: GoogleResult, socialNetwork: String){
        socialNetworkUseCase.execute(resultGoogle.accessToken!!.idToken!!, socialNetwork, remember.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleSocialNetworkGoogleResult(it, resultGoogle)}
            .addTo(disposables)
    }

    private fun handleSocialNetworkFacebookResult(result : UserResult, resultFacebook: FacebookResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()
                loginRouter.navigate(LoginRouter.Route.MAIN, Bundle())
                FacebookUtil.clearLoginResult()
            }
            is UserResult.Failure ->{
                hideDialog()

                loginRouter.navigate(LoginRouter.Route.REGISTER, Bundle().apply { putBoolean("loginFacebook", true) })
            }
            is UserResult.Loading -> {
                showDialog()
            }
        }
    }

    private fun handleSocialNetworkGoogleResult(result : UserResult, resultgoogle: GoogleResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()
                loginRouter.navigate(LoginRouter.Route.MAIN, Bundle())
                GoogleUtil.clearGoogleResult()
            }
            is UserResult.Failure ->{
                hideDialog()

                loginRouter.navigate(LoginRouter.Route.REGISTER, Bundle().apply { putBoolean("loginGoogle", true) })
            }
            is UserResult.Loading -> {
                showDialog()
            }
        }
    }

    private fun handleLoginResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                hideDialog()
                loginRouter.navigate(LoginRouter.Route.MAIN, Bundle())
            }
            is UserUseCase.Result.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())
                if(json.has("detail"))
                    detail.postValue(json["detail"].toString())
                else
                    setToast(message.AUTH_ERROR.value)
            }
            is UserUseCase.Result.Loading -> {
                showDialog()
            }
        }
    }

    enum class message(val value: Int){
        AUTH_ERROR(1)
    }

}