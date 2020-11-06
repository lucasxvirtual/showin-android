package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.PaginationExtract
import br.com.noclaftech.domain.model.Worked
import io.reactivex.Single

interface DonateRepository{
    fun postDonate(id : Int, user : String, value : Int, password : String): Single<Worked>

    fun postDonateShow(id: Int, show: Int, value: Int, password: String): Single<Worked>

    fun getExtract(page : Int) : Single<PaginationExtract>
}