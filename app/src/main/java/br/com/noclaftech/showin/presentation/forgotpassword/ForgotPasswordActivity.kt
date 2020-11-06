package br.com.noclaftech.showin.presentation.forgotpassword

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityForgotPasswordBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.AlertHelper
import com.google.android.exoplayer2.util.ColorParser
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityForgotPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            setupError(it.email, viewModel.getEmailError())
        }

        viewModel.let{
            it.getDetail().observe(this,
            Observer {
                it?.let {string ->
                    setAlert(string)
                }
            })
        }

        viewModel.getSuccess().observe(this,
        Observer{
            it?.let {
                if (it){
                    Toast.makeText(this, getString(R.string.success_email_sent), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })

    }

    private fun setAlert(text : String){
        AlertHelper.alertGenericTwoButtons(this,
            getString(R.string.error),
            text,
            getString(R.string.ok),
            Color.parseColor("#4089e7"),
            DialogInterface.OnClickListener{ _, _ ->  },
            null, null)
    }

    override fun getBaseViewModel() = viewModel
}
