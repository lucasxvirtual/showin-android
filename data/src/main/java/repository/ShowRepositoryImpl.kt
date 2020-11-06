package repository

import api.ShowApi
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Single
import mapper.*
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val showApi : ShowApi,
    private val showAndArtistMapper: ShowAndArtistMapper,
    private val showDetailsMapper: ShowDetailsMapper,
    private val showMapper: ShowMapper,
    private val searchMapper: SearchMapper,
    private val ticketMapper: TicketMapper,
    private val paginationShowMapper: PaginationShowMapper,
    private val deepLinkMapper: DeepLinkMapper,
    private val coinsPriceMapper: CoinsPriceMapper,
    private val paginationFollowMapper: PaginationFollowMapper): ShowRepository {

    override fun getShowAndArtist(): Single<ShowAndArtist> {
        return showApi.getShowAndArtistList().map { showAndArtistMapper.map(it) }
    }

    override fun getShow(id: Int) : Single<ShowDetails>{
        return showApi.getShow(id).map { showDetailsMapper.map(it) }
    }

    override fun buyTicket(id: Int, value: Float, countTickets: Int): Single<Ticket> {
        return showApi.buyTicket(id, value, countTickets).map { ticketMapper.map(it) }
    }

    override fun postShow(name: String, date : String, description: String, duration: Int,ageRating: Int,sendViewerEmail: Boolean,category: Int,ticketLimit: Int?,ticketValue: Int,
                          payWhatYouCan: Boolean, verticalImage: String, horizontalImage: String, position: String, displayViewers: Boolean, liveTest: Boolean): Single<Show> {
        return showApi.postShow(name, date, description, duration, ageRating, sendViewerEmail, category, ticketLimit, ticketValue, payWhatYouCan, verticalImage, horizontalImage,
            position, displayViewers, liveTest).map { showMapper.map(it) }
    }

    override fun getSearch(query: String): Single<Search> {
        return showApi.getSearch(query).map { searchMapper.map(it) }
    }

    override fun getCategoryShows(id: Int): Single<Pagination<Show>> {
        return showApi.getCategoryShows(id).map { paginationShowMapper.map(it) }
    }

    override fun putShow(id: Int,name : String, ageRating : Int, description : String, sendViewerEmail : Boolean, category: Int, verticalImage : String?, horizontalImage : String?,
                         position : String, displayViewers : Boolean, setList : String?, liveTest: Boolean) : Single<Show>{
        return showApi.putShow(id, name, ageRating, description, sendViewerEmail, category, verticalImage, horizontalImage, position, displayViewers, setList, liveTest).map { showMapper.map(it) }
    }

    override fun createDeepLink(id: Int): Single<DeepLink> {
        return showApi.createDeepLink(id).map { deepLinkMapper.map(it) }
    }

    override fun deleteShow(id: Int): Single<Unit> {
        return showApi.deleteShow(id)
    }

    override fun getBuyers(id: Int, page: Int): Single<PaginationFollow> {
        return showApi.getBuyers(id, page).map { paginationFollowMapper.map(it) }
    }

    override fun postShowRevenue(duration: Int, coin_amount: Int, category: Int): Single<CoinsPrice> {
        return showApi.postShowRevenue(duration, coin_amount, category).map { coinsPriceMapper.map(it) }
    }
}