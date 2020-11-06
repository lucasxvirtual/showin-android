package api

import io.reactivex.Single
import response.PaymentResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaymentEndpoint{
    @FormUrlEncoded
    @POST("/paymentapp/pack-acquisition/")
    fun paymentPack(@Field("pack") pack : Int,
                    @Field("card_holder") card_holder : String,
                    @Field ("card_number") card_number : String,
                    @Field("card_brand") card_brand : String,
                    @Field("card_date") card_date : String,
                    @Field("card_code") card_code : Int,
                    @Field("value") value : Float,
                    @Field("count_packs") countPack : Int,
                    @Field("show") show : Int,
                    @Field("type_payment") typePayment : Int
                    ) : Single<PaymentResponse>
}