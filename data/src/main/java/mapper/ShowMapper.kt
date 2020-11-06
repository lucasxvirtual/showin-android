package mapper

import br.com.noclaftech.domain.model.Show
import response.ShowResponse
import javax.inject.Inject

class ShowMapper @Inject constructor(){
    fun map(responseList: List<ShowResponse>?) : List<Show>?{
        if (responseList == null)
            return null
        return responseList.map{(map(it))}
    }

    fun map(response : ShowResponse) : Show{
        return Show(
            id = response.id,
            verticalImage = response.verticalImage,
            horizontalImage = response.horizontalImage,
            artistArtisticName = response.artistArtisticName,
            name = response.name,
            date = response.date,
            dateFinish = response.dateFinish,
            description = response.description,
            ageRating = response.ageRating,
            sendArtistEmail = response.sendArtistEmail,
            sendViewerEmail = response.sendViewerEmail,
            ticketLimit = response.ticketLimit,
            ticketValue = response.ticketValue,
            createdAt = response.created_at,
//            artist = response.artist,
            category = response.category,
            categoryObj = CategoryMapper().map(response.category_obj),
            ageRatingObj = AgeRatingMapper().map(response.ageRatingResponse),
            isPurchased = response.is_purchased,
            ticketSold = response.ticket_sold,
            payWhatYouCan = response.pay_what_you_can,
            status = response.status,
            configTime = response.config_time,
            liveTime = response.live_time,
            doneTime = response.done_time,
            canceledTime = response.canceled_time,
            setList = response.set_list,
            position = response.position,
            displayViewers = response.display_viewers,
            liveTest = response.live_test,
            ticketPrice = response.ticketPrice
        )
    }
}