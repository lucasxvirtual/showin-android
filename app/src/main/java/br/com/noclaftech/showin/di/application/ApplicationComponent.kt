package br.com.noclaftech.showin.di.application

import br.com.noclaftech.showin.BaseApplication
import br.com.noclaftech.showin.di.screen.ScreenComponent
import br.com.noclaftech.showin.di.screen.ScreenModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, EndpointModule::class])
interface ApplicationComponent {

    fun inject(activity: BaseApplication)

    fun plus(screenModule: ScreenModule): ScreenComponent
}
