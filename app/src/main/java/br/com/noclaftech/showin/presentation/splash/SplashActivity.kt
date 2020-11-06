package br.com.noclaftech.showin.presentation.splash

import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivitySplashBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun getBaseViewModel() = viewModel
    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_splash)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

//        try {
//            val info = packageManager.getPackageInfo(
//                "br.com.noclaftech.showin",
//                PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            Log.d("KeyHash:","FALHOU")
//
//        } catch (e: NoSuchAlgorithmException) {
//            Log.d("KeyHash:","FALHOU")
//        }


        val hander = Handler()
        hander.postDelayed({
            viewModel.bound()

        }, 2000)
    }
}