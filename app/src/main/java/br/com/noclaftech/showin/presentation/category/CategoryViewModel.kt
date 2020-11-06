package br.com.noclaftech.showin.presentation.category

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ShowListResult
import br.com.noclaftech.domain.model.Category
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.usecase.ShowUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity.Companion.EXTRA_SHOW
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryViewModel@Inject constructor(
    private val categoryRouter: CategoryRouter,
    private val showUseCase: ShowUseCase,
    application: Application
): BaseViewModel(application) {

    val shows = MutableLiveData<List<Show>>().apply { value = emptyList() }
    val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    val category = MutableLiveData<Category?>().apply { value = null }

    fun bound(category: Category){
        this.category.value = category
        showUseCase.execute(category.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleResult(it)}
            .addTo(disposables)
    }

    fun onItemClicked(item: Show){
        categoryRouter.navigate(CategoryRouter.Route.SHOW, Bundle().apply { putInt(EXTRA_SHOW, item.id) })
    }

    fun onButtonClicked(item: Show){
        categoryRouter.navigate(CategoryRouter.Route.BUY, Bundle().apply { putSerializable(BuyTicketActivity.EXTRA_SHOW_HOME, item) })
    }

    private fun handleResult(result: ShowListResult){
        nonBlockingLoading.postValue(result == ShowListResult.Loading)
        when(result){
            is ShowListResult.Success ->{
                this.shows.value = result.pagination.results
            }
            is ShowListResult.Failure ->{
            }
        }
    }

    fun goBack(){
        categoryRouter.goBack()
    }

}