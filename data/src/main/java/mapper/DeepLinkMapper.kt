package mapper

import br.com.noclaftech.domain.model.DeepLink
import response.DeepLinkResponse
import javax.inject.Inject

class DeepLinkMapper @Inject constructor(){

    fun map(response : DeepLinkResponse) : DeepLink {
        return DeepLink(
            deeplink = response.deeplink
        )
    }

}