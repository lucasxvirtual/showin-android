package br.com.noclaftech.showin.presentation.profilemoreoptions

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityProfileMoreOptionsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class ProfileMoreOptionsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ProfileMoreOptionsViewModel

    override fun getBaseViewModel() = viewModel

    lateinit var binding: ActivityProfileMoreOptionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_more_options)

        screenComponent.inject(this)

        binding.let{
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        viewModel.getOpenHowToCreateShow().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openHowToCreateShow(this)
                }
            })

        viewModel.getOpenStreamTips().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openStreamTips(this)
                }
            })

        viewModel.getOpenMarketingTips().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openMarketingTips(this)
                }
            })
    }
}
