package br.com.noclaftech.showin.presentation.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.SearchResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Category
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.usecase.SearchUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.category.CategoryActivity.Companion.EXTRA_CATEGORY
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity.Companion.EXTRA_SHOW
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import storage.Singleton
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRouter: SearchRouter,
    private val searchUseCase: SearchUseCase,
    application: Application
): BaseViewModel(application) {

    private val categories = MutableLiveData<List<Category>?>().apply { value = Singleton.instance.getConstants()?.blockingGet()?.categories }
    private val shows = MutableLiveData<List<Show>?>().apply { value = emptyList() }
    private val users = MutableLiveData<List<User>?>().apply { value = emptyList() }
    private val number = MutableLiveData<Int>().apply { value = 1 }
    val searchText = MutableLiveData<String>().apply { value = "" }

    val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    private val searchLog = MutableLiveData<Boolean>().apply { value = false }

    fun getSearchLog(): LiveData<Boolean> = searchLog

    fun getNumber() : LiveData<Int> = number
    fun getCategories() : LiveData<List<Category>?> = categories
    fun getShows() : LiveData<List<Show>?> = shows
    fun getUsers() : LiveData<List<User>?> = users

    fun goBack(){
        searchRouter.goBack()
    }

    fun onClick(number: Int){
        this.number.postValue(number)
    }

    fun onClickUser(item: User){
        searchRouter.navigate(SearchRouter.Route.ARTIST, bundle = Bundle().apply { putInt(
            ArtistProfileActivity.EXTRA_ARTIST, item.id) })
    }

    fun onClickShow(item: Show){
        searchRouter.navigate(SearchRouter.Route.LIVE, bundle = Bundle().apply { putInt(EXTRA_SHOW, item.id) })
    }

    fun onClickCategory(item: Category){

        searchRouter.navigate(SearchRouter.Route.CATEGORY, bundle = Bundle().apply { putSerializable(EXTRA_CATEGORY, item) })
    }

    fun search(){
        searchUseCase.execute(searchText.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleSearchResult(it)}
            .addTo(disposables)
    }

    private fun handleSearchResult(result : SearchResult){
        searchLog.postValue(true)
        nonBlockingLoading.postValue(result == SearchResult.Loading)
        when(result){
            is SearchResult.Success ->{
                val search = result.search
                categories.postValue(search.categories)
                shows.postValue(search.shows)
                users.postValue(search.users)
                hideDialog()
            }
            is SearchResult.Failure -> {
                var a = result
                hideDialog()
            }
            is SearchResult.Loading -> {
                showDialog()
            }
        }
    }

}