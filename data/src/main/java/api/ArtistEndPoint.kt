package api

import io.reactivex.Single
import response.*
import retrofit2.http.*

interface ArtistEndPoint {
    @POST("userapp/user/{id}/follow/")
    fun postFollow(@Path("id") id : Int) : Single<WorkedResponse>

    @POST("userapp/user/{id}/unfollow/")
    fun postUnfollow(@Path("id") id : Int) : Single<WorkedResponse>

    @FormUrlEncoded
    @POST("showapp/artist/")
    fun postArtist(@Field("artistic_name") artisticName: String,
                   @Field("biography") biography: String): Single<ArtistResponse>

    @FormUrlEncoded
    @PATCH("showapp/artist/{id}/")
    fun patchArtist(@Path("id") id: Int,
                    @Field("artistic_name") artisticName: String,
                    @Field("biography") biography: String,
                    @Field("display_old_shows") displayOldShows: Boolean,
                    @Field("display_fans") displayFans: Boolean): Single<ArtistResponse>


    @GET("showapp/artist/{id}/")
    fun getArtist(@Path ("id") id : Int) : Single<ArtistResponse>

    @FormUrlEncoded
    @POST("showapp/artist/")
    fun changeArtistAccount(@Field ("artistic_name") artisticName : String,
                            @Field ("bank_name") bankName : String,
                            @Field ("owner") owner : String,
                            @Field ("agency") agency : String,
                            @Field ("account") account : String,
                            @Field ("cpf") cpf : String?,
                            @Field ("cnpj") cnpj : String?,
                            @Field ("account_type") accountType : String) : Single<ArtistResponse>


    @FormUrlEncoded
    @POST("userapp/user-message/")
    fun postMessage(@Field ("message") message : String) : Single<MessageResponse>

    @FormUrlEncoded
    @PATCH("showapp/bank/{id}/")
    fun putBank(@Path ("id") id: Int,
                @Field("owner") owner: String,
                @Field("bank") bank : String,
                @Field("agency") agency : String,
                @Field("account") account : String,
                @Field("cpf") cpf : String?,
                @Field("cnpj") cnpj : String?,
                @Field("account_type") accountType: String) : Single<BankResponse>

    @GET("showapp/artist/{id}/transfers")
    fun getExtractArtist(@Path("id") id: Int): Single<PaginationResponse<ArtistExtractResponse>>

    @GET("showapp/bank/")
    fun getBank() : Single<BankResponse>
}