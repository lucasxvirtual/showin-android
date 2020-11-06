package br.com.noclaftech.showin.di.application

import api.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class EndpointModule {

    @Provides
    @Singleton
    fun provideListTypeShowEndpoint(retrofit: Retrofit) : ShowEndpoint {
        return retrofit.create(ShowEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthEndpoint(retrofit: Retrofit) : AuthenticateEndPoint{
        return retrofit.create(AuthenticateEndPoint::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketEndpoint(retrofit: Retrofit) : TicketEndpoint{
        return retrofit.create(TicketEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideDonateEndpoint(retrofit: Retrofit) : DonateEndpoint{
        return retrofit.create(DonateEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideConstantsEndpoint(retrofit: Retrofit) : ConstantsEndpoint{
        return retrofit.create(ConstantsEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideWatchEndpoint(retrofit: Retrofit) : WatchEndpoint{
        return retrofit.create(WatchEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideArtistEndpoint(retrofit: Retrofit) : ArtistEndPoint{
        return retrofit.create(ArtistEndPoint::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentEndpoint(retrofit: Retrofit) : PaymentEndpoint{
        return retrofit.create(PaymentEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideStreamingEndpoint(retrofit: Retrofit) : StreamingEndPoint {
        return retrofit.create(StreamingEndPoint::class.java)
    }

    @Provides
    @Singleton
    fun provideFollowEndpoint(retrofit: Retrofit) : FollowEndpoint{
        return retrofit.create(FollowEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationEndpoint(retrofit: Retrofit) : NotificationEndpoint{
        return retrofit.create(NotificationEndpoint::class.java)
    }
}