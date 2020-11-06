package br.com.noclaftech.showin.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityMainBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.PopupDialog
import br.com.noclaftech.showin.presentation.home.HomeFragment
import br.com.noclaftech.showin.presentation.profile.ProfileFragment
import br.com.noclaftech.showin.presentation.profileartist.ProfileArtistFragment
import br.com.noclaftech.showin.presentation.sale.SaleFragment
import br.com.noclaftech.showin.presentation.shows.ShowsFragment
import br.com.noclaftech.showin.presentation.tickets.TicketsFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import javax.inject.Inject


class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var viewModel: MainViewModel
    private val disposables = CompositeDisposable()
    private val profileUser = ProfileFragment.newInstance()
    private val profileArtist = ProfileArtistFragment.newInstance()
    private val home = HomeFragment.newInstance()
    private val sale = SaleFragment.newInstance()
    private val shows = ShowsFragment.newInstance()
    private val tickets = TicketsFragment.newInstance()
    private lateinit var profile : Fragment
    //private lateinit var client : OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, layout.activity_main)

        screenComponent.inject(this)

        binding.viewModel = viewModel
        viewModel.bound()

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.bottomNavigation.itemIconTintList = null
        }

        viewModel.getPopUp().observe(this, Observer {
            if(it)
                PopupDialog().show(supportFragmentManager, "")
        })

        if (viewModel.user!!.isArtist) {
            profile = profileArtist
            bottom_navigation.inflateMenu(R.menu.bottom_navigation_menu)
        }
        else {
            profile = profileUser
            bottom_navigation.inflateMenu(R.menu.bottom_navigation_menu)
        }

        bottom_navigation.setOnNavigationItemSelectedListener(this)

        when {
            intent.hasExtra(BUY_TICKET) -> {
                openFragment(tickets)
                bottom_navigation.selectedItemId = R.id.tickets
            }
            intent.hasExtra(SCHEDULE_SHOW) -> {
                openFragment(shows)
                bottom_navigation.selectedItemId = R.id.myShows
            }
            else -> {
                openFragment(home)
                bottom_navigation.selectedItemId = R.id.home
            }
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                viewModel.editNotificationToken(token.toString())

            })

        subscribeFirebase()
    }

    private fun subscribeFirebase() {
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener {
            }
    }


    override fun getBaseViewModel() = viewModel

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home ->{
                openFragment(home)
            }
            R.id.tickets ->{
                openFragment(tickets)
            }
            R.id.myShows ->{
                if (profile == profileArtist) {
                    openFragment(shows)
                } else {
                    viewModel.goToChangeToArtistAccount()
                    return false
                }
            }
            R.id.sale -> {
                openFragment(sale)
            }
            R.id.profile -> {
                openFragment(profile)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        //transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if(bottom_navigation.selectedItemId != R.id.home) {
            openFragment(home)
            bottom_navigation.selectedItemId = R.id.home
        }

        else
            super.onBackPressed()
    }

    companion object{
        const val BUY_TICKET = "buy_ticket"
        const val SCHEDULE_SHOW = "schedule_show"
    }
}