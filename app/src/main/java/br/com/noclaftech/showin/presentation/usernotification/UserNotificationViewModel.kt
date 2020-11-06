package br.com.noclaftech.showin.presentation.usernotification

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.NotificationResult
import br.com.noclaftech.domain.UnitResult
import br.com.noclaftech.domain.model.Notification
import br.com.noclaftech.domain.usecase.DeleteAllNotificationsUseCase
import br.com.noclaftech.domain.usecase.DeleteNotificationUseCase
import br.com.noclaftech.domain.usecase.GetNotificationUseCase
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserNotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
    private val router: UserNotificationRouter,
    application: Application) : BaseViewModel(application) {
    private var page : Int = 1
    var isLastPage : Boolean = true
    val pageSize = 30

    private val notification = MutableLiveData<List<Notification>>().apply { value = null }
    fun getNotification(): LiveData<List<Notification>> = notification

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    private val alert = MutableLiveData<Boolean>().apply { value = false }
    fun getAlert() : LiveData<Boolean> = alert

    fun onBackClicked(){
        router.goBack()
    }

    fun bound(){
        requestGetNotification()
    }

    fun actionSwipe(){
        page = 1
        bound()
    }

    fun clickDeleteAll(){
        alert.postValue(true)
    }

    fun deleteAll(){
        deleteAllNotificationsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleNotificationDeleteResult(it)}
            .addTo(disposables)
    }

    fun deleteNotification(id : Int){
        deleteNotificationUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleNotificationDeleteResult(it)}
            .addTo(disposables)
    }

    private fun requestGetNotification(){
        getNotificationUseCase.execute(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleNotificationResult(it)}
            .addTo(disposables)
    }

    private fun handleNotificationDeleteResult(result : UnitResult){
        when(result){
            is UnitResult.Loading ->{
                showDialog()
            }
            else -> {
                hideDialog()
                page = 1
                requestGetNotification()
            }
        }
    }

    private fun handleNotificationResult(result : NotificationResult){
        nonBlockingLoading.postValue(result == NotificationResult.Loading)
        when(result){
            is NotificationResult.Success ->{
                isLastPage = result.paginationNotification.next != null

                if (page == 1){
                    notification.postValue(result.paginationNotification.results)
                }
                else{
                    notification.mutation {
                        val l = ArrayList(value!!)
                        l.addAll(result.paginationNotification.results)
                        value = l
                    }
                }
                page++
            }
            is NotificationResult.Failure ->{

            }
        }
    }
}