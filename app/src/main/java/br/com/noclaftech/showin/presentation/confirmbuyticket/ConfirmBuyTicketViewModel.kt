package br.com.noclaftech.showin.presentation.confirmbuyticket

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.ShowDetails
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.presentation.util.Helper
import java.io.Serializable
import javax.inject.Inject

class ConfirmBuyTicketViewModel @Inject constructor(application: Application,
                                                    private val router: ConfirmBuyTicketRouter
                                                    ): BaseViewModel(application) {

    private val showDetails = MutableLiveData<ShowDetails>().apply { value = null }
    private val showHome = MutableLiveData<Show>().apply { value = null }

    fun setExtraShow(serializable: Serializable){
        val it = serializable as ShowDetails
        showDetails.value =  it
    }

    fun setExtraShowHome(serializable: Serializable){
        val it = serializable as Show
        showHome.value = it
    }

    fun onClickFinish(){
        router.navigate(ConfirmBuyTicketRouter.Route.MY_TICKETS, Bundle().apply { putString(MainActivity.BUY_TICKET, MainActivity.BUY_TICKET) })
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

    fun getImage() : String?{
        return if (showDetails.value != null){
            showDetails.value!!.verticalImage
        } else{
            showHome.value!!.verticalImage
        }
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

    fun onBackClicked(){
        router.navigate(ConfirmBuyTicketRouter.Route.MY_TICKETS)
    }

}