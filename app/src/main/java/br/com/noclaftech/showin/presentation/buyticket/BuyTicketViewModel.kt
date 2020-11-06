package br.com.noclaftech.showin.presentation.buyticket

import android.app.Application
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.ShowDetails
import br.com.noclaftech.domain.usecase.BuyTicketUseCase
import br.com.noclaftech.showin.presentation.confirmbuyticket.ConfirmBuyTicketActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import java.io.Serializable
import javax.inject.Inject

class BuyTicketViewModel @Inject constructor(
    private val buyTicketRouter: BuyTicketRouter,
    private val buyTicketUseCase: BuyTicketUseCase,
    application: Application) : BaseViewModel(application){

    private val showDetails = MutableLiveData<ShowDetails>().apply { value = null }
    private val showHome = MutableLiveData<Show>().apply { value = null }

    val value = MutableLiveData<String>().apply { value = "0" }

    val numberOfTickets = MutableLiveData<String>().apply { value = "1" }

    private val invalidBalance = MutableLiveData<Boolean>().apply { value = false }
    private val detail = MutableLiveData<String>().apply { value = null }
    private val invalidValue = MutableLiveData<Float?>().apply { value = null }
    private val invalidQuantity = MutableLiveData<Boolean>().apply { value = false }

    val config = Singleton.instance.getConstants()?.blockingGet()?.config

    fun getInvalidBalance() : LiveData<Boolean> = invalidBalance
    fun getDetail() : LiveData<String?> = detail
    fun getInvalidValue() : LiveData<Float?> = invalidValue
    fun getInvalidQuantity() : LiveData<Boolean> = invalidQuantity

    val user = Singleton.instance.getUser()?.blockingGet()

    fun setExtraShow(serializable: Serializable){
        val it = serializable as ShowDetails
        showDetails.value =  it
        value.value = String.format("%.2f", it.ticketPrice).replace(".", ",")
    }

    fun checkPayWhatYouCan(): Boolean {
        return if (showDetails.value != null){
            showDetails.value!!.payWhatYouCan
        } else{
            showHome.value!!.payWhatYouCan
        }
    }

    fun setExtraShowHome(serializable: Serializable){
        val it = serializable as Show
        showHome.value = it
        value.value = String.format("%.2f", it.ticketPrice).replace(".", ",")
    }

    fun getArtistName() : String{
        return if (showDetails.value != null){
            showDetails.value!!.artist.artisticName
        } else{
            showHome.value!!.artistArtisticName
        }
    }

    fun getShowName() : String{
        return if (showDetails.value != null){
            showDetails.value!!.name
        } else{
            showHome.value!!.name
        }
    }

    fun getPrice() : Float{
        return if (showDetails.value != null){
            showDetails.value!!.ticketPrice!!
        } else{
            showHome.value!!.ticketPrice!!
        }
    }

    fun onBuyCoinClicked(){
        buyTicketRouter.navigate(BuyTicketRouter.Route.BUY_COINS)
    }

    fun getDate() : String{
        return if (showDetails.value != null){
           Helper.getDateShow(showDetails.value!!.date)
        } else{
            Helper.getDateShow(showHome.value!!.date)
        }
    }

    fun getHour() : String{
        return if (showDetails.value != null){
            "${Helper.getHour(showDetails.value!!.date)} - ${Helper.getHour(showDetails.value!!.dateFinish)}"
        } else{
            "${Helper.getHour(showHome.value!!.date)} - ${Helper.getHour(showHome.value!!.dateFinish)}"
        }
    }

    fun getAgeRating() : String{
        return if (showDetails.value != null){
            showDetails.value!!.ageRatingObg.image
        } else{
            showHome.value!!.ageRatingObj.image
        }
    }

    fun getImage() : String?{
        return if (showDetails.value != null){
            showDetails.value!!.verticalImage
        } else{
            showHome.value!!.verticalImage
        }
    }

    fun updateValue(){
        if(numberOfTickets.value.isNullOrEmpty()) {
            value.postValue("0")
            return
        }
        val tickets = numberOfTickets.value!!.toInt()
        value.postValue(String.format("%.2f", tickets * getPrice()).replace(".", ","))
    }

    fun onBuyClicked(){

        if (numberOfTickets.value.isNullOrBlank()){
            invalidQuantity.postValue(true)
            return
        }

        if(value.value!!.replace(",", ".").toFloat() < config?.minimumPriceReal!! * numberOfTickets.value!!.toInt()){
            invalidValue.postValue(config.minimumPriceReal!! * numberOfTickets.value!!.toInt())
            return
        }

        val id = if (showDetails.value != null){
            showDetails.value!!.id
        }
        else{
            showHome.value!!.id
        }

        buyTicketRouter.navigate(BuyTicketRouter.Route.BUY_COINS, bundleOf("show" to id,
            "value" to value.value!!.replace(",", ".").toFloat(),
            "countPack" to numberOfTickets.value!!.toInt()))

//        buyTicketUseCase.execute(id, value.value!!.replace(",", ".").toFloat(), numberOfTickets.value!!.toInt())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {handleBuyTicketResult(it)}
//            .addTo(disposables)
    }

    fun onBackClicked(){
        buyTicketRouter.goBack()
    }

    private fun handleBuyTicketResult(result : BuyTicketUseCase.Result){
        when(result){
            is BuyTicketUseCase.Result.Success ->{
                hideDialog()
                buyTicketRouter.navigate( BuyTicketRouter.Route.CONFIRM_BUY_TICKET, Bundle().apply {
                    if(showDetails.value != null)
                        putSerializable(ConfirmBuyTicketActivity.EXTRA_SHOW, showDetails.value)

                    if(showHome.value != null)
                        putSerializable(ConfirmBuyTicketActivity.EXTRA_SHOW_HOME, showHome.value)
                } )
            }
            is BuyTicketUseCase.Result.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is BuyTicketUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    enum class ToastMessage(val value: Int) {
        SOON(1)
    }
}