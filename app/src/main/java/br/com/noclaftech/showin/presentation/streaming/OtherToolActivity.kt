package br.com.noclaftech.showin.presentation.streaming

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityOtherToolBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.adapter.ChatAdapter
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class OtherToolActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: StreamingViewModel

    private lateinit var binding : ActivityOtherToolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_tool)

        screenComponent.inject(this)

        val adapter = ChatAdapter(emptyList()).apply { onItemClickedListener = {viewModel.onItemClicked() } }

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            it.chat.layoutManager = LinearLayoutManager(this)
            it.chat.adapter = adapter

            viewModel.chatMessages.observe(this, Observer {
                val imm: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.message.windowToken, 0)
            })

        }

        viewModel.getTutorial().observe(this, Observer {
            if(it){
                Helper.openHowToCreateShow(this)
            }
        })
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.bound(intent.extras!![EXTRA_SHOW] as Show)
        binding.hour.text = viewModel.getHour()
    }

    companion object {
        const val EXTRA_SHOW = "show"
        const val REQUEST_CODE = 650
    }

    override fun getMessage(number: Int?): String? {
        return when(number){
            StreamingViewModel.ToastMessage.LIVE.value -> getString(R.string.live_error)
            StreamingViewModel.ToastMessage.FINISH.value -> getString(R.string.finish_error)
            else -> null
        }
    }
}
