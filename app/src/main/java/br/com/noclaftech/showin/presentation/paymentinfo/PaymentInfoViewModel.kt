package br.com.noclaftech.showin.presentation.paymentinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.BankResult
import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Bank
import br.com.noclaftech.domain.usecase.GetBankUseCase
import br.com.noclaftech.domain.usecase.PutBankUseCase
import br.com.noclaftech.showin.ext.isCNPJ
import br.com.noclaftech.showin.ext.isCPF
import br.com.noclaftech.showin.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import javax.inject.Inject

class PaymentInfoViewModel @Inject constructor(
    private val getBankUseCase: GetBankUseCase,
    private val putBankUseCase: PutBankUseCase,
    private val router: PaymentInfoRouter,
    application: Application) : BaseViewModel(application){

    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }

    lateinit var bankObj : Bank

    val bank = MutableLiveData<String>().apply { value = artist.value?.bank?.bank }
    private val bankError = MutableLiveData<Boolean>().apply { value = false }

    val agency = MutableLiveData<String>().apply { value = artist.value?.bank?.agency }
    private val agencyError = MutableLiveData<Boolean>().apply { value = false }

    val account = MutableLiveData<String>().apply { value = artist.value?.bank?.account }
    private val accountError = MutableLiveData<Boolean>().apply { value = false }

    val cpf = MutableLiveData<String>().apply { value = artist.value?.bank?.cpf }
    private val cpfError = MutableLiveData<Boolean>().apply { value = false }

    val cnpj = MutableLiveData<String>().apply { value = artist.value?.bank?.cnpj }
    private val cnpjError = MutableLiveData<Boolean>().apply { value = false }

    val isPhysicalPerson = MutableLiveData<Boolean>().apply { value = artist.value?.bank?.accountType == "PF" }

    val owner = MutableLiveData<String>().apply { value = artist.value?.bank?.owner }
    private val ownerError = MutableLiveData<Boolean>().apply { value = false }

    private val invalidCpf = MutableLiveData<Boolean>().apply { value = false }
    private val invalidCnpj = MutableLiveData<Boolean>().apply { value = false }

    fun getBankError(): LiveData<Boolean> = bankError
    fun getAgencyError(): LiveData<Boolean> = agencyError
    fun getAccountError(): LiveData<Boolean> = accountError
    fun getCpfError(): LiveData<Boolean> = cpfError
    fun getCnpjError(): LiveData<Boolean> = cnpjError
    fun invalidCpf(): LiveData<Boolean> = invalidCpf
    fun invalidCnpj(): LiveData<Boolean> = invalidCnpj
    fun getOwnerError(): LiveData<Boolean> = ownerError

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail

    fun goBack(){
        router.goBack()
    }

    fun bound(){
        getBankUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleGetBankResult(it)}
            .addTo(disposables)
    }

    fun onSaveClicked(){
        bankError.value = bank.value.isNullOrBlank()
        agencyError.value = agency.value.isNullOrBlank()
        accountError.value = account.value.isNullOrBlank()
        ownerError.value = owner.value.isNullOrBlank()

        if(bankError.value!! ||
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


        val accoutType =  if (isPhysicalPerson.value!!)
             "PF"
        else
            "PJ"

        putBankUseCase.execute(bankObj.id, owner.value!!, bank.value!!, agency.value!!, account.value!!, cpf.value, cnpj.value, accoutType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutBankResult(it)}
            .addTo(disposables)
    }

    private fun handlePutBankResult(result : BankResult){
        when(result){
            is BankResult.Success ->{
                hideDialog()
                router.goBack()
            }
            is BankResult.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }
            is BankResult.Loading ->{
                showDialog()
            }
        }
    }

    private fun handleGetBankResult(result : BankResult){
        when(result){
            is BankResult.Success ->{
                hideDialog()
                bankObj = result.bank
                bank.postValue(result.bank.bank)
                agency.postValue(result.bank.agency)
                owner.postValue(result.bank.owner)
                account.postValue(result.bank.account)
                cpf.postValue(result.bank.cpf)
                cnpj.postValue(result.bank.cnpj)
                account.postValue(result.bank.account)
                isPhysicalPerson.postValue(result.bank.accountType == "PF")
            }
            is BankResult.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())


                detail.postValue(json["detail"].toString())
            }
            is BankResult.Loading ->{
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