package br.com.noclaftech.showin.presentation.watch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.FragmentWatchFullscreenBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.adapter.ChatAdapter
import br.com.noclaftech.showin.presentation.util.Helper


class WatchFullScreenFragment: BaseFragment() {

    lateinit var binding: FragmentWatchFullscreenBinding

    lateinit var viewModel  : WatchViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWatchFullscreenBinding.inflate(inflater,
            container, false)

        screenComponent.inject(this)

        val adapter = ChatAdapter(emptyList()).apply { onItemClickedListener = { viewModel.onItemClicked(it)} }
        val linearLayoutManager = LinearLayoutManager(context)

        binding.let {
            it.recycler.layoutManager = linearLayoutManager
            it.recycler.adapter = adapter

            it.lifecycleOwner = activity
            it.viewModel = viewModel
        }

        viewModel.getShow().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.position != "VERTICAL"){
                    binding.optionsLinearLayout.visibility = View.GONE
                    binding.optionsLinearLayout2.visibility = View.VISIBLE
                    Helper.setMargins(binding.recycler, 0, 0, 300, 0)
                } else {
                    binding.optionsLinearLayout.visibility = View.VISIBLE
                    binding.optionsLinearLayout2.visibility = View.GONE
                }
            }
        })

        viewModel.chatMessages.observe(viewLifecycleOwner, Observer {
            val imm: InputMethodManager = context?.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.message.windowToken, 0)
        })

        viewModel.getNoChat().observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.black, resources.newTheme()))
                binding.chatIcon2.background = resources.getDrawable(R.drawable.bg_white, resources.newTheme())
                binding.chatIcon2.setColorFilter(resources.getColor(R.color.black, resources.newTheme()))
                binding.chatConstraint.visibility = View.GONE
                binding.messageConstraint.visibility = View.GONE
            } else {
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white_border, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.white, resources.newTheme()))
                binding.chatIcon2.background = resources.getDrawable(R.drawable.bg_white_border, resources.newTheme())
                binding.chatIcon2.setColorFilter(resources.getColor(R.color.white, resources.newTheme()))
                binding.chatConstraint.visibility = View.VISIBLE
                binding.messageConstraint.visibility = View.VISIBLE
            }
        })
        return binding.root
    }

    companion object {
        fun getInstance(viewModel: WatchViewModel): WatchFullScreenFragment{
            return WatchFullScreenFragment().apply { this.viewModel = viewModel }
        }
    }

}