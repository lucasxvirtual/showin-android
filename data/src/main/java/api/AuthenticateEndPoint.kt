package api

import io.reactivex.Single
import response.*
import retrofit2.http.*

interface AuthenticateEndPoint {

    @FormUrlEncoded
    @POST("userapp/auth/")
    fun auth(@Field("username") username : String,
             @Field("password") password : String): Single<TokenResponse>

    @GET("userapp/user/")
    fun getUser() : Single<UserResponse>

    @GET("userapp/user/{id}/")
    fun getUser(@Path("id") id: Int) : Single<UserResponse>

    @POST("showapp/user/{id}/follow/")
    fun postFollow(@Path("id") id : Int) : Single<WorkedResponse>

    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putImage(@Path("id") id: Int,
                 @Field("profile_image") profile_image : String) : Single<UserResponse>

    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putUser(@Path("id") id: Int,
                @Field("token_notification") token : String) : Single<UserResponse>

    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putUser(@Path ("id") id: Int,
                @Field ("name") name : String,
                @Field ("birthday") birthday : String,
                @Field ("gender") gender : String,
                @Field("bio") bio : String,
                @Field("artistic_name") artisticName: String?) : Single<UserResponse>


    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putPassword(@Path ("id") id: Int,
                @Field ("password2") passwordOld : String,
                @Field ("password") passwordNew : String ): Single<UserResponse>

    @FormUrlEncoded
    @POST("/userapp/user/")
    fun postUser(@Field("username") username : String,
                 @Field("email") email : String,
                 @Field("name") name : String,
                 @Field("birthday") birthday: String,
                 @Field("password") password: String,
                 @Field ("gender") gender: String): Single<UserResponse>


    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putUser(@Path("id") id: Int,
                @Field("allow_notification") allow_notification : Boolean,
                @Field("allow_artist_email") allow_artist_email : Boolean,
                @Field("allow_commercial_email") allow_commercial_email : Boolean) : Single<UserResponse>

    @FormUrlEncoded
    @POST("userapp/contact/")
    fun contact(@Field("title") title : String,
                @Field("message") message : String) : Single<ContactResponse>

    @FormUrlEncoded
    @POST("socialnetworkapp/social-network-auth/")
    fun authSocialNetwork(
        @Field("social_network") socialNetwork: String,
        @Field("token") token: String) : Single<TokenResponse>

    @FormUrlEncoded
    @POST("userapp/user/forgot-password/")
    fun forgotPassword (
        @Field("email") email : String) : Single<WorkedResponse>

    @FormUrlEncoded
    @PATCH("userapp/user/{id}/")
    fun putSocials(@Path("id") id: Int,
                   @Field("whatsapp") whatsapp : String?,
                   @Field("instagram") instagram : String?,
                   @Field("facebook") facebook : String?,
                   @Field("linkedin") linkedin : String?,
                   @Field("twitter") twitter : String?) : Single<UserResponse>

    @FormUrlEncoded
    @POST("userapp/report/")
    fun report(@Field("user_reported") userReported : Int,
               @Field("chat_message") chatMessage : String,
               @Field("reason") reason : String) : Single<ReportResponse>
}