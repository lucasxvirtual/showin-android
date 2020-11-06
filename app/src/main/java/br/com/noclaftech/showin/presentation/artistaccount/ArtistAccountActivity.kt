package br.com.noclaftech.showin.presentation.artistaccount

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityArtistAccountBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import javax.inject.Inject

class ArtistAccountActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistAccountViewModel


    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_account)

        val binding : ActivityArtistAccountBinding = DataBindingUtil.setContentView(this, layout.activity_artist_account)
        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
    }

    override fun onBackPressed() {
        viewModel.onScheduleNowClicked()
    }
}