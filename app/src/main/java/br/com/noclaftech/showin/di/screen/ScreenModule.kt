package br.com.noclaftech.showin.di.screen

import br.com.noclaftech.showin.di.scope.PerScreen
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.about.AboutRouter
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsRouter
import br.com.noclaftech.showin.presentation.artistaccount.ArtistAccountRouter
import br.com.noclaftech.showin.presentation.artistinfo.ArtistInfoRouter
import br.com.noclaftech.showin.presentation.artistmessages.ArtistMessagesRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileRouter
import br.com.noclaftech.showin.presentation.buycoins.BuyCoinsRouter
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketRouter
import br.com.noclaftech.showin.presentation.category.CategoryRouter
import br.com.noclaftech.showin.presentation.changeartistaccountstep1.ChangeArtistAccountStep1Router
import br.com.noclaftech.showin.presentation.changeartistaccountstep2.ChangeArtistAccountStep2Router
import br.com.noclaftech.showin.presentation.changepassword.ChangePasswordRouter
import br.com.noclaftech.showin.presentation.contact.ContactUsRouter
import br.com.noclaftech.showin.presentation.donatecoins.DonateCoinsRouter
import br.com.noclaftech.showin.presentation.extract.ExtractRouter
import br.com.noclaftech.showin.presentation.extractartist.ExtractArtistRouter
import br.com.noclaftech.showin.presentation.followers.FollowersRouter
import br.com.noclaftech.showin.presentation.forgotpassword.ForgotPasswordRouter
import br.com.noclaftech.showin.presentation.home.HomeRouter
import br.com.noclaftech.showin.presentation.login.LoginRouter
import br.com.noclaftech.showin.presentation.artistmoreoptions.ArtistMoreOptionsRouter
import br.com.noclaftech.showin.presentation.mymessages.MyMessagesRouter
import br.com.noclaftech.showin.presentation.payment.PaymentRouter
import br.com.noclaftech.showin.presentation.paymentinfo.PaymentInfoRouter
import br.com.noclaftech.showin.presentation.paymentsuccess.PaymentSuccessRouter
import br.com.noclaftech.showin.presentation.profile.ProfileRouter
import br.com.noclaftech.showin.presentation.profileartist.ProfileArtistRouter
import br.com.noclaftech.showin.presentation.profilemoreoptions.ProfileMoreOptionsRouter
import br.com.noclaftech.showin.presentation.register.RegisterRouter
import br.com.noclaftech.showin.presentation.registrationinformation.RegistrationInformationRouter
import br.com.noclaftech.showin.presentation.sale.SaleRouter
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowRouter
import br.com.noclaftech.showin.presentation.search.SearchRouter
import br.com.noclaftech.showin.presentation.settings.SettingsRouter
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsRouter
import br.com.noclaftech.showin.presentation.shows.ShowsRouter
import br.com.noclaftech.showin.presentation.artistsocials.ArtistSocialsRouter
import br.com.noclaftech.showin.presentation.buyers.BuyersRouter
import br.com.noclaftech.showin.presentation.confirmbuyticket.ConfirmBuyTicketRouter
import br.com.noclaftech.showin.presentation.editshow.EditShowRouter
import br.com.noclaftech.showin.presentation.main.MainRouter
import br.com.noclaftech.showin.presentation.mysocials.MySocialsRouter
import br.com.noclaftech.showin.presentation.report.ReportRouter
import br.com.noclaftech.showin.presentation.splash.SplashRouter
import br.com.noclaftech.showin.presentation.streaming.StreamingRouter
import br.com.noclaftech.showin.presentation.tickets.TicketsRouter
import br.com.noclaftech.showin.presentation.tutorials.TutorialsRouter
import br.com.noclaftech.showin.presentation.usernotification.UserNotificationRouter
import br.com.noclaftech.showin.presentation.watch.WatchRouter
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
class ScreenModule(private val activity: BaseActivity) {

    //    @PerScreen
    //    @Provides
    //    fun getApplication() : Application{
    //        return application
    //    }

    @PerScreen
    @Provides
    fun providesActivity(): BaseActivity {
        return activity
    }

