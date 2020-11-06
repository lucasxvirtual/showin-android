package br.com.noclaftech.showin.presentation.payment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.PaymentResult
import br.com.noclaftech.domain.model.Pack
import br.com.noclaftech.domain.usecase.BuyTicketUseCase
import br.com.noclaftech.domain.usecase.PaymentUseCase
import br.com.noclaftech.showin.presentation.util.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.io.Serializable
import javax.inject.Inject

class PaymentViewModel @Inject constructor(
    private val router: PaymentRouter,
    private val paymentUseCase: PaymentUseCase,
    application: Application) : BaseViewModel(application){

    val cardHolder = MutableLiveData<String>().apply { value = "" }
    private val cardHolderError = MutableLiveData<Boolean>().apply { value = false }

    val cardNumber = MutableLiveData<String>().apply { value = "" }
    private val cardNumberError = MutableLiveData<Boolean>().apply { value = false }

    val cardBrand = MutableLiveData<String>().apply { value = "" }
    private val cardBrandError = MutableLiveData<Boolean>().apply { value = false }

    val cardDate = MutableLiveData<String>().apply { value = "" }
    private val cardDateError = MutableLiveData<Boolean>().apply { value = false }
    val cardDateWrongFormat = MutableLiveData<Boolean>().apply { value = false }

    val cardCode = MutableLiveData<String>().apply { value = "" }
    private val cardCodeError = MutableLiveData<Boolean>().apply { value = false }

    private var openBrand = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenBrand() : LiveData<Boolean> = openBrand

    private val detail = MutableLiveData<String?>().apply { value = null }

    private var show : Int? = null
    private var countPack : Int? = null

    val value = MutableLiveData<Float?>().apply { value = null }

    private val paymentLog = MutableLiveData<Boolean>().apply { value = false }
    fun getPaymentLog() : LiveData<Boolean> = paymentLog

    fun getDetail() : LiveData<String?> = detail

    fun getCardHolderError(): LiveData<Boolean> = cardHolderError
    fun getCardNumberError(): LiveData<Boolean> = cardNumberError
    fun getCardBrandError(): LiveData<Boolean> = cardBrandError
    fun getCardDateError(): LiveData<Boolean> = cardDateError
    fun getCardCodeError(): LiveData<Boolean> = cardCodeError
    fun getCardDateWrongFormat(): LiveData<Boolean> = cardDateWrongFormat


    fun bound(value: Float, show: Int, countPack : Int){
        this.show = show
        this.value.postValue(value)
        this.countPack = countPack
    }

    fun onBackClicked(){
        router.goBack()
    }

    fun onBuyClicked(){
        cardHolderError.value = cardHolder.value.isNullOrBlank()
        cardNumberError.value = cardNumber.value.isNullOrBlank()
        cardBrandError.value = cardBrand.value.isNullOrBlank()
        cardDateError.value = cardDate.value.isNullOrBlank()
        cardCodeError.value = cardCode.value.isNullOrBlank()


        if (cardDate.value?.count()!! != 7){
            cardDateWrongFormat.value = true
        }

        if(cardHolderError.value!! ||
            cardNumberError.value!! ||
            cardBrandError.value!! ||
            cardDateError.value!! ||
            cardCodeError.value!! ||
            cardDateError.value!! ||
            cardDateWrongFormat.value!!)
            return

        request()
    }

    private fun request(){
        val brand = Helper.getBrandApiName(cardBrand.value!!)

        paymentUseCase.execute(1, cardHolder.value!!, cardNumber.value!!.replace(" ", ""), brand, cardDate.value!!, cardCode.value!!.toInt(), value.value!!, countPack!!, show!!, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePaymentResult(it)}
            .addTo(disposables)
    }

    private fun handlePaymentResult(result : PaymentResult){
        paymentLog.postValue(true)
        when(result){
            is PaymentResult.Success ->{
                hideDialog()
                router.navigate(PaymentRouter.Route.PAYMENT_SUCESS)
            }
            is PaymentResult.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }
            is PaymentResult.Loading ->{
                showDialog()
            }
        }
    }

    fun onBrandClicked(){
        openBrand.postValue(true)
    }

    fun setBrand(text : String){
        cardBrand.value = text
        openBrand.postValue(false)
        cardBrandError.value = false
    }
}