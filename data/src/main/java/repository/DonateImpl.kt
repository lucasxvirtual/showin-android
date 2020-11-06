package repository

import api.DonateApi
import br.com.noclaftech.domain.model.PaginationExtract
import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.DonateRepository
import io.reactivex.Single
import mapper.PaginationExtractMapper
import mapper.WorkedMapper
import javax.inject.Inject

class DonateImpl @Inject constructor(
    private val donateApi: DonateApi,
    private val workedMapper: WorkedMapper,
    private val paginationExtractMapper: PaginationExtractMapper) : DonateRepository{

    override fun postDonate(id: Int, user: String, value: Int, password: String) : Single<Worked> {
        return donateApi.postDonate(id, user, value, password).map { workedMapper.map(it) }
    }

    override fun postDonateShow(id: Int, show: Int, value: Int, password: String): Single<Worked> {
        return donateApi.postDonateShow(id, show, value, password).map { workedMapper.map(it) }
    }

    override fun getExtract(page: Int): Single<PaginationExtract> {
        return donateApi.getExtract(page).map { paginationExtractMapper.map(it) }
    }
}