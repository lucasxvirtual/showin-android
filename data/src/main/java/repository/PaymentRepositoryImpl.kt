package repository

import api.PaymentApi
import br.com.noclaftech.domain.model.Payment
import br.com.noclaftech.domain.repository.PaymentRepository
import io.reactivex.Single
import mapper.PaymentMapper
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentApi: PaymentApi,
    private val paymentMapper: PaymentMapper) : PaymentRepository {

    override fun paymentPack(pack: Int, cardHolder: String, cardNumber: String, cardBrand: String, cardDate: String, cardCode: Int, value : Float, countPack : Int, show : Int, typePayment: Int): Single<Payment> {
        return paymentApi.paymentPack(pack, cardHolder, cardNumber, cardBrand, cardDate, cardCode, value, countPack, show, typePayment).map { paymentMapper.map( it ) }
    }
}