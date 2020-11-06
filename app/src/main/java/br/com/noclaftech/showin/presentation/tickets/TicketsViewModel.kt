package br.com.noclaftech.showin.presentation.tickets

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.domain.model.Ticket
import br.com.noclaftech.domain.usecase.DeleteTicketUseCase
import br.com.noclaftech.domain.usecase.DonateTicketsUseCase
import br.com.noclaftech.domain.usecase.GetMyTicketsUseCase
import br.com.noclaftech.domain.usecase.PostUserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class TicketsViewModel @Inject constructor(
    private val getMyTicketsUseCase: GetMyTicketsUseCase,
    private val deleteTicketUseCase: DeleteTicketUseCase,
    private val donateTicketsUseCase: DonateTicketsUseCase,
    private val ticketsRouter: TicketsRouter,
    application: Application): BaseViewModel(application) {

    private val ticketsOld = MutableLiveData<List<Ticket>?>().apply { value = null }
    private val ticketsFuture = MutableLiveData<List<Ticket>?>().apply { value = null }

    private val ticketsCount = MutableLiveData<Map<Int, List<Ticket>>>().apply { value = null }

    private val detail = MutableLiveData<String?>().apply { value = null }

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    fun getTicketsOld(): LiveData<List<Ticket>?> = ticketsOld
    fun getTicketsFuture(): LiveData<List<Ticket>?> = ticketsFuture
    fun getTicketsCount(): LiveData<Map<Int, List<Ticket>>?> = ticketsCount

    fun getDetail() : LiveData<String?> = detail

    fun bound(){
        getMyTickets()
    }

    private fun getMyTickets(){
        getMyTicketsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetMyTickets(it)}
            .addTo(disposables)
    }

    fun deleteTicket(id: Int){
        deleteTicketUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleDeleteTicket(it) }
            .addTo(disposables)
    }

    fun onDonationClick(triple: Triple<String, Int, Int>){
        donateTicketsUseCase.execute(triple.first, triple.second, triple.third)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostUserResult(it)}
            .addTo(disposables)
    }

    override fun onClickItem(item: Any){
        if(item is Ticket){
            ticketsRouter.navigate(TicketsRouter.Route.SHOW_DETAILS, Bundle().apply { putInt(
                ShowDetailsActivity.EXTRA_SHOW, item.show.id) })
        }
    }


    private fun handlePostUserResult(result : DonateTicketsUseCase.Result){
        when(result){
            is DonateTicketsUseCase.Result.Success ->{
                hideDialog()
                getMyTickets()
            }
            is DonateTicketsUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }

            is DonateTicketsUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }
    private fun handleDeleteTicket(result: DeleteTicketUseCase.Result){
        when (result){
            is DeleteTicketUseCase.Result.Success ->{
                getMyTickets()
                detail.postValue(result.worked.detail)
            }
            is DeleteTicketUseCase.Result.Failure ->{
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is DeleteTicketUseCase.Result.Loading ->{
            }
        }
    }

    private fun handleGetMyTickets(result : GetMyTicketsUseCase.Result){
        when(result){
            is GetMyTicketsUseCase.Result.Success ->{
                nonBlockingLoading.postValue(false)

                ticketsOld.postValue(checkTickets(result.listTypeTickets.old))
                ticketsFuture.postValue(checkTickets(result.listTypeTickets.future))
            }
            is GetMyTicketsUseCase.Result.Failure ->{
                nonBlockingLoading.postValue(false)
            }
            is GetMyTicketsUseCase.Result.Loading -> {
                nonBlockingLoading.postValue(true)
            }
        }
    }

    private fun checkTickets(tickets: List<Ticket>) : List<Ticket>{
        val count = tickets.groupBy { it.show.id }
        ticketsCount.postValue(count)

//        count[tickets.show.id]?.size

        return tickets.distinctBy { it.show.id }
    }
}