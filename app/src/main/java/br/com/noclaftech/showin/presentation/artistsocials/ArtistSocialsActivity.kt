package br.com.noclaftech.showin.presentation.artistsocials

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityArtistSocialsBinding
import br.com.noclaftech.showin.ext.addFragment
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.mysocialsfragment.MySocialsFragment
import storage.Singleton
import java.io.Serializable
import javax.inject.Inject

class ArtistSocialsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistSocialsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityArtistSocialsBinding = DataBindingUtil.setContentView(this,R.layout.activity_artist_socials)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        viewModel.putUser(intent.extras!!["user"] as Serializable)


        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

        }

        addFragment(MySocialsFragment.getInstance(intent.extras!!["user"] as User))
    }
}