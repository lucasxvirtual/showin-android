package mapper

import br.com.noclaftech.domain.model.ShowDetails
import response.ShowDetailsResponse
import javax.inject.Inject

class ShowDetailsMapper @Inject constructor(){

    fun map(response: ShowDetailsResponse) : ShowDetails{
        return ShowDetails(
            id = response.id,
            artist = ArtistMapper().map(response.artist),
            name = response.name,
            date = response.date,
            dateFinish = response.dateFinish,
            description = response.description,
            ageRating = response.ageRating,
            sendViewerEmail = response.sendViewerEmail,
            sendArtistEmail = response.sendArtistEmail,
            ticketValue = response.ticketValue,
            ticketLimit = response.ticketLimit,
            createdAt = response.created_at,
            category = response.category,
            categoryObj = CategoryMapper().map(response.category_obj),
            verticalImage = response.verticalImage,
            horizontalImage = response.horizontalImage,
            ageRatingObg = AgeRatingMapper().map(response.ageRatingObj),
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