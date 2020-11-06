package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.Payment
import rx.Single

interface PaymentRepository{

    fun paymentPack(pack : Int, cardHolder : String, cardNumber : String, cardBrand : String, cardDate : String, cardCode : Int, value : Float, countPack : Int, show : Int, typePayment: Int): io.reactivex.Single<Payment>
}