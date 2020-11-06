package br.com.noclaftech.showin.presentation.tutorials

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityTutorialsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class TutorialsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: TutorialsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityTutorialsBinding = DataBindingUtil.setContentView(this, layout.activity_tutorials)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
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

        viewModel.getOpenMaketingTips().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openMarketingTips(this)
                }
            })

    }
}
