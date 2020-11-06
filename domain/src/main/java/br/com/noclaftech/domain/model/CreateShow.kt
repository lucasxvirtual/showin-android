package br.com.noclaftech.domain.model

import java.io.Serializable

//Apenas para o fluxo de agendar um show
class CreateShow : Serializable {
    var name : String? = null
    var date : String? = null
    var description : String? = null
    var duration : Int? = null
    var ageRating : Int? = null
    var sendViewerEmail : Boolean? = null
    var category: Int? = null
    var ticketLimit : Int? = null
    var ticketValue : Int? = null
    var payWhatYouCan : Boolean = false
    var verticalImage : String? = null
    var horizontalImage : String? = null
    var position: String? = null
    var displayViewers : Boolean = false
    var setList : String? = null
    var liveTest : Boolean = false
}