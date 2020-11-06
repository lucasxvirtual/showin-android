package br.com.noclaftech.showin.presentation.scheduleshow

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ShowResult
import br.com.noclaftech.domain.model.CoinsPrice
import br.com.noclaftech.domain.model.CreateShow
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.domain.usecase.PostShowRevenueUseCase
import br.com.noclaftech.domain.usecase.PostShowUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.main.MainActivity
import br.com.noclaftech.showin.presentation.util.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import java.io.Serializable
import javax.inject.Inject

class ScheduleShowViewModel @Inject constructor(
    private val scheduleShowRouter: ScheduleShowRouter,
    private val postShowRevenueUseCase: PostShowRevenueUseCase,
    private val postShowUseCase: PostShowUseCase,
    application: Application): BaseViewModel(application){

    var coinsPrice = MutableLiveData<CoinsPrice>().apply { value = null }

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail(): LiveData<String?> = detail
    // Step 1

    val name = MutableLiveData<String>().apply { value = "" }
    private val nameError = MutableLiveData<Boolean>().apply { value = false }
    fun getNameError() : LiveData<Boolean> = nameError

    val date = MutableLiveData<String>().apply { value = "" }
    private val dateError = MutableLiveData<Boolean>().apply { value = false }
    private val invalidDate = MutableLiveData<Boolean>().apply { value = false }
    fun getDateError() : LiveData<Boolean> = dateError
    fun getInvalidDate() : LiveData<Boolean> = invalidDate

    val hour = MutableLiveData<String>().apply { value  = "" }
    private val hourError = MutableLiveData<Boolean>().apply { value = false }
    private val invalidHour = MutableLiveData<Boolean>().apply { value = false }
    fun getHourError(): LiveData<Boolean> = hourError
    fun getInvalidHour(): LiveData<Boolean> = invalidHour

    val duration = MutableLiveData<String>().apply { value = "" }
    private val durationError = MutableLiveData<Boolean>().apply { value = false }
    fun getDurationError(): LiveData<Boolean> = durationError

    val description = MutableLiveData<String>().apply { value = "" }
    private val descriptionError = MutableLiveData<Boolean>().apply { value = false }
    fun getDescriptionError(): LiveData<Boolean> = descriptionError

    val ageRating = MutableLiveData<String>().apply { value = "" }
    private val ageRatingError = MutableLiveData<Boolean>().apply { value = false }
    fun getAgeRatingError(): LiveData<Boolean> = ageRatingError

    val recordPosition = MutableLiveData<String>().apply { value = null }
    private val recordPositionError = MutableLiveData<Boolean>().apply { value = false }
    fun getRecordPositionError(): LiveData<Boolean> = recordPositionError

    private var imageVertical : String? = null
    private val notImageVertical = MutableLiveData<Boolean>().apply { value = false }
    fun getNotImageVertical(): LiveData<Boolean> = notImageVertical

    private var imageHorizontal : String? = null
    private val notImageHorizontal = MutableLiveData<Boolean>().apply { value = false }
    fun getNotImageHorizontal(): LiveData<Boolean> = notImageHorizontal

    private var openCamera = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenCamera() : LiveData<Boolean> = openCamera

    private var cost = MutableLiveData<Boolean>().apply { value = false }
    fun getCost() : LiveData<Boolean> = cost

    private val vertical = MutableLiveData<Boolean>().apply { value = false }
    private val horizontal = MutableLiveData<Boolean>().apply { value = false }

    fun getVertical(): LiveData<Boolean> = vertical
    fun getHorizontal(): LiveData<Boolean> = horizontal

    private val openAgeRating = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenAgeRating(): LiveData<Boolean> = openAgeRating

    private val openRecordPosition = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenRecordPosition(): LiveData<Boolean> = openRecordPosition

    private var openDate = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenDate() : LiveData<Boolean> = openDate

    private var openHour = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenHour() : LiveData<Boolean> = openHour

    fun onDateClicked(){
        openDate.postValue(true)
    }

    fun onHourClicked(){
        openHour.postValue(true)
    }

    fun updateDate(string: String){
//        val  myFormat = "dd/MM/yyyy"
//        val  sdf = SimpleDateFormat(myFormat)
//        date.value = sdf.format(string.time)

        date.postValue(string)
        openDate.postValue(false)
    }

    fun updateHour(string: String){
        hour.postValue(string)
        openHour.postValue(false)
    }

    fun setAgeRating(text : String){
        ageRating.postValue(text)
        openAgeRating.postValue(false)
        ageRatingError.postValue(false)
    }

    fun setRecordPosition(text: String){
        recordPosition.value = text
        openRecordPosition.postValue(false)
    }

    private val openDuration = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenDuration(): LiveData<Boolean> = openDuration

    fun onAgeRatingClicked(){
        openAgeRating.postValue(true)
    }

    fun onRecordPositionClicked(){
        openRecordPosition.postValue(true)
    }

    fun onDurationClicked(){
        openDuration.postValue(true)
    }

    fun setDuration(text: String){
        duration.postValue(text)
        openDuration.postValue(false)
        durationError.postValue(false)
    }

    fun onClickCost(){
        cost.postValue(true)
    }


    fun onClickNext(){
        nameError.value = name.value.isNullOrBlank()
        dateError.value = date.value.isNullOrBlank()
        hourError.value = hour.value.isNullOrBlank()
        durationError.value = duration.value.isNullOrBlank()
        descriptionError.value  = description.value.isNullOrBlank()
        ageRatingError.value = ageRating.value.isNullOrBlank()
        recordPositionError.value = recordPosition.value.isNullOrBlank()
        categoryError.value = category.value.isNullOrBlank()

        if (nameError.value!! ||
                dateError.value!! ||
                hourError.value!! ||
                durationError.value!! ||
                descriptionError.value!! ||
                ageRatingError.value!! ||
                recordPositionError.value!! ||
                categoryError.value!!)
            return

        invalidDate.value = !Helper.validateDate(date.value!!)

        if (invalidDate.value!!)
            return

        invalidHour.value = !Helper.validateHour(hour.value!!)

        if (invalidHour.value!!)
            return

        invalidDate.value = !Helper.validateCurrentDate(date.value!!, hour.value!!)
        invalidHour.value = !Helper.validateCurrentDate(date.value!!, hour.value!!)

        if (invalidDate.value!! || invalidHour.value!!)
            return

        notImageHorizontal.value = imageHorizontal.isNullOrBlank()
        if (notImageHorizontal.value!!)
            return

        notImageVertical.value = imageVertical.isNullOrBlank()
        if (notImageVertical.value!!)
            return


        val createShow = CreateShow()

        createShow.name = name.value
        createShow.date = Helper.convertDateForApi(date.value!!, hour.value!!)
        createShow.duration = Helper.getDuration(duration.value!!)
        createShow.description = description.value
        createShow.ageRating = Helper.getAgeRating(ageRating.value!!)
        createShow.horizontalImage = imageHorizontal
        createShow.verticalImage = imageVertical
        createShow.position = recordPosition.value!!
        createShow.category = Helper.getCategory(category.value!!)

        scheduleShowRouter.navigate(ScheduleShowRouter.Route.SCHEDULE_SHOW_2, Bundle().apply { putSerializable("show", createShow) })
//        scheduleShowRouter.navigate(ScheduleShowRouter.Route.SCHEDULE_SHOW_2)
    }

    fun onClickNewImageVertical(){
        openCamera.postValue(true)
        vertical.postValue(true)
    }

    fun onClickNewImageHorizontal(){
        openCamera.postValue(true)
        horizontal.postValue(true)
    }

    fun setImageThumb(image : String?){

        if (vertical.value!!)
            imageVertical = image
        else
            imageHorizontal = image

        vertical.postValue(false)
        horizontal.postValue(false)
        openCamera.postValue(false)
    }

    //Step 1

    //Step2

    private var show : CreateShow = CreateShow()

    fun setShow(serializable: Serializable){
        show = serializable as CreateShow
    }

    val payWhatYouCan = MutableLiveData<Boolean>().apply { value = false }
    val limitTicket = MutableLiveData<Boolean>().apply { value = false }
    val sendTicketNotification = MutableLiveData<Boolean>().apply { value  = true }
    val displayViewers = MutableLiveData<Boolean>().apply { value  = true }
    val liveTest = MutableLiveData<Boolean>().apply { value = false}

    val category = MutableLiveData<String>().apply { value = "" }
    private val openCategory = MutableLiveData<Boolean>().apply { value = false }
    private val categoryError = MutableLiveData<Boolean>().apply { value = false }
    fun getCategoryError(): LiveData<Boolean> = categoryError
    fun getOpenCategory(): LiveData<Boolean> = openCategory

    val ticketPrice = MutableLiveData<String>().apply { value = "" }
    private val ticketError = MutableLiveData<Boolean>().apply { value = false }
    private val invalidTicket = MutableLiveData<Boolean>().apply { value = false }
    fun getTicketError(): LiveData<Boolean> = ticketError
    fun getInvalidTicket(): LiveData<Boolean> = invalidTicket

    val suggestedCurrency = MutableLiveData<String>().apply { value = "" }
    private val suggestedCurrencyError = MutableLiveData<Boolean>().apply { value = false }
    private val invalidSuggestedCurrency = MutableLiveData<Boolean>().apply { value = false }
    fun getSuggestedCurrencyError(): LiveData<Boolean> = suggestedCurrencyError
    fun getInvalidSuggestedCurrency(): LiveData<Boolean> = invalidSuggestedCurrency

    val limit = MutableLiveData<String>().apply { value = "" }
    private val limitError = MutableLiveData<Boolean>().apply { value = false }
    fun getLimitError(): LiveData<Boolean> = limitError

    fun onClickCategory(){
        openCategory.postValue(true)
    }

    fun getEcad() : String {
        return Helper.getCategoriesObj().find { it.id == show.category }!!.ecad.toString() + "%"
    }

    fun setCategory(text: String){
        category.postValue(text)
        openCategory.postValue(false)
    }

    fun onClickSuccess(){
        if (payWhatYouCan.value!!){
            suggestedCurrencyError.value = suggestedCurrency.value.isNullOrBlank()
            if (suggestedCurrencyError.value!!)
                return
                //loginRouter.navigate(LoginRouter.Route.MAIN, Bundle())

            invalidSuggestedCurrency.value = suggestedCurrency.value!!.toInt() < Singleton.instance.getConstants()?.blockingGet()!!.config.minimunPrice!!
            if (invalidSuggestedCurrency.value!!)
                return
        }
        else{
            ticketError.value = ticketPrice.value.isNullOrBlank()
            if (ticketError.value!!)
                return

            invalidTicket.value = ticketPrice.value!!.toInt() < Singleton.instance.getConstants()?.blockingGet()!!.config.minimunPrice!!
            if (invalidTicket.value!!)
                return
        }

        if (limitTicket.value!!){
            limitError.value = limit.value.isNullOrBlank()
            if (limitError.value!!)
                return
        }

//        categoryError.value = category.value.isNullOrBlank()
//        if (categoryError.value!!)
//            return


        show.payWhatYouCan = payWhatYouCan.value!!

        if (payWhatYouCan.value!!)
            show.ticketValue = suggestedCurrency.value!!.toInt()
        else
            show.ticketValue = ticketPrice.value!!.toInt()

        if (limitTicket.value!!)
            show.ticketLimit = limit.value!!.toInt()

//        show.category = Helper.getCategory(category.value!!)

        show.sendViewerEmail = sendTicketNotification.value

        show.displayViewers = displayViewers.value!!

        show.liveTest = liveTest.value!!

        postShowUseCase.execute(show)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostShowResult(it)}
            .addTo(disposables)
    }

    fun getPercentage() = String.format("%d%%", Singleton.instance.getConstants()?.blockingGet()!!.config.artistPercentage!!.times(100).toInt())

    private fun handlePostShowResult(result : ShowResult){
        when(result){
            is ShowResult.Success ->{
                hideDialog()
                scheduleShowRouter.navigate(ScheduleShowRouter.Route.SUCCESS, Bundle().apply { putSerializable("success", result.show) })
            }
            is ShowResult.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                if(json.has("non_field_errors"))
                    detail.postValue(json["non_field_errors"].toString())

                if(json.has("detail"))
                    detail.postValue(json["detail"].toString())
            }
            is ShowResult.Loading -> {
                showDialog()
            }
        }
    }

    fun postShowRevenue(){
        val durations = Singleton.instance.getConstants()?.blockingGet()?.showDurations
        val filteredDuration = durations?.find { it.minutes == show.duration!! }

        val price = if (!payWhatYouCan.value!!){
            ticketPrice.value!!.toInt()
        } else {
            suggestedCurrency.value!!.toInt()
        }


        postShowRevenueUseCase.execute(filteredDuration!!.id, price, show.category!! )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePostShowRevenue(it)}
            .addTo(disposables)
    }

    private fun handlePostShowRevenue(result : PostShowRevenueUseCase.Result){
        when (result){
            is PostShowRevenueUseCase.Result.Success -> {
                coinsPrice.postValue(result.coinsPrice)

//                hideDialog()
            }
            is PostShowRevenueUseCase.Result.Failure -> {
//                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                if(json.has("detail"))
                    detail.postValue(json["detail"].toString())
            }
            is PostShowRevenueUseCase.Result.Loading -> {
//                showDialog()
            }
        }
    }

    //Step2

    //Set3

    val successShow = MutableLiveData<Show>().apply { value = null }
    fun getSuccessShow() : LiveData<Show?> = successShow

    fun setSuccessShow(serializable: Serializable){
            successShow.postValue(serializable as Show)
    }

    fun getHour() : String {
        if (successShow.value?.date != null){
            return "${Helper.getHour(successShow.value!!.date)} - ${Helper.getHour(successShow.value!!.dateFinish)}"
        }
        return ""
    }

    fun getDate() : String {
        if (successShow.value?.date != null){
            return Helper.getDateShow(successShow.value!!.date)
        }
        return ""
    }

    fun onClickFinish(){
        scheduleShowRouter.navigate(ScheduleShowRouter.Route.AGENDA, Bundle().apply { putString(MainActivity.SCHEDULE_SHOW, MainActivity.SCHEDULE_SHOW) })
    }

    fun goToHome(){
        scheduleShowRouter.navigate(ScheduleShowRouter.Route.HOME)
    }

    fun goBack(){
        scheduleShowRouter.navigate(ScheduleShowRouter.Route.BACK)
    }
}