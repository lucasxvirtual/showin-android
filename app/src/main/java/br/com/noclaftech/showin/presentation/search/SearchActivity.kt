package br.com.noclaftech.showin.presentation.search

import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivitySearchBinding
import br.com.noclaftech.showin.ext.dp
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.adapter.CategoryAdapter
import br.com.noclaftech.showin.presentation.adapter.SearchShowAdapter
import br.com.noclaftech.showin.presentation.adapter.ShowHorizontalAdapter
import br.com.noclaftech.showin.presentation.adapter.UserAdapter
import br.com.noclaftech.showin.presentation.util.FacebookLog
import br.com.noclaftech.showin.presentation.util.ItemDecorator
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SearchViewModel

    lateinit var binding : ActivitySearchBinding

    private val showAdapter = SearchShowAdapter(emptyList())
    private val userAdapter = UserAdapter(emptyList())
    private val categoryAdapter = CategoryAdapter(emptyList())

    private val linearLayoutManager = LinearLayoutManager(this)
    private val gridLayoutManager = GridLayoutManager(this, 2)
    private val itemDecorator = ItemDecorator(15.dp(), 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        screenComponent.inject(this)

        showAdapter.onItemClickedListener = {
            viewModel.onClickShow(it)
        }

        userAdapter.onItemClickedListener = {
            viewModel.onClickUser(it)
        }

        categoryAdapter.onItemClickedListener = {
            viewModel.onClickCategory(it)
        }

        binding.let {
            it.viewModel = viewModel

            it.recycler.adapter = showAdapter
            it.recycler.isNestedScrollingEnabled = true


            it.editTextSearch.setOnEditorActionListener{ _, actionId,_ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.search()
                }
                true
            }
        }

        viewModel.getShows().observe(this, Observer {
            if(it != null){
                showAdapter.replaceItems(it)
                binding.lives.text = "${getString(R.string.lives)} (${it.size.toString()})"
            } else {
                binding.lives.text = getString(R.string.lives)
            }
        })

        viewModel.getUsers().observe(this, Observer {
            if(it != null){
                userAdapter.replaceItems(it)
                binding.artists.text = "${getString(R.string.search_profile)} (${it.size.toString()})"
            } else {
                binding.artists.text = getString(R.string.search_profile)
            }
        })

        viewModel.getCategories().observe(this, Observer {
            if(it != null){
                categoryAdapter.replaceItems(it)
                binding.categories.text = "${getString(R.string.category)} (${it.size.toString()})"
            } else {
                binding.lives.text = getString(R.string.category)
            }
        })

        viewModel.getSearchLog().observe(this, Observer {
            FacebookLog.logSearch(this)
        })

        viewModel.getNumber().observe(this, Observer {
            when(it){
                1 -> {
                    binding.recycler.layoutManager = linearLayoutManager
                    binding.recycler.adapter = showAdapter
                    binding.recycler.removeItemDecoration(itemDecorator)
                    //RecyclerView to don’t calculate items size every time they added and removed from RecyclerView.
                    binding.recycler.setHasFixedSize(true)
                    //number of offscreen views to retain before adding them to the potentially shared recycled view pool.
                    binding.recycler.setItemViewCacheSize(20)

                    binding.lives.isSelected = true
                    binding.artists.isSelected = false
                    binding.categories.isSelected = false
                }
                2 -> {
                    binding.recycler.layoutManager = gridLayoutManager
                    binding.recycler.adapter = userAdapter
                    binding.recycler.addItemDecoration(itemDecorator)
                    //RecyclerView to don’t calculate items size every time they added and removed from RecyclerView.
                    binding.recycler.setHasFixedSize(true)
                    //number of offscreen views to retain before adding them to the potentially shared recycled view pool.
                    binding.recycler.setItemViewCacheSize(20)

                    binding.artists.isSelected = true
                    binding.lives.isSelected = false
                    binding.categories.isSelected = false
                }
                3 -> {
                    binding.recycler.layoutManager = linearLayoutManager
                    binding.recycler.adapter = categoryAdapter
                    binding.recycler.removeItemDecoration(itemDecorator)
                    //RecyclerView to don’t calculate items size every time they added and removed from RecyclerView.
                    binding.recycler.setHasFixedSize(true)
                    //number of offscreen views to retain before adding them to the potentially shared recycled view pool.
                    binding.recycler.setItemViewCacheSize(20)

                    binding.categories.isSelected = true
                    binding.artists.isSelected = false
                    binding.lives.isSelected = false
                }
            }
        })
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }
}
