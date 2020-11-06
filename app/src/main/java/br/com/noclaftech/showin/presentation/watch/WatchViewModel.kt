package br.com.noclaftech.showin.presentation.watch

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.ReportResult
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.usecase.*
import br.com.noclaftech.showin.ext.asyncMutation
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import com.google.gson.JsonParser
import com.neovisionaries.ws.client.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import storage.Singleton
import java.io.Serializable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class WatchViewModel @Inject constructor(
    private val showDetailsUseCase: ShowDetailsUseCase,
    private val renewWatchUseCase: RenewWatchUseCase,
    private val removeWatchUseCase: RemoveWatchUseCase,
    private val donateUseCase: DonateUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val reportUseCase: ReportUseCase,
    private val watchRouter: WatchRouter,
    application: Application
): BaseViewModel(application){

    private lateinit var ws : WebSocket

    private val watch = MutableLiveData<Watch>().apply { value = null }
    fun getWatch() : String = watch.value!!.link!!

    private val donatedAmount = MutableLiveData<Int>().apply { value = 0 }

    val show = MutableLiveData<ShowDetails>().apply { value = null }

    val chatText = MutableLiveData<String>().apply { value = "" }
    val chatMessages = MutableLiveData<ArrayList<ChatMessage>>().apply { value = arrayListOf(
//        ChatMessage(1, "a", false, "", "aaaaaa", ""),
//        ChatMessage(1, "b", false, "", "bbbbbb", ""),
//        ChatMessage(1, "c", false, "", "cccccc", "")
    ) }

    val viewers = MutableLiveData<Int>().apply { value = 0 }

    private val detail = MutableLiveData<String?>().apply { value = null }
    fun getDetail() : LiveData<String?> = detail

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }

    private var popUpUser = MutableLiveData<User?>().apply { value = null }
    fun getPopUpUser() : LiveData<User?> = popUpUser

    private val alert = MutableLiveData<ChatMessage?>().apply { value = null }

    val fullScreen = MutableLiveData<Boolean>().apply { value = false }
    val donate = MutableLiveData<Boolean>().apply { value = false }

    private val noChat = MutableLiveData<Boolean>().apply { value = false }
    fun getNoChat() : LiveData<Boolean> = noChat

    private val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }
    fun getNonBlockingLoading() : LiveData<Boolean> = nonBlockingLoading

    private lateinit var mThread: Thread

    private var mTimer1: Timer? = null
    private var mTt1: TimerTask? = null
    private val mTimerHandler: Handler = Handler()
    private lateinit var deviceId: String

    private val emojis = listOf("¶clap¶", "¶hot¶", "¶heart¶", "¶love¶", "¶laugh¶", "¶star¶", "¶stars¶", "¶bigstar¶")
    val emoji = MutableLiveData<Boolean>().apply { value = false }

    fun onNoChatClick(){
        if (noChat.value == false){
            noChat.postValue(true)
        } else {
            noChat.postValue(false)
        }
    }

    fun emojiClick(){
        emoji.postValue(!emoji.value!!)
    }

    fun emoji(i: Int){
        chatText.mutation {
            chatText.value += emojis[i]
        }
    }

    private fun stopTimer() {
        mTimer1?.cancel()
        mTimer1?.purge()
    }

    private fun startTimer() {
        mTimer1 = Timer()
        mTt1 = object : TimerTask() {
            override fun run() {
                mTimerHandler.post {
                    renewWatchUseCase.execute(show.value!!.id, deviceId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {handleWatchResult(it)}
                        .addTo(disposables)
                }
            }
        }
        mTimer1?.schedule(mTt1, 0, 60000)
    }

    fun getAlert(): LiveData<ChatMessage?> = alert

    fun getFullScreen(): LiveData<Boolean> = fullScreen

    fun setFullScreen(boolean: Boolean){
        fullScreen.postValue(boolean)
    }

    fun onFullScreenClicked(){
        fullScreen.mutation {
            value = !value!!
        }
    }

    fun getShow(): LiveData<ShowDetails> = show

    fun bound(serializable: Serializable, showId: Int, deviceId: String){
        this.deviceId = deviceId

        watch.value = serializable as Watch
        if(showId != 0){
            showDetailsUseCase.execute(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleGetShowDetailsResult(it)}
                .addTo(disposables)

            mThread = thread(start = true) {
                socket(showId, user.value?.id)
            }
        }
    }

    fun onClickDonate() {
        donate.postValue(true)
    }

    fun onDonated(amount: Int?){
        if(amount == null || amount == -1)
            return
        val json = JSONObject()
        json.put("id", user.value!!.id)
        json.put("message", "$amount")
        json.put("username", user.value!!.username)
        json.put("verified", artist.value != null && artist.value!!.verified)
        json.put("photo", user.value!!.profileImage)
        json.put("message_type", "donation")

        ws.sendText(json.toString())

        chatText.mutation { value = "" }
    }

    fun onItemClicked(message: ChatMessage) {
        getUserUseCase.execute(message.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleGetUserResult(it, message) }
            .addTo(disposables)
    }

    private fun handleGetUserResult(result : UserResult, message: ChatMessage) {
        when(result) {
            is UserResult.Success -> {
                popUpUser.postValue(result.user)
                alert.postValue(message)
                nonBlockingLoading.postValue(false)
            }
            is UserResult.Failure -> {
                nonBlockingLoading.postValue(false)
            }
            is UserResult.Loading -> {
                nonBlockingLoading.postValue(true)
            }
        }
    }

    fun openProfile(id: Int){
        watchRouter.navigate(WatchRouter.Route.USER, bundleOf(ArtistProfileActivity.EXTRA_ARTIST to id))
    }

    fun openReport(id: Int, message: String){
        watchRouter.navigate(WatchRouter.Route.REPORT, bundleOf("id" to id, "message" to message))
    }

    private fun handleGetShowDetailsResult(result : ShowDetailsUseCase.Result){
        when(result){
            is ShowDetailsUseCase.Result.Success ->{
                show.postValue(result.showDetails)
                startTimer()
            }
        }
    }

    override fun unbound(){
        ws.disconnect()
        mThread.interrupt()
        stopTimer()
        removeWatchUseCase.execute(show.value!!.id, deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleWatchResult(it)}
            .addTo(disposables)
    }

    private fun handleWatchResult(workedResult: WorkedResult){
        when(workedResult){
            is WorkedResult.Success -> {
                Log.i("handleWatchResult", "SUCCESS")
            }
            is WorkedResult.Failure -> Log.i("handleWatchResult", "FAILURE")
        }
    }

    private fun socket(id: Int, userId: Int? = null){
        ws = if(userId != null)
            WebSocketFactory().createSocket("wss://chat.api.showin.tv:5000/ws/chat/${id}/?id=${userId}")
        else
            WebSocketFactory().createSocket("wss://chat.api.showin.tv:5000/ws/chat/${id}/")
        ws.addListener(object: WebSocketAdapter(){
            override fun onTextMessageError(
                websocket: WebSocket?,
                cause: WebSocketException?,
                data: ByteArray?
            ) {
                Log.i("onTextMessageError", cause?.message!!)
            }

            override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                Log.i("onConnectError", exception?.message!!)
            }

            override fun onDisconnected(
                websocket: WebSocket?,
                serverCloseFrame: WebSocketFrame?,
                clientCloseFrame: WebSocketFrame?,
                closedByServer: Boolean
            ) {
                Log.i("onDisconnected", "DISCONECTADO")
            }

            override fun onTextMessage(websocket: WebSocket?, text: String?) {
                val jsonObject = JsonParser().parse(text).asJsonObject
                when{
                    jsonObject.has("number") -> viewers.postValue(jsonObject["number"].asInt)
                    jsonObject.has("message") -> {
                        chatMessages.asyncMutation {
                            value!!.add(ChatMessage(
                                jsonObject["id"].asInt,
                                jsonObject["username"].asString,
                                jsonObject["verified"].asBoolean,
                                jsonObject["photo"].asString,
                                jsonObject["message"].asString,
                                jsonObject["message_type"].asString
                            ))
                        }

                    }
                }
            }

            override fun onConnected(
                websocket: WebSocket?,
                headers: MutableMap<String, MutableList<String>>?
            ) {
                Log.i("onConnected", "conectado")
            }

            override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
                Log.i("onError", cause?.message!!)
            }

            override fun onMessageError(
                websocket: WebSocket?,
                cause: WebSocketException?,
                frames: MutableList<WebSocketFrame>?
            ) {
                Log.i("onMessageError", cause?.message!!)
            }
        })
        ws.connect()
    }

    fun sendMessage(){
        if(chatText.value!!.isEmpty())
            return

        val json = JSONObject()
        json.put("id", user.value!!.id)
        json.put("message", chatText.value)
        json.put("username", user.value!!.username)
        json.put("verified", artist.value != null && artist.value!!.verified)
        json.put("photo", user.value!!.profileImage)

        ws.sendText(json.toString())

        chatText.mutation { value = "" }
    }

    fun onBackClicked(){
        watchRouter.goBack()
    }

    fun donate(amount: Int, password: String){
        donatedAmount.postValue(amount)
        donateUseCase.execute(user.value!!.balance!!.id, donatedAmount.value!!, password, show = show.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleDonateResult(it) }
            .addTo(disposables)
    }

    private fun handleDonateResult(result : DonateUseCase.Result){
        when(result){
            is DonateUseCase.Result.Success ->{
                hideDialog()
                if (result.worked.worked != null && result.worked.worked!!) {
                    onDonated(donatedAmount.value!!)
                }
                else{
                    detail.postValue(result.worked.detail)
                }
            }
            is DonateUseCase.Result.Failure ->{
                hideDialog()
                val st = (result.throwable as HttpException).response().errorBody()!!.string()
                val json = JSONObject(st.toString())

                detail.postValue(json["detail"].toString())
            }
            is DonateUseCase.Result.Loading ->{
                showDialog()
            }
        }
    }

    fun onReportClicked(userReportedId : Int, chatMessage : String, report : String) {
        reportUseCase.execute(userReported = userReportedId, chatMessage = chatMessage, reason =  report)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleReportResult(it)}
            .addTo(disposables)
    }

    private fun handleReportResult(result : ReportResult){
        when(result){
            is ReportResult.Success -> {
                hideDialog()
            }
            is ReportResult.Failure ->{
                hideDialog()
            }
            is ReportResult.Loading -> {
                showDialog()
            }
        }
    }
}