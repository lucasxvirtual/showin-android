package br.com.noclaftech.showin.presentation.artistmoreoptions

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityArtistMoreOptionsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class ArtistMoreOptionsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistMoreOptionsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityArtistMoreOptionsBinding = DataBindingUtil.setContentView(this, layout.activity_artist_more_options)

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

        viewModel.getOpenMarketingTips().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openMarketingTips(this)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }
}
