package br.com.noclaftech.showin.presentation.artistprofile

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistmessages.ArtistMessagesActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.artistsocials.ArtistSocialsActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import java.lang.ref.WeakReference

class ArtistProfileRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SHOW_DETAILS,
        BUY,
        WATCH,
        SOCIALS,
        ARTIST_MESSAGES,
        FOLLOWERS
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SHOW_DETAILS -> { showNextScreen(ShowDetailsActivity::class.java, bundle) }
            Route.BUY -> { showNextScreen(BuyTicketActivity::class.java, bundle) }
            Route.WATCH -> { showNextScreen(WatchActivity::class.java, bundle) }
            Route.SOCIALS -> {showNextScreen(ArtistSocialsActivity::class.java, bundle)}
            Route.ARTIST_MESSAGES -> { showNextScreen(ArtistMessagesActivity::class.java, bundle) }
            Route.FOLLOWERS -> { showNextScreen(FollowersActivity::class.java, bundle) }
        }
    }
}