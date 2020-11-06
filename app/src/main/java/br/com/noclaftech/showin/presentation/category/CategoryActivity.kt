package br.com.noclaftech.showin.presentation.category

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.noclaftech.domain.model.Category
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityCategoryBinding
import br.com.noclaftech.showin.ext.dp
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.adapter.ShowVerticalAdapter
import br.com.noclaftech.showin.presentation.util.ItemDecorator
import javax.inject.Inject

class CategoryActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: CategoryViewModel

    lateinit var binding : ActivityCategoryBinding
    private lateinit var adapter: ShowVerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)

        adapter = ShowVerticalAdapter(emptyList()).apply {
            onItemClickedListener = {show -> viewModel.onItemClicked(show) }
            onButtonClickedListener = {show -> viewModel.onButtonClicked(show) }
        }

        screenComponent.inject(this)

        binding.let {
            it.viewModel = viewModel

            it.recycler.layoutManager = GridLayoutManager(this, 2)
            it.recycler.addItemDecoration(ItemDecorator(10.dp(), 2))
            it.recycler.adapter = adapter
        }

        viewModel.shows.observe(this, Observer {
            adapter.replaceItems(it)
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.bound(intent.extras!![EXTRA_CATEGORY] as Category)
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        const val EXTRA_CATEGORY = "category"
    }
}
