package br.com.noclaftech.showin.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(application: Application): AndroidViewModel(application),
    LifecycleObserver {

    protected val disposables = CompositeDisposable()

    private val loadingVisibility = MutableLiveData<Boolean>().apply { value = false }
    fun getLoadingVisibility(): LiveData<Boolean> = loadingVisibility

    private val toast = MutableLiveData<Int>().apply { value = 0 }
    fun getToast(): LiveData<Int> = toast

    fun showDialog(){
        loadingVisibility.postValue(true)
    }

    fun hideDialog(){
        loadingVisibility.postValue(false)
    }

    open fun unbound(){
        disposables.clear()
    }

    fun setToast(code: Int){
        toast.postValue(code)
    }

    fun isDialogShowing() = loadingVisibility.value!!

    open fun onClickItem(item: Any){
        Log.e("BaseActivity", "onClickItem")
    }
}