package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.Watch
import br.com.noclaftech.domain.model.Worked
import io.reactivex.Single

interface WatchRepository{
    fun watch(id : Int, deviceId: String) : Single<Watch>

    fun renewWatch(id : Int, deviceId: String) : Single<Worked>

    fun removeWatch(id : Int, deviceId: String) : Single<Worked>
}