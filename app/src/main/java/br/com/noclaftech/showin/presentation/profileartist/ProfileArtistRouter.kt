package br.com.noclaftech.showin.presentation.profileartist

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.artistmoreoptions.ArtistMoreOptionsActivity
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.mymessages.MyMessagesActivity
import br.com.noclaftech.showin.presentation.mysocials.MySocialsActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.usernotification.UserNotificationActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import java.lang.ref.WeakReference

class ProfileArtistRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        MORE,
        FOLLOWERS,
        MESSAGES,
        WATCH,
        SHOW_DETAILS,
        MY_SOCIALS,
        NOTIFICATIONS
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.WATCH -> {showNextScreen(WatchActivity::class.java, bundle)}
            Route.SHOW_DETAILS -> { showNextScreen(ShowDetailsActivity::class.java, bundle) }
            Route.MESSAGES -> { showNextScreen(MyMessagesActivity::class.java, bundle) }
            Route.MORE -> { showNextScreen(ArtistMoreOptionsActivity::class.java, bundle)}
            Route.FOLLOWERS -> { showNextScreen(FollowersActivity::class.java, bundle)}
            Route.MY_SOCIALS -> {showNextScreen(AddSocialsActivity::class.java, bundle)}
            Route.NOTIFICATIONS -> {showNextScreen(UserNotificationActivity::class.java, bundle)}
        }
    }
}