package br.com.noclaftech.showin.presentation.confirmbuyticket

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityConfirmBuyTicketBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import javax.inject.Inject

class ConfirmBuyTicketActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ConfirmBuyTicketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityConfirmBuyTicketBinding>(this, R.layout.activity_confirm_buy_ticket)

        screenComponent.inject(this)

        if (intent.hasExtra(EXTRA_SHOW)){
            viewModel.setExtraShow(intent.getSerializableExtra(EXTRA_SHOW)!!)
        }
        else if (intent.hasExtra(EXTRA_SHOW_HOME)){
            viewModel.setExtraShowHome(intent.getSerializableExtra(EXTRA_SHOW_HOME)!!)
        }

        binding.let{
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        const val EXTRA_SHOW = "show"
        const val EXTRA_SHOW_HOME = "show_home"
    }

    override fun onBackPressed() {
        viewModel.onBackClicked()
    }
}