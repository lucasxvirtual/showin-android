package br.com.noclaftech.showin.presentation.buycoins

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityBuyCoinsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.PacksAdapter
import kotlinx.android.synthetic.main.activity_buy_coins.*
import javax.inject.Inject

class BuyCoinsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: BuyCoinsViewModel

    override fun getBaseViewModel() = viewModel
    lateinit var binding : ActivityBuyCoinsBinding
    private lateinit var adapter: PacksAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_buy_coins)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            layoutManager = LinearLayoutManager(it.recycler.context)
            it.recycler.layoutManager = layoutManager
        }

        viewModel.getPacks().observe(this,
            Observer {
                it?.let {
                    adapter = PacksAdapter(it, this)
                    recycler.adapter = adapter
                    adapter.onButtonClickedListener = { viewModel.onClickBuyPack(it) }
                }
            })
    }
}