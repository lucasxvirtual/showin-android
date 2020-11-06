package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.LiveConfig
import br.com.noclaftech.domain.model.Worked
import io.reactivex.Single

interface StreamingRepository {

    fun goLive(id: Int): Single<Worked>

    fun finish(id: Int): Single<Worked>

    fun getConfig(id: Int): Single<LiveConfig>

}