package api

import io.reactivex.Single
import response.ListTypeTicketsResponse
import response.WorkedResponse
import retrofit2.http.*

interface TicketEndpoint{
    @GET("showapp/ticket/formatted/")
    fun getMyTickets() : Single<ListTypeTicketsResponse>

    @FormUrlEncoded
    @POST("showapp/ticket/donate/")
    fun postDonateTicket(@Field("user") user : String,
                         @Field("show") show : Int,
                         @Field("count") count : Int) : Single<WorkedResponse>

    @DELETE("showapp/ticket/{id}/delete/")
    fun deleteTicket(@Path("id") id: Int) : Single<WorkedResponse>
}