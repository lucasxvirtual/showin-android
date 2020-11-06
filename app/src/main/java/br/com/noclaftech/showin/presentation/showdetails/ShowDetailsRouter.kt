package br.com.noclaftech.showin.presentation.showdetails

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.buyers.BuyersActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.editshow.EditShowActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.login.LoginActivity
import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.presentation.shows.ShowsRouter
import br.com.noclaftech.showin.presentation.streaming.OtherToolActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import java.lang.ref.WeakReference

class ShowDetailsRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        BUY_TICKET,
        WATCH,
        FOLLOWERS,
        ARTIST,
        EDIT,
        LOGIN,
        MAIN,
        OTHER_TOOL,
        STREAM,
        BUYERS
    }

    fun goBack(userLogged: Boolean) {
        if(activityRef.get()?.intent?.action != null)
            if(!userLogged)
                navigate(Route.LOGIN)
            else
                navigate(Route.MAIN)
        else
            goBack()
    }

    override fun goBack() {
        activityRef.get()?.finish()
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.BUY_TICKET -> { showNextScreen(BuyTicketActivity::class.java, bundle)}
            Route.WATCH -> { showNextScreen(WatchActivity::class.java, bundle)}
            Route.FOLLOWERS -> { showNextScreen(FollowersActivity::class.java, bundle)}
            Route.ARTIST -> { showNextScreen(ArtistProfileActivity::class.java, bundle)}
            Route.EDIT -> { showNextScreen(EditShowActivity::class.java, bundle) }
            Route.LOGIN -> { showNextScreenClearTask(LoginActivity::class.java, bundle) }
            Route.MAIN -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
            Route.STREAM -> { showNextScreen(StreamingActivity::class.java, bundle) }
            Route.OTHER_TOOL -> { showNextScreen(OtherToolActivity::class.java, bundle) }
            Route.BUYERS -> { showNextScreen(BuyersActivity::class.java, bundle)}
        }
    }
}