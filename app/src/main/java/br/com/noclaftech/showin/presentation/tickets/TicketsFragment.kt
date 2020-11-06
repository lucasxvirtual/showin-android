package br.com.noclaftech.showin.presentation.tickets

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.databinding.TicketsFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.adapter.TicketsAdapter
import javax.inject.Inject
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.DonateTicketsDialog
import br.com.noclaftech.showin.presentation.adapter.TicketsOldAdapter
import br.com.noclaftech.showin.presentation.util.AlertHelper

class TicketsFragment : BaseFragment() {

    companion object {
        fun newInstance() = TicketsFragment()
    }

    lateinit var binding: TicketsFragmentBinding

    @Inject
    lateinit var viewModel: TicketsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : TicketsFragmentBinding = TicketsFragmentBinding.inflate(inflater,
            container, false)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = activity
            it.viewModel = viewModel

            binding.recyclerNextEvents.layoutManager = LinearLayoutManager(binding.recyclerNextEvents.context)
            binding.recyclerNextEvents.isNestedScrollingEnabled = false

            binding.recyclerPastEvents.layoutManager = LinearLayoutManager(binding.recyclerPastEvents.context)
            binding.recyclerPastEvents.isNestedScrollingEnabled = false

        }

        viewModel.bound()

        binding.donateButton.setOnClickListener {
            val future = viewModel.getTicketsFuture().value!!
            val count = viewModel.getTicketsCount().value!!
            DonateTicketsDialog(future, count) {
                viewModel.onDonationClick(it)
            }.show(activity!!.supportFragmentManager, "")
        }

        viewModel.getTicketsFuture().observe(viewLifecycleOwner,
            Observer {
                it?.let {

                    val count = viewModel.getTicketsCount().value!!
                    val adapter = TicketsAdapter(it, count, activity as BaseActivity)
                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                    binding.recyclerNextEvents.adapter = adapter

                    if (it.count() == 0){
                        binding.noNextLivesPlaceholder.visibility = View.VISIBLE
                        binding.donateButton.isClickable = false
                    }
                }
            }
        )

        viewModel.getTicketsOld().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    val adapter = TicketsOldAdapter(it)
                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                    adapter.onButtonClickedListener = { ticket -> viewModel.deleteTicket(ticket.id) }
                    binding.recyclerPastEvents.adapter = adapter

                    if (it.isEmpty()){
                        binding.noPastLivesPlaceholder.visibility = View.VISIBLE
                    }
                }

            }
        )

        viewModel.getDetail().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    AlertHelper.alertGenericTwoButtons(activity!!,
                        getString(R.string.error),
                        it,
                        getString(R.string.ok),
                        Color.parseColor("#4089e7"),
                        DialogInterface.OnClickListener { _, _ ->  },
                        null, null)
                }
            })


        return binding.root
    }
}