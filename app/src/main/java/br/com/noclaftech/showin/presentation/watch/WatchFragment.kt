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
import br.com.noclaftech.showin.databinding.FragmentWatchBinding

import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.adapter.ChatAdapter

class WatchFragment: BaseFragment() {

    lateinit var binding: FragmentWatchBinding

    lateinit var viewModel  : WatchViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWatchBinding.inflate(inflater,
            container, false)

        screenComponent.inject(this)

        val adapter = ChatAdapter(emptyList()).apply { onItemClickedListener = {viewModel.onItemClicked(it)} }
        val linearLayoutManager = LinearLayoutManager(context)

        binding.let {
            it.lifecycleOwner = activity
            it.viewModel = viewModel

            it.recycler.layoutManager = linearLayoutManager
            it.recycler.adapter = adapter
        }

        viewModel.getNoChat().observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.black, resources.newTheme()))
                binding.chatLayout.visibility = View.GONE
                binding.artistInfoLayout.visibility = View.VISIBLE
            } else {
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white_border, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.white, resources.newTheme()))
                binding.chatLayout.visibility = View.VISIBLE
                binding.artistInfoLayout.visibility = View.GONE
            }
        })

        viewModel.chatMessages.observe(viewLifecycleOwner, Observer {
            val imm: InputMethodManager = context?.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.message.windowToken, 0)
        })

        return binding.root
    }

    companion object {
        fun getInstance(viewModel: WatchViewModel): WatchFragment{
            return WatchFragment().apply { this.viewModel = viewModel }
        }
    }

}