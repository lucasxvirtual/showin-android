package br.com.noclaftech.showin.presentation.contact

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.ContactResult
import br.com.noclaftech.domain.usecase.ContactUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactUsViewModel @Inject constructor(
    private val contactUseCase: ContactUseCase,
    private val router: ContactUsRouter,
    application: Application) : BaseViewModel(application){
    val title = MutableLiveData<String>().apply { value = "" }
    private val titleError = MutableLiveData<Boolean>().apply { value = false }

    val message = MutableLiveData<String>().apply { value = "" }
    private val messageError = MutableLiveData<Boolean>().apply { value = false }

    private val showMessage = MutableLiveData<Boolean>().apply { value = false }

    fun getTitleError(): LiveData<Boolean> = titleError
    fun getMessageError(): LiveData<Boolean> = messageError
    fun getShowMessage() : LiveData<Boolean> = showMessage

    fun onSendClicked(){
        titleError.value = title.value.isNullOrBlank()
        messageError.value = message.value.isNullOrBlank()

        if(titleError.value!! ||
            messageError.value!!)
            return

        request()
    }

    fun onBackClicked(){
        router.goBack()
    }

    private fun request(){
        contactUseCase.execute(title.value!!, message.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleContact(it)}
            .addTo(disposables)
    }

    private fun handleContact( result : ContactResult){
        when(result){
            is ContactResult.Success ->{
                hideDialog()
                showMessage.postValue(true)
                router.goBack()
            }
            is ContactResult.Failure ->{
                hideDialog()
            }
            is ContactResult.Loading -> {
                showDialog()
            }
        }
    }
}