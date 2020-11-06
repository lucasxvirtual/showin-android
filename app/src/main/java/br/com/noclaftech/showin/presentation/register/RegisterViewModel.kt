package br.com.noclaftech.showin.presentation.register

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.usecase.PostUserUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.GoogleUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread


class RegisterViewModel @Inject constructor(
    private val postUserUseCase: PostUserUseCase,
    private val registerRouter: RegisterRouter,
    application: Application): BaseViewModel(application){

    val username = MutableLiveData<String>().apply { value = null }
    private val usernameError = MutableLiveData<Boolean>().apply { value = false }

    val email = MutableLiveData<String>().apply { value = null }
    private val emailError = MutableLiveData<Boolean>().apply { value = false }

    val name = MutableLiveData<String>().apply { value = null }
    private val nameError = MutableLiveData<Boolean>().apply { value = false }

    val birthday = MutableLiveData<String>().apply { value = null }
    private val birthdayError = MutableLiveData<Boolean>().apply { value = false }

    val gender = MutableLiveData<String>().apply { value = null }
    private val genderError = MutableLiveData<Boolean>().apply { value = false }

    val password = MutableLiveData<String>().apply { value = null }
    private val passwordError = MutableLiveData<Boolean>().apply { value = false }

    val confirmPassword = MutableLiveData<String>().apply { value = null }
    private val confirmPasswordError = MutableLiveData<Boolean>().apply { value = false }

    private val incorrectPasswordConfirmation = MutableLiveData<Boolean>().apply { value = false }

//    private val notImage = MutableLiveData<Boolean>().apply { value = false }
    private val notAccept = MutableLiveData<Boolean>().apply { value = false }
    private val invalidDate = MutableLiveData<Boolean>().apply { value = false }

    private val socialEmail = MutableLiveData<String>().apply { value = null }
    fun getSocialEmail() : LiveData<String> = socialEmail

    private val socialName = MutableLiveData<String>().apply { value = null }
    fun getSocialName() : LiveData<String> = socialName

    val accept = MutableLiveData<Boolean>().apply { value = false }

    val hasSocialToken = MutableLiveData<Boolean>().apply { value = false }

//    private val bitmap = MutableLiveData<Bitmap?>().apply { value = null }
//    fun getBitmap() : LiveData<Bitmap?> = bitmap

    fun callback(jsonObject: JSONObject?) {
        socialEmail.postValue(jsonObject!!["email"].toString())
        socialName.postValue(jsonObject["name"].toString())

        getBitmapFromURL(jsonObject.getJSONObject("picture").getJSONObject("data").get("url").toString())
    }

    fun setResultFacebook(boolean: Boolean){
        hasSocialToken.value = boolean
        FacebookUtil.graphRequest {
            callback(it as JSONObject)
        }
    }

    fun setResultGoogle(boolean: Boolean){
        hasSocialToken.value = boolean
        socialEmail.postValue(GoogleUtil.loginResult.value!!.accessToken!!.email)
        socialName.postValue(GoogleUtil.loginResult.value!!.accessToken!!.displayName)

        getBitmapFromURL(GoogleUtil.loginResult.value!!.accessToken!!.photoUrl.toString())
    }

    fun getUsernameError(): LiveData<Boolean> = usernameError
    fun getEmailError(): LiveData<Boolean> = emailError
    fun getNameError(): LiveData<Boolean> = nameError
    fun getBirthdayError(): LiveData<Boolean> = birthdayError
    fun getGenderError(): LiveData<Boolean> = genderError
    fun getPasswordError(): LiveData<Boolean> = passwordError
    fun getConfirmPasswordError(): LiveData<Boolean> = confirmPasswordError
    fun getIncorrectPasswordConfirmation(): LiveData<Boolean> = incorrectPasswordConfirmation
//    fun getNotImage(): LiveData<Boolean> = notImage
    fun getNotAccept(): LiveData<Boolean> = notAccept
    fun invalidDate(): LiveData<Boolean> = invalidDate

    private var openCamera = MutableLiveData<Boolean>().apply { value = false }
    private var openGender = MutableLiveData<Boolean>().apply { value = false }
    private var openTermsUse = MutableLiveData<Boolean>().apply { value = false }
//    private var image : String? = null
    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail

    fun getOpenGender() : LiveData<Boolean> = openGender
    fun getOpenTermsUse() : LiveData<Boolean> = openTermsUse
    fun getOpenCamera() : LiveData<Boolean> = openCamera

    private var openDate = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenDate() : LiveData<Boolean> = openDate

    fun onRegisterClicked(){

        usernameError.value = username.value.isNullOrBlank()
        emailError.value = email.value.isNullOrBlank()
        nameError.value = name.value.isNullOrBlank()
        birthdayError.value = birthday.value.isNullOrBlank()
        genderError.value = gender.value.isNullOrBlank()
        passwordError.value = password.value.isNullOrBlank()
        confirmPasswordError.value = confirmPassword.value.isNullOrBlank()

        if(usernameError.value!! ||
            emailError.value!! ||
            nameError.value!! ||
            birthdayError.value!! ||
            genderError.value!! ||
            passwordError.value!! ||
            confirmPasswordError.value!!)
            return

//        notImage.value = image.isNullOrBlank()
//        if (notImage.value!!)
//            return

        invalidDate.value = birthday.value!!.length < 10
        if (invalidDate.value!!)
            return

        invalidDate.value = !Helper.validateDate(birthday.value!!)
        if (invalidDate.value!!)
            return

        incorrectPasswordConfirmation.value = password.value != confirmPassword.value
        if (incorrectPasswordConfirmation.value!!)
            return

        notAccept.value = !accept.value!!
        if (notAccept.value!!)
            return

        postUserUseCase.execute(username.value!!, email.value!!, name.value!!, birthday.value!!, password.value!!, Helper.getGenderValue(gender.value!!))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostUserResult(it)}
            .addTo(disposables)
    }

    private fun handlePostUserResult(result : PostUserUseCase.Result){
        when(result){
            is PostUserUseCase.Result.Success ->{
                hideDialog()
                registerRouter.navigate(RegisterRouter.Route.MAIN)
            }
            is PostUserUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }

            is PostUserUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    fun goBack(){
        registerRouter.goBack()
    }

    fun onTermsUseClicked(){
        openTermsUse.postValue(true)
    }

    fun onClickNewImage(){
        openCamera.postValue(true)
    }

    fun setImageThumb(image : String?){
//        this.image = image
//        openCamera.postValue(false)
    }

    fun onGenderClicked(){
        openGender.postValue(true)
    }

    fun setGender(text : String){
        gender.value = text
        openGender.postValue(false)
        genderError.value = false
    }

    fun onBirthDateClicked(){
        openDate.postValue(true)
    }

    fun updateDate(string: String){
        birthday.postValue(string)
        openDate.postValue(false)
    }

    private fun getBitmapFromURL(src: String?) {
//        var input: InputStream? = null
//
//        thread {
//            try {
//                val url = URL(src)
//                val urlConn: URLConnection = url.openConnection()
//                val httpConn = urlConn as HttpURLConnection
//                httpConn.connect()
//                input = httpConn.inputStream
//            } catch (e: MalformedURLException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            bitmap.postValue(BitmapFactory.decodeStream(input))
//        }
    }

    fun onBackClicked(){
        registerRouter.goBack()
    }
}