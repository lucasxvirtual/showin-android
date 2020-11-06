package br.com.noclaftech.domain

import br.com.noclaftech.domain.model.*

sealed class StringResult {
    object Loading : StringResult()
    data class Success(val string: String?) : StringResult()
    data class Failure(val throwable: Throwable) : StringResult()
}

sealed class ArtistResult {
    object Loading : ArtistResult()
    data class Success(val artist: Artist) : ArtistResult()
    data class Failure(val throwable: Throwable) : ArtistResult()
}

sealed class WorkedResult {
    object Loading : WorkedResult()
    data class Success(val worked: Worked) : WorkedResult()
    data class Failure(val throwable: Throwable) : WorkedResult()
}

sealed class UserResult {
    object Loading : UserResult()
    data class Success(val user: User) : UserResult()
    data class Failure(val throwable: Throwable) : UserResult()
}

sealed class ContactResult {
    object Loading : ContactResult()
    data class Success(val contact: Contact) :ContactResult()
    data class Failure(val throwable: Throwable) : ContactResult()
}

sealed class PaymentResult {
    object Loading : PaymentResult()
    data class Success(val payment: Payment) : PaymentResult()
    data class Failure(val throwable: Throwable) : PaymentResult()
}

sealed class MessageResult {
    object Loading : MessageResult()
    data class Success(val message: Message) : MessageResult()
    data class Failure(val throwable: Throwable) : MessageResult()
}

sealed class ShowResult {
    object Loading : ShowResult()
    data class Success(val show: Show) : ShowResult()
    data class Failure(val throwable: Throwable) : ShowResult()
}

sealed class ShowListResult {
    object Loading : ShowListResult()
    data class Success(val pagination: Pagination<Show>) : ShowListResult()
    data class Failure(val throwable: Throwable) : ShowListResult()
}

sealed class SearchResult {
    object Loading : SearchResult()
    data class Success(val search: Search) : SearchResult()
    data class Failure(val throwable: Throwable) : SearchResult()
}

sealed class LiveConfigResult {
    object Loading : LiveConfigResult()
    data class Success(val liveConfig: LiveConfig) : LiveConfigResult()
    data class Failure(val throwable: Throwable) : LiveConfigResult()
}

sealed class BankResult {
    object Loading : BankResult()
    data class Success(val bank: Bank) : BankResult()
    data class Failure(val throwable: Throwable) : BankResult()
}

sealed class FollowResult {
    object Loading : FollowResult()
    data class Success(val paginationFollow: PaginationFollow) : FollowResult()
    data class Failure(val throwable: Throwable) : FollowResult()
}

sealed class ArtistExtractResult {
    object Loading : ArtistExtractResult()
    data class Success(val paginationArtistExtract: Pagination<ArtistExtract>) : ArtistExtractResult()
    data class Failure(val throwable: Throwable) : ArtistExtractResult()
}

sealed class NotificationResult {
    object Loading : NotificationResult()
    data class Success(val paginationNotification: PaginationNotification) : NotificationResult()
    data class Failure(val throwable: Throwable) : NotificationResult()
}

sealed class DeepLinkResult {
    object Loading : DeepLinkResult()
    data class Success(val deepLink: DeepLink) : DeepLinkResult()
    data class Failure(val throwable: Throwable) : DeepLinkResult()
}

sealed class UnitResult{
    object Loading : UnitResult()
    data class Success(var unit : Unit) : UnitResult()
    data class Failure(val throwable: Throwable) : UnitResult()
}

sealed class ReportResult{
    object Loading : ReportResult()
    data class Success(var report : Report) : ReportResult()
    data class Failure(val throwable: Throwable) : ReportResult()
}