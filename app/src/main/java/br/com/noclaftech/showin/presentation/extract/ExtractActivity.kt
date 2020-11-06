package br.com.noclaftech.showin.presentation.extract

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityExtractBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.ExtractAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_extract.*
import javax.inject.Inject

class ExtractActivity : BaseActivity(), LifecycleOwner {
    @Inject
    lateinit var viewModel: ExtractViewModel
    private val disposables = CompositeDisposable()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExtractAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityExtractBinding = DataBindingUtil.setContentView(this, layout.activity_extract)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            viewModel.bound()

            layoutManager = LinearLayoutManager(it.recyclerView.context)
            it.recyclerView.layoutManager = layoutManager
            it.recyclerView.addOnScrollListener(recyclerViewOnScrollListener)
            adapter = ExtractAdapter(arrayListOf(), this)
            recyclerView.adapter = adapter

            binding.swipe.setOnRefreshListener{
                viewModel.actionSwipe()
                binding.swipe.isRefreshing = false
            }
        }

        viewModel.getExtract().observe(this,
        Observer {
            it?.let {
                adapter.addItems(it)
            }
        })
    }

    override fun getBaseViewModel() = viewModel

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= viewModel.pageSize) {
                    //loadMoreItems()
                    //activity.showDialog()
                    viewModel.bound()
                }
            }
        }
    }
}
