package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.PaymentResult
import br.com.noclaftech.domain.repository.PaymentRepository
import io.reactivex.Observable
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository){

    fun execute(pack : Int, cardHolder : String, cardNumber : String, cardBrand : String, cardDate : String, cardCode : Int, value : Float, countPack : Int, show : Int, typePayment: Int): Observable<PaymentResult> {
        return paymentRepository.paymentPack(pack, cardHolder, cardNumber, cardBrand, cardDate, cardCode, value, countPack, show, typePayment)
            .toObservable()
            .map { PaymentResult.Success(it) as PaymentResult }
            .onErrorReturn { PaymentResult.Failure(it) }
            .startWith(PaymentResult.Loading)
    }
}