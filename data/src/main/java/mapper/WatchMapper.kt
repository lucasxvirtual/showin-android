package mapper

import br.com.noclaftech.domain.model.Watch
import response.WatchResponse
import javax.inject.Inject

class WatchMapper @Inject constructor(){
    fun map (response : WatchResponse) : Watch{
        return Watch(
           link = response.link
        )
    }
}