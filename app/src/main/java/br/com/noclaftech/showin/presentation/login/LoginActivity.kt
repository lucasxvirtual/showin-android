package br.com.noclaftech.showin.presentation.login

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.BuildConfig
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityLoginBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.VideoDialog
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.GoogleUtil
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    companion object {
        const val REQUEST = 200
    }

    @Inject
    lateinit var viewModel: LoginViewModel
    private val disposables = CompositeDisposable()
    private var googleUtil: GoogleUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, layout.activity_login)

        screenComponent.inject(this)

        googleUtil = GoogleUtil(this, getString(R.string.client_id))

        binding.viewModel = viewModel
        viewModel.bound()

        binding.let {b ->
            b.viewModel = viewModel
            setupError(b.username, viewModel.getUsernameError())
            setupError(b.password, viewModel.getPasswordError())

            b.facebook.setOnClickListener {
                FacebookUtil.login(this).observe(this,
                    Observer {
                        it?.let {
                            //it.accessToken
                            if(!it.error && it.accessToken != null)
                                viewModel.loginSocialNetworkFacebook(it, "Facebook")
                        }
                    })
            }

            b.google.setOnClickListener {
//                val fm: FragmentManager = supportFragmentManager
//                val videoDialog = VideoDialog.newInstance("https://showin-dev.s3-sa-east-1.amazonaws.com/media/20200712_180248.mp4")
//                videoDialog.show(fm, "ola")

                googleUtil?.login()?.observe(this,
                    Observer {
                        it?.let {
                            if (!it.error && it.accessToken != null)
                                viewModel.loginSocialNetworkGoogle(it, "Google")
                        }
                    })
            }
        }

        viewModel.getDetail().observe(this,
            Observer {
                it?.let {
                    AlertHelper.alertGenericTwoButtons(this,
                        getString(R.string.error),
                        getString(R.string.email_or_password_invalid),
                        getString(R.string.ok),
                        Color.parseColor("#4089e7"),
                        DialogInterface.OnClickListener { _, _ ->  },
                        null, null)
                }
            })

        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            binding.version.text = ("${getString(R.string.version)} $version${BuildConfig.VERSION_SUFFIX}")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FacebookUtil.onActivityResult(requestCode, resultCode, data)
        googleUtil?.onActivityResult(requestCode, data)
    }

    override fun getMessage(number: Int?): String? {
        return when(number){
            LoginViewModel.message.AUTH_ERROR.value -> getString(R.string.auth_error)
            else -> null
        }
    }

    override fun getBaseViewModel() = viewModel

}