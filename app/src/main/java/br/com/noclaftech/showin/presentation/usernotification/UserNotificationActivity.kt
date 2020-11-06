package br.com.noclaftech.showin.presentation.usernotification

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityUserNotificationBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.UserNotificationAdapter
import br.com.noclaftech.showin.presentation.util.AlertHelper
import javax.inject.Inject

class UserNotificationActivity : BaseActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: UserNotificationAdapter

    @Inject
    lateinit var viewModel: UserNotificationViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityUserNotificationBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_notification)

        screenComponent.inject(this)

        binding.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            viewModel.bound()
            layoutManager = LinearLayoutManager(it.recyclerNotifications.context)
            it.recyclerNotifications.layoutManager = layoutManager
            it.recyclerNotifications.addOnScrollListener(recyclerViewOnScrollListener)
            adapter = UserNotificationAdapter(arrayListOf())
            adapter.onItemClickedListener = {notification ->  viewModel.deleteNotification(notification.id)}
            it.recyclerNotifications.adapter = adapter
            it.recyclerNotifications.isNestedScrollingEnabled = true

            binding.swipe.setOnRefreshListener{
                viewModel.actionSwipe()
                binding.swipe.isRefreshing = false
            }
        }

        viewModel.getNotification().observe(this,
            Observer {
                it?.let {
                    adapter.addItems(it)
                }
            })

        viewModel.getAlert().observe(this,
            Observer {
                it?.let {
                    if(it)
                        setAlert()
                }
            })
    }

    private fun setAlert(){
        AlertHelper.alertGenericTwoButtons(this,
            getString(R.string.erase),
            getString(R.string.erase_all),
            getString(R.string.yes),
            Color.parseColor("#4089e7"),
            DialogInterface.OnClickListener { _, _ -> viewModel.deleteAll() },
            getString(R.string.no),
            null)
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= viewModel.pageSize) {
                    viewModel.bound()
                }
            }
        }
    }
}