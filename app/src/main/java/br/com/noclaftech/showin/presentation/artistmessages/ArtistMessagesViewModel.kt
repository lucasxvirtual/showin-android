package br.com.noclaftech.showin.presentation.artistmessages

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.FollowUseCase
import br.com.noclaftech.domain.usecase.UnfollowUseCase
import br.com.noclaftech.showin.ext.mutation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import java.io.Serializable
import javax.inject.Inject

class ArtistMessagesViewModel @Inject constructor(
    private val router : ArtistMessagesRouter,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
    application: Application) : BaseViewModel(application) {

    private val messages = MutableLiveData<List<Message>?>().apply { value = null }
    val user = MutableLiveData<User>().apply { value = Singleton.instance.getUser()?.blockingGet() }
//    var artist = MutableLiveData<Artist>().apply { value = null }

    fun putUser(serializable: Serializable){
        user.value = serializable as User
        messages.value = serializable.userMessages
    }

    fun getMessages(): LiveData<List<Message>?> = messages

    fun onBackClicked(){
        router.goBack()
    }

    fun onClickFollow(){
        if(user.value!!.isFollowed){
            unfollowUseCase.execute(user.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it, isUnfollow = true)}
                .addTo(disposables)
        } else {
            followUseCase.execute(user.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleFollowResult(it)}
                .addTo(disposables)
        }
    }


    private fun handleFollowResult(result: WorkedResult, isUnfollow: Boolean = false){
        when(result){
            is WorkedResult.Success ->{
                user.mutation {
                    if(!isUnfollow) {
                        user.value!!.followers += 1
                        user.value!!.isFollowed = true
                    } else {
                        user. value!!.followers -= 1
                        user.value!!.isFollowed = false
                    }
                }
            }
        }
    }
}