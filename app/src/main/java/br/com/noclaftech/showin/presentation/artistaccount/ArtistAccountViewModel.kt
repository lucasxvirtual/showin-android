package br.com.noclaftech.showin.presentation.artistaccount

import android.app.Application
import br.com.noclaftech.showin.presentation.BaseViewModel
import javax.inject.Inject

class ArtistAccountViewModel @Inject constructor(
    private val router: ArtistAccountRouter,
    application: Application
) : BaseViewModel(application){

    fun onScheduleNowClicked(){
        router.navigate(ArtistAccountRouter.Route.SCHEDULE_NOW)
    }

    fun onBackClicked(){
        router.navigate(ArtistAccountRouter.Route.HOME)
    }
}