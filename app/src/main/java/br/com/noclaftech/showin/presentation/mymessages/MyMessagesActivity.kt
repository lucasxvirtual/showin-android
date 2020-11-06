package br.com.noclaftech.showin.presentation.mymessages

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityMyMessagesBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.ArtistMessagesAdapter
import javax.inject.Inject

class MyMessagesActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: MyMessagesViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMyMessagesBinding = DataBindingUtil.setContentView(this, layout.activity_my_messages)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        viewModel.setUser(intent.getSerializableExtra("user")!!)
        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            binding.recyclerMessages.layoutManager = LinearLayoutManager(this)
            binding.recyclerMessages.isNestedScrollingEnabled = true
            setupError(it.message, viewModel.getMessageError())
        }

        viewModel.getMessages().observe(this,
            Observer {
                it?.let {
                    val adapter = ArtistMessagesAdapter(it)
                    binding.recyclerMessages.adapter = adapter
                }
            })
    }
}
