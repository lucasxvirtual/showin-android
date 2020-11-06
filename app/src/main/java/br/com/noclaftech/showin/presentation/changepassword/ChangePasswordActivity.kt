package br.com.noclaftech.showin.presentation.changepassword

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityChangePasswordBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.AlertHelper
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ChangePasswordViewModel
   // lateinit var binding : ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityChangePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        screenComponent.inject(this)

        binding.let{
            it.viewModel = viewModel
            setupError(it.newPassword, viewModel.getPasswordError())
            setupError(it.confirmNewPassword, viewModel.confirmPasswordError())
            setupError(it.currentPassword, viewModel.currentPasswordError())

        }

        viewModel.let {
            it.getIncorrectPasswordConfirmation().observe(this,
                Observer { bool ->
                    if(bool) binding.confirmNewPassword.error = getString(R.string.password_must_match)
                })

            it.getDetail().observe(this,
                Observer {
                    it?.let { string ->
                        setAlert(string)
                    }
                })
        }

        viewModel.getSuccess().observe(this,
            Observer {
                it?.let {
                    if (it){
                        Toast.makeText(this, getString(R.string.success_change_password), Toast.LENGTH_LONG).show()
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
            DialogInterface.OnClickListener { _, _ ->  },
            null, null)
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }
}
