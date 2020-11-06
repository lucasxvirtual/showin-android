package br.com.noclaftech.showin.presentation.contact

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityContactUsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.FacebookLog
import javax.inject.Inject

class ContactUsActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: ContactUsViewModel

    override fun getBaseViewModel() = viewModel
    lateinit var binding : ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout.activity_contact_us)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            setupError(it.title, viewModel.getTitleError())
            setupError(it.message, viewModel.getMessageError())
        }

        viewModel.getShowMessage().observe(this,
            Observer {
                it?.let {
                    if (it) {
                        FacebookLog.logContact(this)
                        Toast.makeText(this, getString(R.string.thanks_message), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
    }
}