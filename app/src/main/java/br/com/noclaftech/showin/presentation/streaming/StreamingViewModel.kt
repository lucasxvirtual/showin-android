package br.com.noclaftech.showin.presentation.streaming

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.LiveConfigResult
import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.usecase.ConfigUseCase
import br.com.noclaftech.domain.usecase.FinishUseCase
import br.com.noclaftech.domain.usecase.GoLiveUseCase
import br.com.noclaftech.showin.ext.asyncMutation
import br.com.noclaftech.showin.ext.mutation
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.Helper
import com.google.gson.JsonParser
import com.neovisionaries.ws.client.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import storage.Singleton
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread

class StreamingViewModel @Inject constructor(private val goLiveUseCase: GoLiveUseCase,
                                             private val finishUseCase: FinishUseCase,
                                             private val configUseCase: ConfigUseCase,
                                             private val streamingRouter: StreamingRouter,
                                             application: Application): BaseViewModel(application) {

    val live = MutableLiveData<Boolean>().apply { value = false }
    val viewers = MutableLiveData<Int>().apply { value = 0 }
    val show = MutableLiveData<Show?>().apply { value = null }
    val liveConfig = MutableLiveData<LiveConfig?>().apply { value = null }
    private val alert = MutableLiveData<Boolean>().apply { value = false }
    private val noChat = MutableLiveData<Boolean>().apply { value = false }

    val emoji = MutableLiveData<Boolean>().apply { value = false }

    private var ignoreClick = false

    private lateinit var ws : WebSocket

    val chatText = MutableLiveData<String>().apply { value = "" }
    val chatMessages = MutableLiveData<ArrayList<ChatMessage>>().apply { value = arrayListOf() }

    private var user = MutableLiveData<User?>().apply { value = Singleton.instance.getUser()?.blockingGet() }
    private var artist = MutableLiveData<Artist?>().apply { value = Singleton.instance.getArtist()?.blockingGet() }

    private lateinit var mThread: Thread

    private val tutorial = MutableLiveData<Boolean>().apply { value = false }

    private val emojis = listOf("¶clap¶", "¶hot¶", "¶heart¶", "¶love¶", "¶laugh¶", "¶star¶", "¶stars¶", "¶bigstar¶")

    private var mTimer1: Timer? = null
    private var mTt1: TimerTask? = null
    private val mTimerHandler: Handler = Handler()
    val timer = MutableLiveData<String?>().apply { value = null }

    fun getTutorial(): LiveData<Boolean> = tutorial

    fun getAlert(): LiveData<Boolean> = alert

    fun getNoChat() : LiveData<Boolean> = noChat

    fun bound(show: Show){
        this.show.value = show

        live.postValue(show.status == "LIVE")
        mThread = thread {
            socket(show.id, user.value?.id)
        }
        configUseCase.execute(show.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleConfig(it)}
            .addTo(disposables)
    }

    fun emojiClick(){
        emoji.postValue(!emoji.value!!)
    }

    fun emoji(i: Int){
        chatText.mutation {
            chatText.value += emojis[i]
        }
    }

    private fun handleConfig(result: LiveConfigResult){
        when(result){
            is LiveConfigResult.Success -> {
                this.liveConfig.postValue(result.liveConfig)
            }
        }
    }

    fun seeTutorial(){
        tutorial.postValue(true)
    }

    fun onNoChatClick(){
        if (noChat.value == false){
            noChat.postValue(true)
        } else {
            noChat.postValue(false)
        }
    }

    fun onClickLive(){
        if(ignoreClick)
            return
        if(!live.value!!) {
            goLiveUseCase.execute(show.value!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {handleLive(it)}
                .addTo(disposables)
            ignoreClick = true
        } else {
            alert.postValue(true)
        }
    }

    fun finishLive(){
        finishUseCase.execute(show.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {handleFinish(it)}
            .addTo(disposables)
    }

    private fun handleLive(result: WorkedResult){
        when(result){
            is WorkedResult.Success -> {
                ignoreClick = false
                live.postValue(true)
            }
            is WorkedResult.Failure -> {
                ignoreClick = false
                setToast(ToastMessage.LIVE.value)
            }
        }
    }

    private fun handleFinish(result: WorkedResult){
        when(result){
            is WorkedResult.Success -> {
                goBack()
            }
            is WorkedResult.Failure -> {
                setToast(ToastMessage.FINISH.value)
            }
        }
    }

    fun getHour(): String{
        return if(show.value != null)
            "${Helper.getHour(show.value!!.date)} - ${Helper.getHour(show.value!!.dateFinish)}"
        else ""
    }

    fun goBack(){
        streamingRouter.goBack()
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
                            value!!.add(
                                ChatMessage(
                                    jsonObject["id"].asInt,
                                    jsonObject["username"].asString,
                                    jsonObject["verified"].asBoolean,
                                    jsonObject["photo"].asString,
                                    jsonObject["message"].asString,
                                    jsonObject["message_type"].asString
                                )
                            )
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

    fun onItemClicked() {
        return
    }

    override fun unbound(){
        super.unbound()
        ws.disconnect()
        mThread.interrupt()
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

    enum class ToastMessage(val value: Int){
        LIVE(1),
        FINISH(2)
    }

    private fun stopTimer() {
        mTimer1?.cancel()
        mTimer1?.purge()
    }

    fun startTimer() {
        mTimer1 = Timer()
        mTt1 = object : TimerTask() {
            override fun run() {
                mTimerHandler.post {
                    // get current system time
                    val currentTime = Calendar.getInstance().time
                    val finishDate = Helper.strToDate(show.value!!.dateFinish)
                    val diff: Long = finishDate!!.time - currentTime.time

                    if(diff <= 0) {
                        finishLive()
                        stopTimer()
                    }

                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    timer.postValue(String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60))
                }
            }
        }
        mTimer1?.schedule(mTt1, 0, 1000)
    }







}