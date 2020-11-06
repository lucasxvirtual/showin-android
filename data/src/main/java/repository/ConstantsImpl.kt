package repository

import api.ConstantsApi
import br.com.noclaftech.domain.model.Constants
import br.com.noclaftech.domain.repository.ConstantsRepository
import io.reactivex.Single
import mapper.ConstansMapper
import storage.Singleton
import javax.inject.Inject

class ConstantsImpl @Inject constructor(
    private val constantsApi: ConstantsApi,
    private val constantsMapper: ConstansMapper
) : ConstantsRepository{

    override fun getConstants(): Single<Constants> {
        return constantsApi.getConstants().map { constantsMapper.map(it) }
    }

    override fun saveConstants(constants: Constants) {
        Singleton.instance.setConstants(constants)
    }

    override fun setPopupDate(date: String) {
        constantsApi.setPopup(date)
    }

    override fun getPopupDate(): Single<String?> {
        return constantsApi.getPopup()
    }
}