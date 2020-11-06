package mapper

import br.com.noclaftech.domain.model.Payment
import br.com.noclaftech.domain.model.Report
import response.PaymentResponse
import response.ReportResponse
import javax.inject.Inject

class ReportMapper @Inject constructor(){
    fun map(responseList: List<ReportResponse>) : List<Report>{
        return responseList.map { (map(it)) }
    }

    fun map(response: ReportResponse) : Report {
        return Report(
            id = response.id,
            chatMessage = response.chat_message,
            reason = response.reason,
            createdAt = response.created_at,
            userReported = response.user_reported,
            userReporting = response.user_reporting
        )
    }
}