    @PerScreen
    @Provides
    fun providesLoginRouter(): LoginRouter{
        return LoginRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesShowDetailsRouter(): ShowDetailsRouter{
        return ShowDetailsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesHomeRouter(): HomeRouter {
        return HomeRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesSaleRouter(): SaleRouter {
        return SaleRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesProfileRouter(): ProfileRouter {
        return ProfileRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesExtractArtistRouter(): ExtractArtistRouter {
        return ExtractArtistRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesExtractRouter(): ExtractRouter {
        return ExtractRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesDonateCoinsRouter(): DonateCoinsRouter {
        return DonateCoinsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesBuyTicketRouter(): BuyTicketRouter {
        return BuyTicketRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesRegistrationInformationRouter(): RegistrationInformationRouter {
        return RegistrationInformationRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesWatchRouter(): WatchRouter {
        return WatchRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesTicketsRouter(): TicketsRouter {
        return TicketsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesRegisterRouter(): RegisterRouter {
        return RegisterRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesChangeArtistAccountStep1Router(): ChangeArtistAccountStep1Router {
        return ChangeArtistAccountStep1Router(WeakReference(activity))
    }
    @PerScreen
    @Provides
    fun providesChangeArtistAccountStep2Router(): ChangeArtistAccountStep2Router {
        return ChangeArtistAccountStep2Router(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesAboutRouter(): AboutRouter {
        return AboutRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesSettingsRouter(): SettingsRouter {
        return SettingsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesContactUsRouter(): ContactUsRouter {
        return ContactUsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesSplashRouter(): SplashRouter {
        return SplashRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesBuyCoinsRouter(): BuyCoinsRouter {
        return BuyCoinsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesPaymentRouter(): PaymentRouter {
        return PaymentRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesPaymentSuccessRouter(): PaymentSuccessRouter {
        return PaymentSuccessRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesArtistProfileRouter(): ArtistProfileRouter {
        return ArtistProfileRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesScheduleShowRouter(): ScheduleShowRouter {
        return ScheduleShowRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesShowsRouter(): ShowsRouter {
        return ShowsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesArtistMessagesRouter(): ArtistMessagesRouter {
        return ArtistMessagesRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesArtistAccountRouter(): ArtistAccountRouter {
        return ArtistAccountRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesProfileArtistRouter(): ProfileArtistRouter {
        return ProfileArtistRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesMyMessagesRouter(): MyMessagesRouter {
        return MyMessagesRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesArtistMoreOptionsRouter(): ArtistMoreOptionsRouter {
        return ArtistMoreOptionsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesTutorialsRouter(): TutorialsRouter {
        return TutorialsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesFollowersRouter(): FollowersRouter {
        return FollowersRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesChangePasswordRouter(): ChangePasswordRouter {
        return ChangePasswordRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesArtistInfoRouter(): ArtistInfoRouter {
        return ArtistInfoRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesSearchRouter(): SearchRouter {
        return SearchRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesPaymentInfoRouter(): PaymentInfoRouter {
        return PaymentInfoRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesForgotPasswordRouter(): ForgotPasswordRouter {
        return ForgotPasswordRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesCategoryRouter(): CategoryRouter {
        return CategoryRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesStreamingRouter(): StreamingRouter {
        return StreamingRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesProfileMoreOptionsRouter(): ProfileMoreOptionsRouter {
        return ProfileMoreOptionsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesAddSocialsRouter(): AddSocialsRouter {
        return AddSocialsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesUserNotificationRouter(): UserNotificationRouter {
        return UserNotificationRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesSocialsRouter(): ArtistSocialsRouter {
        return ArtistSocialsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesMySocialsRouter(): MySocialsRouter {
        return MySocialsRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesEditShowRouter(): EditShowRouter {
        return EditShowRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesReportRouter(): ReportRouter {
        return ReportRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesBuyersRouter(): BuyersRouter {
        return BuyersRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesConfirmBuyTicketRouter(): ConfirmBuyTicketRouter {
        return ConfirmBuyTicketRouter(WeakReference(activity))
    }

    @PerScreen
    @Provides
    fun providesMainRouter(): MainRouter {
        return MainRouter(WeakReference(activity))
    }
}