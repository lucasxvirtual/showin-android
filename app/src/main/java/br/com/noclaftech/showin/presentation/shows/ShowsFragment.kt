package br.com.noclaftech.showin.presentation.shows

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ShowsFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.TestMomentDialog
import br.com.noclaftech.showin.presentation.adapter.MyShowHorizontalAdapter
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShowsFragment : BaseFragment(){

    lateinit var binding : ShowsFragmentBinding

    companion object {
        fun newInstance() = ShowsFragment()
    }

    @Inject
    lateinit var viewModel: ShowsViewModel

    override fun getBaseViewModel() = viewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ShowsFragmentBinding.inflate(inflater, container, false)
        screenComponent.inject(this)

        binding.viewModel = viewModel


        binding.let {
            it.viewModel = viewModel
            viewModel.bound()

            binding.recyclerNextShows.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerNextShows.isNestedScrollingEnabled = false

            binding.recyclerPastShows.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerPastShows.isNestedScrollingEnabled = false
        }


        viewModel.getShowsFuture().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    val adapter = MyShowHorizontalAdapter(it)
                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                    adapter.onButtonClickedListener = { viewModel.onClickButton(it) }
                    binding.recyclerNextShows.adapter = adapter

                    if (it.count() == 0){
                        binding.noNextLivesPlaceholder.visibility = View.VISIBLE
                    }
                }

            }
        )

        viewModel.getShowsOld().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    val adapter = MyShowHorizontalAdapter(it)
                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                    adapter.onButtonClickedListener = { viewModel.onClickButton(it) }
                    binding.recyclerPastShows.adapter = adapter

                    if (it.isNotEmpty()){
                        binding.pastLivesLayout.visibility = View.VISIBLE
                    }
                }
            }
        )

        viewModel.getAlert().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    setAlert(it)
                }
            })

        return binding.root
    }

    private fun setAlert(text : String){
        requestPermissions()
//        AlertHelper.alertGenericTwoButtons(context!!,
//            getString(R.string.stream),
//            text,
//                getString(R.string.device_stream),
//            Color.parseColor("#4089e7"),
//            DialogInterface.OnClickListener { _, _ -> requestPermissions() },
//            getString(R.string.another_tool), DialogInterface.OnClickListener { _, _ -> viewModel.onClickAnotherTool() })
    }

    private fun requestPermissions(){
        activity?.let {
            if(ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    StreamingActivity.REQUEST_CODE
                )
            } else {

                TestMomentDialog {
                    viewModel.onClickStream()
                }.show(parentFragmentManager, "")
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == StreamingActivity.REQUEST_CODE){
            for(result in grantResults){
                if (result != PackageManager.PERMISSION_GRANTED){
                    requestPermissions()
                    return
                }
            }
            TestMomentDialog {
                viewModel.onClickStream()
            }.show(parentFragmentManager, "")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getArtist()
    }
}


