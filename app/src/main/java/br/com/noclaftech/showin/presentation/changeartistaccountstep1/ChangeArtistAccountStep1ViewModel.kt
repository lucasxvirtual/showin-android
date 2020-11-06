package br.com.noclaftech.showin.presentation.changeartistaccountstep1

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.ChangeArtistAccount
import br.com.noclaftech.domain.usecase.ChangeArtistAccountUseCase
import io.reactivex.rxkotlin.addTo
import br.com.noclaftech.showin.ext.isCNPJ
import br.com.noclaftech.showin.ext.isCPF
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.changeartistaccountstep2.ChangeArtistAccountStep2Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangeArtistAccountStep1ViewModel @Inject constructor(
    private val changeArtistAccountStep1Router: ChangeArtistAccountStep1Router,
    private val changeArtistAccountUseCase: ChangeArtistAccountUseCase,
    application: Application) : BaseViewModel(application){

    val artisticName = MutableLiveData<String>().apply { value = "" }
    private val artisticNameError = MutableLiveData<Boolean>().apply { value = false }

    val bank = MutableLiveData<String>().apply { value = "" }
    private val bankError = MutableLiveData<Boolean>().apply { value = false }

    val agency = MutableLiveData<String>().apply { value = "" }
    private val agencyError = MutableLiveData<Boolean>().apply { value = false }

    val account = MutableLiveData<String>().apply { value = "" }
    private val accountError = MutableLiveData<Boolean>().apply { value = false }

    val cpf = MutableLiveData<String>().apply { value = "" }
    private val cpfError = MutableLiveData<Boolean>().apply { value = false }

    val cnpj = MutableLiveData<String>().apply { value = "" }
    private val cnpjError = MutableLiveData<Boolean>().apply { value = false }

    val isPhysicalPerson = MutableLiveData<Boolean>().apply { value = true }

    val owner = MutableLiveData<String>().apply { value = "" }
    private val ownerError = MutableLiveData<Boolean>().apply { value = false }

    private val invalidCpf = MutableLiveData<Boolean>().apply { value = false }
    private val invalidCnpj = MutableLiveData<Boolean>().apply { value = false }

    fun getArtisticNameError(): LiveData<Boolean> = artisticNameError
    fun getBankError(): LiveData<Boolean> = bankError
    fun getAgencyError(): LiveData<Boolean> = agencyError
    fun getAccountError(): LiveData<Boolean> = accountError
    fun getCpfError(): LiveData<Boolean> = cpfError
    fun getCnpjError(): LiveData<Boolean> = cnpjError
    fun invalidCpf(): LiveData<Boolean> = invalidCpf
    fun invalidCnpj(): LiveData<Boolean> = invalidCnpj
    fun getOwnerError(): LiveData<Boolean> = ownerError

    fun onBackClicked(){
        changeArtistAccountStep1Router.goBack()
    }

    fun onNextClicked(){
        artisticNameError.value = artisticName.value.isNullOrBlank()
        bankError.value = bank.value.isNullOrBlank()
        agencyError.value = agency.value.isNullOrBlank()
        accountError.value = account.value.isNullOrBlank()
        ownerError.value = owner.value.isNullOrBlank()

        if(artisticNameError.value!! ||
            bankError.value!! ||
            agencyError.value!! ||
            accountError.value!!)
            return

        if (isPhysicalPerson.value!!){
            cpfError.value = cpf.value.isNullOrBlank()

            if (cpfError.value!!)
                return

            invalidCpf.value = !cpf.value!!.isCPF()
            if (invalidCpf.value!!)
                return
        }
        else{
            cnpjError.value = cnpj.value.isNullOrBlank()

            if (cnpjError.value!!)
                return

            invalidCnpj.value = !cnpj.value!!.isCNPJ()
            if (invalidCnpj.value!!)
                return
        }


        val changeArtistAccount = ChangeArtistAccount()

        changeArtistAccount.artisticName = artisticName.value

        if (isPhysicalPerson.value!!)
             changeArtistAccount.accountType = "PF"
        else
            changeArtistAccount.accountType =  "PJ"

        changeArtistAccount.bankName = bank.value
        changeArtistAccount.owner = owner.value
        changeArtistAccount.agency = agency.value
        changeArtistAccount.account = account.value
        changeArtistAccount.cpf = cpf.value
        changeArtistAccount.cnpj = cnpj.value

        changeArtistAccountUseCase.execute(changeArtistAccount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleChangeResult(it)}
            .addTo(disposables)

//        changeArtistAccountStep1Router.navigate(ChangeArtistAccountStep1Router.Route.CHANGE_ARTISTI_ACCOUNT_STEP_2, Bundle().apply { putSerializable("changeArtist", changeArtistAccount) })
//        changeArtistAccountStep1Router.navigate(ChangeArtistAccountStep1Router.Route.SUCESS_CHANGE)
    }

    private fun handleChangeResult(result : UserResult){
        when(result){
            is UserResult.Success ->{
                hideDialog()
                changeArtistAccountStep1Router.navigate(ChangeArtistAccountStep1Router.Route.SUCESS_CHANGE)
            }
            is UserResult.Failure ->{
                hideDialog()
            }
            is UserResult.Loading -> {
                showDialog()
            }
        }
    }

    fun setPhysicalPerson(){
        isPhysicalPerson.postValue(true)
    }

    fun setLegalPerson(){
        isPhysicalPerson.postValue(false)
    }
}