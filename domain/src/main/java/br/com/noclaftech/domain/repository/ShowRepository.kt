package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.*
import io.reactivex.Single


interface ShowRepository{
    fun getShowAndArtist() : Single<ShowAndArtist>

    fun getShow(id : Int) : Single<ShowDetails>

    fun buyTicket(id: Int, value: Float, countTickets: Int) : Single<Ticket>

    fun postShow(name : String, date : String, description : String, duration : Int, ageRating : Int, sendViewerEmail : Boolean, category: Int,ticketLimit : Int?, ticketValue : Int,
                 payWhatYouCan : Boolean,verticalImage : String, horizontalImage : String, position: String, displayViewers: Boolean, liveTest : Boolean) : Single<Show>

    fun getSearch(query: String): Single<Search>

    fun getCategoryShows(id: Int): Single<Pagination<Show>>

    fun createDeepLink(id: Int): Single<DeepLink>

    fun putShow(id: Int,name : String, ageRating : Int, description : String, sendViewerEmail : Boolean, category: Int, verticalImage : String?, horizontalImage : String?,
                position : String, displayViewers : Boolean, setList : String?, liveTest: Boolean) : Single<Show>

    fun deleteShow(id: Int): Single<Unit>

    fun getBuyers(id: Int, page: Int): Single<PaginationFollow>

    fun postShowRevenue(duration: Int, coin_amount: Int, category: Int): Single<CoinsPrice>
}