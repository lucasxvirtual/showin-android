package br.com.noclaftech.showin.presentation.mysocialsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.databinding.MySocialsFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.util.Helper
import java.io.Serializable
import javax.inject.Inject

class MySocialsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: MySocialsViewModel
    lateinit var binding : MySocialsFragmentBinding

    override fun getBaseViewModel() = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = MySocialsFragmentBinding.inflate(inflater, container, false)

        screenComponent.inject(this)

        binding.let {
            it.viewModel =  viewModel
            it.lifecycleOwner = viewLifecycleOwner
            viewModel.bound(arguments!!.get("user") as Serializable)
        }

        viewModel.let {
            it.getOpenWhatsapp().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.whatsapp(activity!!, viewModel.whatsapp.value!!)
                })

            it.getOpenFacebook().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.facebook(activity!!, viewModel.facebook.value!!)
                })

            it.getOpenInstagram().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.instagram(activity!!, viewModel.instagram.value!!)
                })

            it.getOpenLinkedin().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.linkedin(activity!!, viewModel.linkedin.value!!)
                })

            it.getOpenTwitter().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.twitter(activity!!, viewModel.twitter.value!!)
                })
        }

        return binding.root
    }

    companion object {
        fun getInstance(user : User) : MySocialsFragment{
            return MySocialsFragment().apply {
                arguments = Bundle().apply { putSerializable("user", user) }
            }
        }
    }
}