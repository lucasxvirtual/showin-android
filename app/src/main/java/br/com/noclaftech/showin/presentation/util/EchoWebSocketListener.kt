package br.com.noclaftech.showin.presentation.util

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

class EchoWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        val json1 = JSONObject()

//        json1.put("message", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA TO COM DEPRESSAO")
//        json1.put("username", "matheusfalcao")
//        json1.put("verified", "1")

        Log.e("SEND", "SEND")

        webSocket.send(json1.toString())
        //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    override fun onMessage(
        webSocket: WebSocket,
        text: String
    ) {

        Log.e("MESSAGE", text)
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {

        Log.e("MESSAGE", bytes.toString())
    }

    override fun onClosing(
        webSocket: WebSocket,
        code: Int,
        reason: String
    ) {
        Log.e("CLOSE", "CLOSE")
        //webSocket.close(NORMAL_CLOSURE_STATUS, null)
        //output("Closing : $code / $reason")
    }

    override fun onFailure(
        webSocket: WebSocket?,
        t: Throwable,
        response: Response?
    ) {

        Log.e("FAIL", "FAIL")
        //output("Error : " + t.message)
    }


    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}