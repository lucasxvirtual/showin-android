package br.com.noclaftech.showin.presentation.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.HomeFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.noclaftech.showin.presentation.adapter.*
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    private lateinit var pageAdapter: PageAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var viewModel  : HomeViewModel

    override fun getBaseViewModel() = viewModel

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater,
            container, false)

        screenComponent.inject(this)

        binding.let {
            it.lifecycleOwner = activity
            it.viewModel = viewModel

            viewModel.bound()
            pageAdapter = PageAdapter(arrayListOf())
            it.viewPager.adapter = pageAdapter

            it.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    it.pageIndicatorView.selection = position
                }
            })

//            adjustFidelityCardBounds(it)
        }

        viewModel.getBanners().observe(viewLifecycleOwner, Observer {
            it?.let {
                pageAdapter.setItems(it)
                pageAdapter.onItemClickedListener = { viewModel.onClickItem(it) }
                pageAdapter.onButtonClickedListener = {
                    val deviceId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
                    viewModel.onButtonClicked(it, deviceId)
                }
            }
        })

        viewModel.getShows().observe(viewLifecycleOwner,
            Observer {
                it?.let {

                    lin.removeAllViews()

                    it.map {
                        when(it.type){
                            "Horizontal" ->{
                                setupTitle(it.title)
                                val recyclerView = setupRecycler()

                                val adapter = ShowHorizontalAdapter(it.data)
                                adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                                adapter.onButtonClickedListener = {
                                    val deviceId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
                                    viewModel.onButtonClicked(it, deviceId)
                                }

                                recyclerView.adapter = adapter
                                lin.addView(recyclerView)
                            }
                            "Vertical" ->{
                                setupTitle(it.title)
                                val recyclerView = setupRecycler()

                                val adapter = ShowVerticalAdapter(it.data)
                                adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                                adapter.onButtonClickedListener = {
                                    val deviceId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
                                    viewModel.onButtonClicked(it, deviceId)
                                }

                                recyclerView.adapter = adapter
                                lin.addView(recyclerView)
                            }
                        }
                    }

                }
            })

        viewModel.getArtists().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    it.map {
                        setupTitle(it.title)
                        val recyclerView = setupRecycler()
                        val adapter = ArtistAdapter(it.data)
                        adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                        recyclerView.adapter = adapter
                        lin.addView(recyclerView)
                    }
                }
            })

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

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    private fun setupTitle(title : String) {
        val relative = RelativeLayout(activity)
        val textView = TextView(activity)
//        val textViewSeeAll = TextView(activity)

        textView.text = title
        textView.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        textView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        textView.setTextAppearance(R.style.textViewHome)


//        textViewSeeAll.text = activity!!.getString(R.string.see_all)
//        textViewSeeAll.setTextColor(ContextCompat.getColor(activity!!, R.color.light_blue))
//        val params  = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//        params.addRule(RelativeLayout.ALIGN_PARENT_END)
//        textViewSeeAll.layoutParams = params
//        textViewSeeAll.textSize = 14f


        relative.setPadding(Helper.dpToInt(20,activity!!), Helper.dpToInt(20,activity!!),Helper.dpToInt(20,activity!!), 0)
        relative.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

        relative.removeAllViews()
        relative.addView(textView)
//        relative.addView(textViewSeeAll)

        lin!!.addView(relative)
    }

    private fun setupRecycler() : RecyclerView {
        val recyclerView = RecyclerView(activity!!)
        recyclerView.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = null
        recyclerView.setPadding(Helper.dpToInt(20, activity!!), Helper.dpToInt(20, activity!!), Helper.dpToInt(20, activity!!), Helper.dpToInt(0, activity!!))
        recyclerView.clipToPadding = false
        recyclerView.isNestedScrollingEnabled = false
        //lin!!.addView(recyclerView)

        return recyclerView
    }
}
