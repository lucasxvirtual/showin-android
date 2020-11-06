package repository

import api.WatchApi
import br.com.noclaftech.domain.model.Watch
import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.WatchRepository
import io.reactivex.Single
import mapper.WatchMapper
import mapper.WorkedMapper
import javax.inject.Inject

class WatchRepositoryImpl @Inject constructor(
    private val watchApi: WatchApi,
    private val watchMapper: WatchMapper,
    private val workedMapper: WorkedMapper
): WatchRepository {
    override fun watch(id: Int, deviceId: String): Single<Watch> {
        return watchApi.watch(id, deviceId).map { watchMapper.map(it) }
    }

    override fun renewWatch(id: Int, deviceId: String): Single<Worked> {
        return watchApi.renewWatch(id, deviceId).map { workedMapper.map(it) }
    }

    override fun removeWatch(id: Int, deviceId: String): Single<Worked> {
        return watchApi.removeWatch(id, deviceId).map { workedMapper.map(it) }
    }
}