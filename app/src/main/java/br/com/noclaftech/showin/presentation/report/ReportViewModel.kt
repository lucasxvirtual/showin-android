package br.com.noclaftech.showin.presentation.report

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ContactResult
import br.com.noclaftech.domain.ReportResult
import br.com.noclaftech.domain.SearchResult
import br.com.noclaftech.domain.usecase.ReportUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReportViewModel @Inject constructor(private val router: ReportRouter,
                                          private val reportUseCase: ReportUseCase,
                                          application: Application) : BaseViewModel(application){

    val report = MutableLiveData<String>().apply { value = "" }

    private val reportError = MutableLiveData<Boolean>().apply { value = false }
    fun getReportError(): LiveData<Boolean> = reportError

    private val showMessage = MutableLiveData<Boolean>().apply { value = false }
    fun getShowMessage() : LiveData<Boolean> = showMessage

    private val userReport = MutableLiveData<Int>().apply { value = null }
    private val chatMessage = MutableLiveData<String>().apply { value = "" }

    fun onBackClicked(){
        router.goBack()
    }

    fun setInfos(id : Int, message : String){
        userReport.postValue(id)
        chatMessage.postValue(message)
    }

    fun onReportClicked(){
        reportError.value = report.value.isNullOrBlank()

        if (reportError.value!!)
            return

        reportUseCase.execute(userReport.value!!, chatMessage.value!!, report.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleReportResult(it)}
            .addTo(disposables)
    }

    private fun handleReportResult(result : ReportResult){

        when(result){
            is ReportResult.Success ->{
                hideDialog()
                showMessage.postValue(true)
                router.goBack()
                hideDialog()
            }
            is ReportResult.Failure ->{
                hideDialog()
            }
            is ReportResult.Loading -> {
                showDialog()
            }
        }
    }
}