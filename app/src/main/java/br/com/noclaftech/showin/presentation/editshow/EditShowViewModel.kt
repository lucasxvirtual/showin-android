package br.com.noclaftech.showin.presentation.editshow

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ShowResult
import br.com.noclaftech.domain.model.CreateShow
import br.com.noclaftech.domain.model.ShowDetails
import br.com.noclaftech.domain.usecase.PutShowUseCase
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class EditShowViewModel @Inject constructor(
    private val router : EditShowRouter,
    private val putShowUseCase: PutShowUseCase,
    application: Application
) : BaseViewModel(application) {

    var show = MutableLiveData<ShowDetails?>().apply { value = null }

    val name = MutableLiveData<String>().apply { value = "" }
    private val nameError = MutableLiveData<Boolean>().apply { value = false }
    fun getNameError() : LiveData<Boolean> = nameError

    val ageRating = MutableLiveData<String>().apply { value = "" }
    private val ageRatingError = MutableLiveData<Boolean>().apply { value = false }
    fun getAgeRatingError(): LiveData<Boolean> = ageRatingError

    val presentation = MutableLiveData<String>().apply { value = "" }
    private val presentationError = MutableLiveData<Boolean>().apply { value = false }
    fun getPresentationError(): LiveData<Boolean> = presentationError

    val recordPosition = MutableLiveData<String>().apply { value = "" }
    private val recordPositionError = MutableLiveData<Boolean>().apply { value = false }
    fun getRecordPositionError(): LiveData<Boolean> = recordPositionError

    val category = MutableLiveData<String>().apply { value = "" }
    private val categoryError = MutableLiveData<Boolean>().apply { value = false }
    fun getCategoryError(): LiveData<Boolean> = categoryError

    val setList = MutableLiveData<String>().apply { value = "" }
    private val openCategory = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenCategory(): LiveData<Boolean> = openCategory

    val sendViewerEmail = MutableLiveData<Boolean>().apply { value = false }
    val displayViewers = MutableLiveData<Boolean>().apply { value = true }

    private val openAgeRating = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenAgeRating(): LiveData<Boolean> = openAgeRating

    private val openRecordPosition = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenRecordPosition(): LiveData<Boolean> = openRecordPosition

    private var openCamera = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenCamera() : LiveData<Boolean> = openCamera

    private val vertical = MutableLiveData<Boolean>().apply { value = false }
    private val horizontal = MutableLiveData<Boolean>().apply { value = false }
    fun getVertical(): LiveData<Boolean> = vertical
    fun getHorizontal(): LiveData<Boolean> = horizontal


    var imageVertical : String? = null
    private val notImageVertical = MutableLiveData<Boolean>().apply { value = false }
    fun getNotImageVertical(): LiveData<Boolean> = notImageVertical

    var imageHorizontal : String? = null
    private val notImageHorizontal = MutableLiveData<Boolean>().apply { value = false }
    fun getNotImageHorizontal(): LiveData<Boolean> = notImageHorizontal

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail(): LiveData<String?> = detail

    val isEditable = MutableLiveData<Boolean>().apply { value = false }

    val liveTest = MutableLiveData<Boolean>().apply { value = false}

    fun setShow(serializable: Serializable){
        serializable as ShowDetails
        show.value = serializable

        name.value = show.value?.name
        ageRating.value = show.value?.ageRatingObg?.age
        presentation.value = show.value?.description
        recordPosition.value = show.value?.position?.toLowerCase(Locale.ROOT)
        category.value = show.value?.categoryObj?.name
        setList.value = show.value?.setList
        sendViewerEmail.value = show.value?.sendViewerEmail
        displayViewers.value = show.value?.displayViewers
        liveTest.value = show.value?.liveTest

        imageVertical = show.value?.verticalImage
        imageHorizontal = show.value?.horizontalImage

        isEditable.value = show.value?.status == "WAITING"
    }

    fun onBackClick(){
        router.goBack()
    }

    fun onAgeRatingClicked(){
        if (isEditable.value!!)
            openAgeRating.postValue(true)
    }

    fun onRecordPositionClicked(){
        if (isEditable.value!!)
            openRecordPosition.postValue(true)
    }

    fun onClickCategory(){
        if (isEditable.value!!)
            openCategory.postValue(true)
    }

    fun setRecordPosition(text: String){
        recordPosition.value = text
        openRecordPosition.postValue(false)
    }

    fun setCategory(text: String){
        category.value = text
        openCategory.postValue(false)
    }

    fun setAgeRating(text : String){
        ageRating.value = text
        openAgeRating.postValue(false)
        ageRatingError.postValue(false)
    }

    fun onClickNewImageVertical(){
        if (isEditable.value!!) {
            openCamera.postValue(true)
            vertical.postValue(true)
        }
    }

    fun onClickNewImageHorizontal(){
        if (isEditable.value!!) {
            openCamera.postValue(true)
            horizontal.postValue(true)
        }
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

    fun onEditClicked(){
        nameError.value = name.value.isNullOrBlank()
        ageRatingError.value = ageRating.value.isNullOrBlank()
        presentationError.value  = presentation.value.isNullOrBlank()
        recordPositionError.value = recordPosition.value.isNullOrBlank()
        categoryError.value = category.value.isNullOrBlank()

        if (nameError.value!! ||
            ageRatingError.value!! ||
            presentationError.value!! ||
            recordPositionError.value!! ||
            categoryError.value!!)
            return

        val createShow = CreateShow()

        createShow.name = name.value
        createShow.ageRating = Helper.getAgeRating(ageRating.value!!)
        createShow.description = presentation.value
        createShow.position = recordPosition.value!!
        createShow.category = Helper.getCategory(category.value!!)
        createShow.setList = setList.value
        createShow.sendViewerEmail = sendViewerEmail.value!!
        createShow.displayViewers = displayViewers.value!!
        createShow.verticalImage = imageVertical
        createShow.horizontalImage = imageHorizontal
        createShow.liveTest = liveTest.value!!

        putShowUseCase.execute(show.value!!.id, createShow)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handlePutShowResult(it)}
            .addTo(disposables)
    }

    private fun handlePutShowResult(result : ShowResult){
        when(result){
            is ShowResult.Success ->{
                hideDialog()
                router.goBack()
                //scheduleShowRouter.navigate(ScheduleShowRouter.Route.SUCCESS, Bundle().apply { putSerializable("success", result.show) })
            }
            is ShowResult.Failure ->{
                hideDialog()

                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["non_field_errors"].toString())
            }
            is ShowResult.Loading -> {
                showDialog()
            }
        }
    }
}