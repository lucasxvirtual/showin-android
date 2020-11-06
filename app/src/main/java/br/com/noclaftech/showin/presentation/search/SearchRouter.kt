package br.com.noclaftech.showin.presentation.search

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.category.CategoryActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import java.lang.ref.WeakReference

class SearchRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        BACK,
        LIVE,
        ARTIST,
        CATEGORY
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.BACK -> { goBack() }
            Route.LIVE -> showNextScreen(ShowDetailsActivity::class.java, bundle)
            Route.ARTIST -> showNextScreen(ArtistProfileActivity::class.java, bundle)
            Route.CATEGORY -> showNextScreen(CategoryActivity::class.java, bundle)
        }
    }

}