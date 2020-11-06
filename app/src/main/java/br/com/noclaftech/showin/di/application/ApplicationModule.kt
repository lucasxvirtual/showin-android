package br.com.noclaftech.showin.di.application

import android.app.Application
import br.com.noclaftech.showin.BaseApplication
import br.com.noclaftech.showin.presentation.util.Helper.Companion.getTimeZone
import storage.SessionManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: BaseApplication) {

    private val okHttpClient : OkHttpClient = OkHttpClient( ).newBuilder( ).readTimeout( 60, TimeUnit.SECONDS )
        .writeTimeout( 60, TimeUnit.SECONDS )
        .connectTimeout( 60, TimeUnit.SECONDS )
        .addInterceptor { chain : Interceptor.Chain? ->
            val original = chain!!.request( )

            // Request customization: add request headers
            val response: okhttp3.Response
            val requestBuilder : Request.Builder

            requestBuilder = if (SessionManager(application).getToken()!!.isEmpty() && storage.Singleton.instance.getUser() == null){
                original.newBuilder()
                    .method(original.method(), original.body())
            } else if (storage.Singleton.instance.getUser() == null){
                original.newBuilder()
                    .header("Authorization", "Token " + SessionManager(application).getToken())
                    .method(original.method(), original.body())
            }
            else{
                original.newBuilder()
                    .header("Authorization", "Token " + SessionManager(application).getToken())
                    .header("The-Timezone-IANA", getTimeZone(application))
                    .method(original.method(), original.body())
            }

            val request = requestBuilder.build( )
            response = chain.proceed(request)

            return@addInterceptor response
        }
        .build( )

    @Provides
    @Singleton
    fun provideApplication(): BaseApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideApplication2(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://api.showin.tv/")
            .build()
    }
}
