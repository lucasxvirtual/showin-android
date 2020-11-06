package br.com.noclaftech.showin.presentation.sale

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.SaleFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.TransferWinnsDialog
import br.com.noclaftech.showin.presentation.adapter.ExtractAdapter
import br.com.noclaftech.showin.presentation.adapter.ExtractArtistAdapter
import javax.inject.Inject

class SaleFragment : BaseFragment() {

    lateinit var binding : SaleFragmentBinding

    companion object {
        fun newInstance() = SaleFragment()
    }

    @Inject
    lateinit var viewModel: SaleViewModel

    override fun getBaseViewModel() = viewModel

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var extractAdapter: ExtractAdapter
    private lateinit var artistExtractAdapter: ExtractArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : SaleFragmentBinding = SaleFragmentBinding.inflate(inflater,
            container, false)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        extractAdapter = ExtractAdapter(emptyList(), context!!)
        artistExtractAdapter = ExtractArtistAdapter(emptyList())

        binding.let {
            it.viewModel = viewModel
            viewModel.bound()

            layoutManager = LinearLayoutManager(context)
            binding.extractRecyclerView.layoutManager = layoutManager
            binding.extractRecyclerView.addOnScrollListener(extractRecyclerViewOnScrollListener)
        }

        viewModel.getUserOb().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    binding.availableCoins.text = it.balance!!.value.toString()

                    if(it.isArtist){
                        binding.artistExtractRadioButton.visibility = View.VISIBLE
                    }
                    else{
                        binding.artistExtractRadioButton.visibility = View.GONE
                    }
                }
            })

        viewModel.getExtract().observe(viewLifecycleOwner, Observer {
            it?.let {
                extractAdapter.replaceItems(it)
            }
        })

        viewModel.getArtistExtract().observe(viewLifecycleOwner, Observer {
            it?.let {
                artistExtractAdapter.replaceItems(it.results)
            }
        })

        viewModel.getDisplayingExtract().observe(viewLifecycleOwner, Observer {
            it.let {
                when(it) {
                    0 -> binding.extractRecyclerView.adapter = extractAdapter
                    1 -> binding.extractRecyclerView.adapter = artistExtractAdapter
                    else -> {}
                }
            }
        })

        viewModel.getNonBlockingLoading().observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.nonBlockingProgressBar.visibility = View.VISIBLE
                } else {
                    binding.nonBlockingProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.getDonatedWinnsSuccess().observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    Toast.makeText(context, "Winns transferidas!", Toast.LENGTH_SHORT).show()
                    viewModel.getUser()
                } else {
                    Toast.makeText(context, "Winns não foram transferidas.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.extractRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.extractRadioButton -> {
                    binding.artistExtractRadioButton.setTypeface(null, Typeface.NORMAL)
                    binding.extractRadioButton.setTypeface(null, Typeface.BOLD)
                }
                R.id.artistExtractRadioButton -> {
                    binding.artistExtractRadioButton.setTypeface(null, Typeface.BOLD)
                    binding.extractRadioButton.setTypeface(null, Typeface.NORMAL)
                }
            }
        }

        binding.transferWinnsButton.setOnClickListener {
            TransferWinnsDialog{
                viewModel.onTransferClicked(it)
            }.show(parentFragmentManager, "")
        }

        return binding.root
    }

    override fun getMessage(number: Int?): String? {
        return when(number){
            SaleViewModel.ToastMessage.SOON.value -> getString(R.string.soon)
            else -> null
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    private val extractRecyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLastArtistExtractPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= viewModel.extractPageSize) {
                    viewModel.bound()
                }
            }
        }
    }


}
