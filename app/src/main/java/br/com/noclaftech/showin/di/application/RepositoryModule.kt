package br.com.noclaftech.showin.di.application

import api.*
import br.com.noclaftech.domain.repository.*
import dagger.Module
import dagger.Provides
import mapper.*
import repository.*
import storage.SessionManager
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideListTypeShowRepository(showApi: ShowApi, showAndArtistMapper: ShowAndArtistMapper, showDetailsMapper: ShowDetailsMapper, showMapper: ShowMapper,  ticketMapper: TicketMapper, searchMapper: SearchMapper, paginationShowMapper: PaginationShowMapper, deepLinkMapper: DeepLinkMapper, coinsPriceMapper: CoinsPriceMapper, paginationFollowMapper: PaginationFollowMapper) : ShowRepository {
        return ShowRepositoryImpl(showApi, showAndArtistMapper, showDetailsMapper, showMapper, searchMapper, ticketMapper, paginationShowMapper, deepLinkMapper,coinsPriceMapper ,paginationFollowMapper)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthenticateApi, authMapper: AuthMapper, workedMapper: WorkedMapper, contactMapper: ContactMapper, reportMapper: ReportMapper, sessionManager: SessionManager) : AuthRepository{
        return AuthRepositoryImpl(authApi, authMapper, workedMapper, contactMapper, reportMapper, sessionManager)
    }

    @Provides
    @Singleton
    fun provideTicketRepository(ticketApi: TicketApi, workedMapper: WorkedMapper, listTypeTicketsMapper: ListTypeTicketsMapper) : TicketRepository{
        return TicketImpl(ticketApi, workedMapper, listTypeTicketsMapper)
    }

    @Provides
    @Singleton
    fun provideDonateRepository(donateApi: DonateApi, workedMapper: WorkedMapper, paginationExtractMapper : PaginationExtractMapper) : DonateRepository{
        return DonateImpl(donateApi, workedMapper, paginationExtractMapper)
    }

    @Provides
    @Singleton
    fun provideConstantsRepository(constantsApi: ConstantsApi, constansMapper: ConstansMapper) : ConstantsRepository{
        return ConstantsImpl(constantsApi, constansMapper)
    }

    @Provides
    @Singleton
    fun provideWatchRepository(watchApi: WatchApi, watchMapper: WatchMapper, workedMapper: WorkedMapper) : WatchRepository{
        return WatchRepositoryImpl(watchApi, watchMapper, workedMapper)
    }

    @Provides
    @Singleton
    fun provideArtistRepository(artistApi: ArtistApi, workedMapper: WorkedMapper, artistMapper: ArtistMapper, bankMapper: BankMapper, messageMapper: MessageMapper, paginationArtistExtractMapper: PaginationArtistExtractMapper) : ArtistRepository{
        return ArtistRepositoryImpl(artistApi, workedMapper, artistMapper, bankMapper, messageMapper, paginationArtistExtractMapper)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(paymentApi: PaymentApi, paymentMapper: PaymentMapper) : PaymentRepository{
        return PaymentRepositoryImpl(paymentApi, paymentMapper)
    }

    @Provides
    @Singleton
    fun provideStreamingRepository(streamingApi: StreamingApi, workedMapper: WorkedMapper, liveConfigMapper: LiveConfigMapper) : StreamingRepository {
        return StreamingRepositoryImpl(streamingApi, workedMapper, liveConfigMapper)
    }

    @Provides
    @Singleton
    fun provideFollowRepository(followApi: FollowApi, paginationFollowMapper: PaginationFollowMapper) : FollowRepository{
        return FollowRepositoryImpl(followApi, paginationFollowMapper)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationApi: NotificationApi, paginationNotificationMapper: PaginationNotificationMapper) : NotificationRepository{
        return NotificationImpl(notificationApi, paginationNotificationMapper)
    }
}