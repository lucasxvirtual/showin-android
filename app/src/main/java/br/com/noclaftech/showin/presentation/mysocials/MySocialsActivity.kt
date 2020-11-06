package br.com.noclaftech.showin.presentation.mysocials

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityMySocialsBinding
import br.com.noclaftech.showin.ext.addFragment
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.mysocialsfragment.MySocialsFragment
import storage.Singleton
import javax.inject.Inject

class  MySocialsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: MySocialsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMySocialsBinding = DataBindingUtil.setContentView(this,R.layout.activity_my_socials)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            viewModel.bound()

        }

        addFragment(MySocialsFragment.getInstance(Singleton.instance.getUser()!!.blockingGet()))
    }
}