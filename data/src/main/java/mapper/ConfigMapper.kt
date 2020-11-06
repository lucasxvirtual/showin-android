package mapper

import br.com.noclaftech.domain.model.Config
import response.ConfigResponse
import javax.inject.Inject

class ConfigMapper @Inject constructor(){

    fun map(response : List<ConfigResponse>) : List<Config>{
        return response.map { (map(it)) }
    }

    fun map(response : ConfigResponse) : Config {
        return Config(
            id = response.id,
            about = response.about,
            talkToUsEmail = response.talkToUsEmail,
            privacyPolicy = response.privacyPolicy,
            useTerms = response.useTerms,
            liveTutorialDoc = response.liveTutorialDoc,
            minutesToConfig = response.minutesToConfig,
            minutesToShutDown = response.minutesToShutDown,
            minutesToCancel = response.minutesToCancel,
            coinPrice = response.coinPrice,
            howToCreateShow = response.how_to_create_show,
            streamTips = response.stream_tips,
            marketingTips = response.marketing_tips,
            minimunPrice = response.minimun_price,
            artistPercentage = response.artistPercentage,
            minimumPriceReal = response.minimumPriceReal
        )
    }
}