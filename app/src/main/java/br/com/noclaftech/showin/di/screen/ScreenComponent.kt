package br.com.noclaftech.showin.di.screen

import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.di.scope.PerScreen
import br.com.noclaftech.showin.presentation.about.AboutActivity
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsActivity
import br.com.noclaftech.showin.presentation.artistaccount.ArtistAccountActivity
import br.com.noclaftech.showin.presentation.artistinfo.ArtistInfoActivity
import br.com.noclaftech.showin.presentation.artistmessages.ArtistMessagesActivity
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.buycoins.BuyCoinsActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.category.CategoryActivity
import br.com.noclaftech.showin.presentation.changeartistaccountstep1.ChangeArtistAccountStep1Activity
import br.com.noclaftech.showin.presentation.changeartistaccountstep2.ChangeArtistAccountStep2Activity
import br.com.noclaftech.showin.presentation.changepassword.ChangePasswordActivity
import br.com.noclaftech.showin.presentation.contact.ContactUsActivity
import br.com.noclaftech.showin.presentation.donatecoins.DonateCoinsActivity
import br.com.noclaftech.showin.presentation.extract.ExtractActivity
import br.com.noclaftech.showin.presentation.extractartist.ExtractArtistActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.forgotpassword.ForgotPasswordActivity
import br.com.noclaftech.showin.presentation.home.HomeFragment
import br.com.noclaftech.showin.presentation.paymentinfo.PaymentInfoActivity
import br.com.noclaftech.showin.presentation.login.LoginActivity
import br.com.noclaftech.showin.presentation.artistmoreoptions.ArtistMoreOptionsActivity
import br.com.noclaftech.showin.presentation.mymessages.MyMessagesActivity
import br.com.noclaftech.showin.presentation.streaming.OtherToolActivity
import br.com.noclaftech.showin.presentation.payment.PaymentActivity
import br.com.noclaftech.showin.presentation.paymentsuccess.PaymentSuccessActivity
import br.com.noclaftech.showin.presentation.profile.ProfileFragment
import br.com.noclaftech.showin.presentation.profileartist.ProfileArtistFragment
import br.com.noclaftech.showin.presentation.profilemoreoptions.ProfileMoreOptionsActivity
import br.com.noclaftech.showin.presentation.register.RegisterActivity
import br.com.noclaftech.showin.presentation.registrationinformation.RegistrationInformationActivity
import br.com.noclaftech.showin.presentation.sale.SaleFragment
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep1Activity
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep2Activity
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep3Activity
import br.com.noclaftech.showin.presentation.search.SearchActivity
import br.com.noclaftech.showin.presentation.settings.SettingsActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.shows.ShowsFragment
import br.com.noclaftech.showin.presentation.artistsocials.ArtistSocialsActivity
import br.com.noclaftech.showin.presentation.buyers.BuyersActivity
import br.com.noclaftech.showin.presentation.confirmbuyticket.ConfirmBuyTicketActivity
import br.com.noclaftech.showin.presentation.editshow.EditShowActivity
import br.com.noclaftech.showin.presentation.mysocials.MySocialsActivity
import br.com.noclaftech.showin.presentation.mysocialsfragment.MySocialsFragment
import br.com.noclaftech.showin.presentation.report.ReportActivity
import br.com.noclaftech.showin.presentation.splash.SplashActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.tickets.TicketsFragment
import br.com.noclaftech.showin.presentation.tutorials.TutorialsActivity
import br.com.noclaftech.showin.presentation.usernotification.UserNotificationActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import br.com.noclaftech.showin.presentation.watch.WatchFragment
import br.com.noclaftech.showin.presentation.watch.WatchFullScreenFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [ScreenModule::class])
interface ScreenComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(showDetailsActivity: ShowDetailsActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(buyTicketActivity: BuyTicketActivity)
    fun inject(ticketsFragment: TicketsFragment)
    fun inject(extractActivity: ExtractActivity)
    fun inject(extractArtistActivity: ExtractArtistActivity)
    fun inject(saleFragment: SaleFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(donateCoinsActivity: DonateCoinsActivity)
    fun inject(registrationInformationActivity: RegistrationInformationActivity)
    fun inject(watchActivity: WatchActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(changeArtistAccountStep1Activity: ChangeArtistAccountStep1Activity)
    fun inject(changeArtistAccountStep2Activity: ChangeArtistAccountStep2Activity)
    fun inject(aboutActivity: AboutActivity)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(contactUsActivity: ContactUsActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(buyCoinsActivity: BuyCoinsActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(paymentSuccessActivity: PaymentSuccessActivity)
    fun inject(artistProfileActivity: ArtistProfileActivity)
    fun inject(scheduleShowStep1Activity: ScheduleShowStep1Activity)
    fun inject(scheduleShowStep2Activity: ScheduleShowStep2Activity)
    fun inject(scheduleShowStep2Activity: ScheduleShowStep3Activity)
    fun inject(showsFragment: ShowsFragment)
    fun inject(artistMessagesActivity: ArtistMessagesActivity)
    fun inject(artistAccountActivity: ArtistAccountActivity)
    fun inject(profileArtistFragment: ProfileArtistFragment)
    fun inject(myMessagesActivity: MyMessagesActivity)
    fun inject(artistMoreOptionsActivity: ArtistMoreOptionsActivity)
    fun inject(tutorialsActivity: TutorialsActivity)
    fun inject(followersActivity: FollowersActivity)
    fun inject(changePasswordActivity: ChangePasswordActivity)
    fun inject(artistInfoActivity: ArtistInfoActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(paymentInfoActivity: PaymentInfoActivity)
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
    fun inject(categoryActivity: CategoryActivity)
    fun inject(streamingActivity: StreamingActivity)
    fun inject(otherToolActivity: OtherToolActivity)
    fun inject(profileMoreOptionsActivity: ProfileMoreOptionsActivity)
    fun inject(addSocialsActivity: AddSocialsActivity)
    fun inject(userNotificationActivity: UserNotificationActivity)
    fun inject(artistSocialsActivity: ArtistSocialsActivity)
    fun inject(mySocialsActivity: MySocialsActivity)
    fun inject(watchFragment: WatchFragment)
    fun inject(watchFullScreenFragment: WatchFullScreenFragment)
    fun inject(mySocialsFragment: MySocialsFragment)
    fun inject(editShowActivity: EditShowActivity)
    fun inject(reportActivity: ReportActivity)
    fun inject(buyersActivity: BuyersActivity)
    fun inject(confirmBuyTicketActivity: ConfirmBuyTicketActivity)
}