package br.com.noclaftech.showin.presentation.donatecoins

import android.app.Application
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.usecase.DonateUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class DonateCoinsViewModel @Inject constructor(
    private val donateCoinsRouter: DonateCoinsRouter,
    private val donateUseCase: DonateUseCase,
    application: Application
) : BaseViewModel(application) {

    val value = MutableLiveData<String>().apply { value = "" }
    private val valueError = MutableLiveData<Boolean>().apply { value = false }

    val password = MutableLiveData<String>().apply { value = "" }
    private val passwordError = MutableLiveData<Boolean>().apply { value = false }

    val receiver = MutableLiveData<String>().apply { value = ""}
    private val receiverError = MutableLiveData<Boolean>().apply { value = false }

    private var user = Singleton.instance.getUser()?.blockingGet()

    private val detail = MutableLiveData<String?>().apply { value = null }

    private var show: Int? = null

    fun getDetail() : LiveData<String?> = detail

    fun getValueError(): LiveData<Boolean> = valueError
    fun getReceiverError(): LiveData<Boolean> = receiverError
    fun getPasswordError(): LiveData<Boolean> = passwordError

    fun bound() {

    }

    fun setShowId(show: Int){
        this.show = show
    }

    fun onBackClicked(){
        donateCoinsRouter.resultFinish(Bundle(), false)
    }

    fun donate(){
        valueError.value = value.value.isNullOrBlank()
        receiverError.value = receiver.value.isNullOrBlank()
        passwordError.value = password.value.isNullOrBlank()

        if(valueError.value!!||
            passwordError.value!! ||
            passwordError.value!!)
            return
        if(show == null)
            donateUseCase.execute(user!!.balance!!.id, value.value!!.toInt(), password.value!!, user=receiver.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleDonateResult(it)}
                .addTo(disposables)
        else
            donateUseCase.execute(user!!.balance!!.id, value.value!!.toInt(), password.value!!, show=show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleDonateResult(it)}
                .addTo(disposables)
    }

    private fun handleDonateResult(result : DonateUseCase.Result){
        when(result){
            is DonateUseCase.Result.Success ->{
                hideDialog()
                if (result.worked.worked != null && result.worked.worked!!){
                    setToast(1)
                    donateCoinsRouter.resultFinish(bundleOf(DonateCoinsActivity.AMOUNT to value.value!!.toInt()))
                }
                else{
                    detail.postValue(result.worked.detail)
                }
            }
            is DonateUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is DonateUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

}