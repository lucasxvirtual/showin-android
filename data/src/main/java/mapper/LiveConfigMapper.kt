package mapper

import br.com.noclaftech.domain.model.LiveConfig
import response.LiveConfigResponse
import javax.inject.Inject

class LiveConfigMapper @Inject constructor(private val configMapper: ConfigMapper) {

    fun map(response : LiveConfigResponse) : LiveConfig {
        return LiveConfig(link = response.link,
            config = configMapper.map(response.config))
    }

}