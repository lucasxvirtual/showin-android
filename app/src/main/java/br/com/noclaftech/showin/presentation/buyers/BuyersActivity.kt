package br.com.noclaftech.showin.presentation.buyers

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityBuyersBinding
import br.com.noclaftech.showin.databinding.ActivityFollowersBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.adapter.FollowersAdapter
import javax.inject.Inject

class BuyersActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: BuyersViewModel
    lateinit var binding : ActivityFollowersBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: FollowersAdapter
    private var show: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBuyersBinding = DataBindingUtil.setContentView(this, R.layout.activity_buyers)

        screenComponent.inject(this)

        show = intent.extras!!.get("showId") as Int

        viewModel.bound(show!!)

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            layoutManager = LinearLayoutManager(it.recyclerFollowers.context)
            it.recyclerFollowers.layoutManager = layoutManager
            it.recyclerFollowers.addOnScrollListener(recyclerViewOnScrollListener)

            adapter = FollowersAdapter(arrayListOf())
            adapter.onItemClickedListener = { viewModel.onClickItem(it) }
            it.recyclerFollowers.adapter = adapter

            it.swipe.setOnRefreshListener{
                viewModel.actionSwipe()
                binding.swipe.isRefreshing = false
            }
        }

        viewModel.getBuyers().observe(this,
            Observer {
                it?.let {
                    adapter.addItems(it)
                }
            }
        )
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

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
                    viewModel.bound(show!!)
                }
            }
        }
    }
}