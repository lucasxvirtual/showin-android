package repository

import api.StreamingApi
import br.com.noclaftech.domain.model.LiveConfig
import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.StreamingRepository
import io.reactivex.Single
import mapper.LiveConfigMapper
import mapper.WorkedMapper
import javax.inject.Inject

class StreamingRepositoryImpl @Inject constructor(
    private val streamingApi : StreamingApi,
    private val workedMapper: WorkedMapper,
    private val liveConfigMapper: LiveConfigMapper
    ): StreamingRepository {

    override fun goLive(id: Int): Single<Worked> {
        return streamingApi.goLive(id).map { workedMapper.map(it) }
    }

    override fun finish(id: Int): Single<Worked> {
        return streamingApi.finish(id).map { workedMapper.map(it) }
    }

    override fun getConfig(id: Int): Single<LiveConfig> {
        return streamingApi.getConfig(id).map { liveConfigMapper.map(it) }
    }
}