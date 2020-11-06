package api

import io.reactivex.Single
import okhttp3.ResponseBody
import response.*
import retrofit2.http.*

interface ShowEndpoint{
    @GET("showapp/home/")
    fun getShowAndArtist() : Single<ShowAndArtistResponse>

    @GET("/showapp/show/{id}")
    fun getShowDetails(@Path("id") id : Int) : Single<ShowDetailsResponse>

    @FormUrlEncoded
    @POST("/showapp/show/{id}/buy/")
    fun buyTicket(@Path("id") id : Int,
                  @Field("value") value: Float,
                  @Field("count_tickets") countTickets: Int) : Single<TicketResponse>

    @FormUrlEncoded
    @POST("/showapp/show/")
    fun postShow(@Field ("name") name : String,
                 @Field ("date") date: String,
                 @Field ("description") description : String,
                 @Field ("duration") duration : Int,
                 @Field ("age_rating") ageRating : Int,
                 @Field ("send_viewer_email") sendViewerEmail : Boolean,
                 @Field ("category") category: Int,
                 @Field ("ticket_limit") ticketLimit : Int?,
                 @Field ("ticket_value") ticketValue : Int,
                 @Field ("pay_what_you_can") payWhatYouCan : Boolean,
                 @Field ("vertical_image") verticalImage : String,
                 @Field ("horizontal_image") horizontalImage : String,
                 @Field ("position") position : String,
                 @Field ("display_viewers") displayViewers: Boolean,
                 @Field ("live_test") liveTest: Boolean) : Single<ShowResponse>

    @FormUrlEncoded
    @PATCH("/showapp/show/{id}/")
    fun putShow(@Path("id") id: Int,
                @Field ("name") name : String,
                @Field ("age_rating") ageRating : Int,
                @Field ("description") description : String,
                @Field ("send_viewer_email") sendViewerEmail : Boolean,
                @Field ("category") category: Int,
                @Field ("vertical_image") verticalImage : String?,
                @Field ("horizontal_image") horizontalImage : String?,
                @Field ("position") position : String,
                @Field("display_viewers") displayViewers : Boolean,
                @Field("set_list") setList : String?,
                @Field("live_test") liveTest: Boolean) : Single<ShowResponse>

    @GET("showapp/search/")
    fun getSearch(@Query("q") query: String) : Single<SearchResponse>

    @GET("showapp/show/")
    fun getShow(@Query("category") category: Int) : Single<PaginationResponse<ShowResponse>>

    @GET("showapp/deeplink/")
    fun deeplink(@Query("id") id: Int) : Single<DeepLinkResponse>

    @DELETE("showapp/show/{id}/")
    fun delete(@Path("id") id: Int) : Single<ResponseBody>

    @GET("showapp/show/{id}/buyers")
    fun getBuyers(@Path("id") id: Int,
                     @Query("page") page : Int) : Single<PaginationFollowResponse>

    @FormUrlEncoded
    @POST("/showapp/show-revenue/")
    fun postShowRevenue(@Field("duration") duration: Int,
                        @Field("coin_amount") coin_amount: Int,
                        @Field("category") category: Int): Single<CoinsPriceObjResponse>
}