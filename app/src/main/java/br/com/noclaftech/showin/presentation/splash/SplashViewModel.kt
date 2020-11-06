package br.com.noclaftech.showin.presentation.splash

import android.app.Application
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.domain.usecase.GetConstantsUseCase
import br.com.noclaftech.domain.usecase.RememberUseCase
import br.com.noclaftech.domain.usecase.UserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val constantsUseCase: GetConstantsUseCase,
    private val rememberUseCase: RememberUseCase,
    private val router: SplashRouter,
    application: Application) : BaseViewModel(application){

    fun bound(){

        val rm = rememberUseCase.execute()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {handleLoginResult(it)}
            ?.addTo(disposables)

        if (rm == null){
            router.navigate(SplashRouter.Route.LOGIN)
        }

        constantsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleConstantsResult(it)}
            .addTo(disposables)
    }

    private fun handleConstantsResult(result : GetConstantsUseCase.Result){
        when(result){
            is GetConstantsUseCase.Result.Success ->{
            }
            is GetConstantsUseCase.Result.Failure ->{

            }
        }
    }

    private fun handleLoginResult(result : UserUseCase.Result){
        when(result){
            is UserUseCase.Result.Success ->{
                router.navigate(SplashRouter.Route.MAIN)

            }
            is UserUseCase.Result.Failure ->{
                router.navigate(SplashRouter.Route.LOGIN)
            }
            is UserUseCase.Result.Loading -> {
            }
        }
    }
}