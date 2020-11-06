package api

import io.reactivex.Single
import response.*
import javax.inject.Inject

class ShowApi @Inject constructor(private val showEndpoint: ShowEndpoint){
    fun getShowAndArtistList() : Single<ShowAndArtistResponse>{
        return showEndpoint.getShowAndArtist()
    }

    fun getShow(id : Int) : Single<ShowDetailsResponse>{
        return  showEndpoint.getShowDetails(id)
    }

    fun buyTicket(id: Int, value: Float, countTickets: Int) : Single<TicketResponse>{
        return showEndpoint.buyTicket(id, value, countTickets)
    }

    fun postShow(
        name: String,
        date: String,
        description: String,
        duration: Int,
        ageRating: Int,
        sendViewerEmail: Boolean,
        category: Int,
        ticketLimit: Int?,
        ticketValue: Int,
        payWhatYouCan: Boolean,
        verticalImage: String,
        horizontalImage: String,
        position: String,
        displayViewers: Boolean,
        liveTest: Boolean
    ) : Single<ShowResponse>{
        return showEndpoint.postShow(name, date, description, duration, ageRating, sendViewerEmail, category, ticketLimit, ticketValue, payWhatYouCan, verticalImage, horizontalImage,
            position, displayViewers, liveTest)
    }

    fun getSearch(query: String): Single<SearchResponse>{
        return showEndpoint.getSearch(query)
    }

    fun getCategoryShows(category: Int): Single<PaginationResponse<ShowResponse>>{
        return showEndpoint.getShow(category)
    }

    fun createDeepLink(id: Int): Single<DeepLinkResponse>{
        return showEndpoint.deeplink(id)
    }

    fun putShow(id: Int,name : String, ageRating : Int, description : String, sendViewerEmail : Boolean, category: Int, verticalImage : String?, horizontalImage : String?,
                 position : String, displayViewers : Boolean, setList : String?, liveTest: Boolean) : Single<ShowResponse>{
        return showEndpoint.putShow(id, name, ageRating, description, sendViewerEmail, category, verticalImage, horizontalImage, position, displayViewers, setList, liveTest)
    }

    fun deleteShow(id: Int): Single<Unit> {
        return showEndpoint.delete(id).map { Unit }
    }

    fun getBuyers(id: Int, page : Int) : Single<PaginationFollowResponse>{
        return showEndpoint.getBuyers(id, page)
    }

    fun postShowRevenue(duration: Int, coin_amount: Int, category: Int): Single<CoinsPriceObjResponse>{
        return showEndpoint.postShowRevenue(duration, coin_amount, category)
    }
}