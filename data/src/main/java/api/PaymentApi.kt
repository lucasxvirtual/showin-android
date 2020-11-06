package api

import io.reactivex.Single
import response.PaymentResponse
import javax.inject.Inject

class PaymentApi @Inject constructor(private val paymentEndpoint: PaymentEndpoint){

     fun paymentPack(pack : Int, card_holder : String, card_number : String, card_brand : String, card_date : String, card_code : Int, value : Float, countPack : Int, show : Int, typePayment: Int) : Single<PaymentResponse>{
         return paymentEndpoint.paymentPack(pack, card_holder, card_number, card_brand, card_date, card_code, value, countPack, show, typePayment)
     }
}