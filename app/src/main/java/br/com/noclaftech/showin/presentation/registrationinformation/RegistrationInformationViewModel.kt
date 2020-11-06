package br.com.noclaftech.showin.presentation.registrationinformation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.EditUserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegistrationInformationViewModel @Inject constructor(
    private val editUserUseCase: EditUserUseCase,
    private val registrationInformationRouter: RegistrationInformationRouter,
    application: Application): BaseViewModel(application){

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }
    private var openGender = MutableLiveData<Boolean>().apply { value = false }

    val username = MutableLiveData<String>().apply { value = user.value!!.username }
    val email = MutableLiveData<String>().apply { value = user.value!!.email }

    val name = MutableLiveData<String>().apply { value = user.value!!.name }
    private val nameError = MutableLiveData<Boolean>().apply { value = false }

    val birthDate = MutableLiveData<String>().apply { value = Helper.convertForApp(user.value!!.birthday!!) }
    private val birthdayError = MutableLiveData<Boolean>().apply { value = false }
    private val invalidDate = MutableLiveData<Boolean>().apply { value = false }

    private val gender = MutableLiveData<String>().apply { value = Helper.getGenderName(user.value!!.gender) }

    val biography = MutableLiveData<String>().apply { value = user.value!!.bio ?: "" }
    private val biographyError = MutableLiveData<Boolean>().apply { value = false }
    fun getBiographyError(): LiveData<Boolean> = biographyError

    val artisticName = MutableLiveData<String?>().apply { value = Singleton.instance.getArtist()?.blockingGet()?.artisticName }
    private val artisticNameError = MutableLiveData<Boolean>().apply { value = false }

    fun getOpenGender() : LiveData<Boolean> = openGender
    fun getGender() = gender.value!!

    fun getNameError(): LiveData<Boolean> = nameError
    fun getBirthdayError(): LiveData<Boolean> = birthdayError
    fun invalidDate(): LiveData<Boolean> = invalidDate

    private var openDate = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenDate() : LiveData<Boolean> = openDate

    fun onBackClicked(){
        registrationInformationRouter.goBack()
    }

    fun onEditClicked(){

        nameError.value = name.value.isNullOrBlank()
        birthdayError.value = birthDate.value.isNullOrBlank()

        if( nameError.value!! ||
            birthdayError.value!!)
            return

        invalidDate.value = birthDate.value!!.count() < 10
        if (invalidDate.value!!)
            return

        invalidDate.value = !Helper.validateDate(birthDate.value!!)
        if (invalidDate.value!!)
            return

        if (user.value?.isArtist!!){
            artisticNameError.value = artisticName.value.isNullOrBlank()
            if (artisticNameError.value!!)
                return
        }

        editUserUseCase.execute(user.value!!.id, name.value!!, birthDate.value!!, Helper.getGenderValue(gender.value!!), biography.value!!, artisticName.value)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleEditUserResult(it)}
            .addTo(disposables)
    }

    fun onGenderClicked(){
        openGender.postValue(true)
    }

    fun setGender(text : String){
        gender.value = text
        openGender.postValue(false)
    }

    fun onBirthDateClicked(){
        openDate.postValue(true)
    }

    fun updateDate(string: String){
        birthDate.postValue(string)
        openDate.postValue(false)
    }

    private fun handleEditUserResult(result : EditUserUseCase.Result){
        when(result){
            is EditUserUseCase.Result.Success ->{
                hideDialog()
                user.postValue(result.user)
                registrationInformationRouter.goBack()
            }
            is EditUserUseCase.Result.Failure ->{
                hideDialog()
            }
            is EditUserUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }
}