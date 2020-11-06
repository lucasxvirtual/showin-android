package br.com.noclaftech.showin.presentation.mymessages

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.MessageResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.PostMessageUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

class MyMessagesViewModel @Inject constructor(
    private val router: MyMessagesRouter,
    private val postMessageUseCase: PostMessageUseCase,
    application: Application) : BaseViewModel(application){

//    private val artist = MutableLiveData<Artist>().apply { value = null }
    private val user = MutableLiveData<User>().apply { value = null }
    private val messages = MutableLiveData<List<Message>>().apply { value = null }
    val message = MutableLiveData<String>().apply { value = "" }
    private val messageError = MutableLiveData<Boolean>().apply { value = false }

    fun getMessageError() : LiveData<Boolean> = messageError

    fun getMessages() : LiveData<List<Message>> = messages

    fun setUser(serializable: Serializable){
        user.value = serializable as User
        messages.value = serializable.userMessages
    }

    fun onBackClicked(){
        router.goBack()
    }

    fun sendMessageClicked(){
        messageError.value = message.value.isNullOrBlank()

        if (messageError.value!!)
            return

        postMessageUseCase.execute(message.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostMessage(it)}
            .addTo(disposables)
    }

    private fun handlePostMessage(result : MessageResult){
        when(result){
            is MessageResult.Success ->{
                val list = arrayListOf<Message>()
                list.add(result.message)
                list.addAll(messages.value!!)

                messages.value = list

                message.value = ""

                hideDialog()
            }
            is MessageResult.Failure ->{
                hideDialog()
            }

            is MessageResult.Loading ->{
                showDialog()
            }
        }
    }
}