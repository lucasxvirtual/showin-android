package br.com.noclaftech.showin.presentation.paymentsuccess

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityPaymentSuccessBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import javax.inject.Inject

class PaymentSuccessActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: PaymentSuccessViewModel

    lateinit var binding : ActivityPaymentSuccessBinding

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout.activity_payment_success)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }
    }

    override fun onBackPressed() {
        viewModel.back()
    }
}