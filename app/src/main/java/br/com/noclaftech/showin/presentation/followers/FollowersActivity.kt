package br.com.noclaftech.showin.presentation.followers

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.BaseActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.databinding.ActivityFollowersBinding
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.adapter.FollowersAdapter
import javax.inject.Inject

class FollowersActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: FollowersViewModel
    lateinit var binding : ActivityFollowersBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: FollowersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFollowersBinding = DataBindingUtil.setContentView(this, R.layout.activity_followers)

        screenComponent.inject(this)

        if (intent.hasExtra(FOLLOWERS)){
            viewModel.setUserIdGetFollowers(intent.extras?.get(USERID) as Int)
        }
        else{
            viewModel.setUserIdGetFollowing(intent.extras?.get(USERID) as Int)
        }

        binding.viewModel = viewModel

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

        viewModel.getFollowers().observe(this,
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
                    viewModel.bound()
                }
            }
        }
    }

    companion object{
        val FOLLOWING = "following"
        val FOLLOWERS = "followers"
        val ARTISTID = "artistId"
        val USERID = "userId"
    }
}