package br.com.noclaftech.showin.presentation.extractartist

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.databinding.ActivityExtractArtistBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import br.com.noclaftech.showin.R.layout
import androidx.lifecycle.Observer
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.adapter.ExtractArtistAdapter
import kotlinx.android.synthetic.main.activity_extract_artist.*

class ExtractArtistActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ExtractArtistViewModel
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityExtractArtistBinding = DataBindingUtil.setContentView(this, layout.activity_extract_artist)

        screenComponent.inject(this)

        binding.viewModel = viewModel
        viewModel.bound(intent.extras!![ARTIST_EXTRA] as Artist)

        binding.let {
            it.viewModel = viewModel

            it.recyclerView.layoutManager = LinearLayoutManager(it.recyclerView.context)
            it.recyclerView.isNestedScrollingEnabled = false
        }

        viewModel.getExtract().observe(this,
            Observer {
                it?.let {
                    recyclerView.adapter = ExtractArtistAdapter(it.results)
                }
            })
    }

    companion object {
        const val ARTIST_EXTRA = "artist"
    }

    override fun getBaseViewModel() = viewModel
}