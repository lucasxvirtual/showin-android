package br.com.noclaftech.showin.presentation.artistmessages

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityArtistMessagesBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.ArtistMessagesAdapter
import java.io.Serializable
import javax.inject.Inject

class ArtistMessagesActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistMessagesViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityArtistMessagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_messages)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        viewModel.putUser(intent.extras!!["user"] as Serializable)

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            binding.recyclerArtistMessages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getMessages().observe(this,
            Observer {
                it?.let {
                    binding.recyclerArtistMessages.adapter = ArtistMessagesAdapter(it)
                }
            })
    }
}



