package br.com.noclaftech.showin.presentation.settings

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivitySettingsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SettingsViewModel
    private lateinit var binding : ActivitySettingsBinding
    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        viewModel.getOpenTermsUse().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openTermsUse(this)
                }
            })

        viewModel.getOpenPrivacyPolicy().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openPrivacyPolicy(this)
                }
            })
    }
}
