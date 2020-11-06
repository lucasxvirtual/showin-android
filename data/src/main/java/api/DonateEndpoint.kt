package api

import io.reactivex.Single
import response.PaginationExtractResponse
import response.WorkedResponse
import retrofit2.http.*

interface DonateEndpoint{
    @FormUrlEncoded
    @POST("balanceapp/balance/{id}/donate/")
    fun postBalance(@Path("id") id : Int,
                    @Field("user") user : String,
                    @Field("value") value : Int,
                    @Field ("password") password : String) : Single<WorkedResponse>

    @FormUrlEncoded
    @POST("balanceapp/balance/{id}/donate/")
    fun postDonateShow(@Path("id") id : Int,
                       @Field("show") show : Int,
                       @Field("value") value : Int,
                       @Field ("password") password : String) : Single<WorkedResponse>

    @GET("/balanceapp/transfer/")
    fun getExtract(@Query ("page") page : Int) : Single<PaginationExtractResponse>
}