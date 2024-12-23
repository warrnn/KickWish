package com.android.kickwish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Chatbot.ChatbotAdapter
import com.android.kickwish.Chatbot.Message
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ChatbotActivity : AppCompatActivity() {
    private lateinit var API_KEY: String
    var client = OkHttpClient()

    // Activity Initialization Variables
    private lateinit var _rvChatbot: RecyclerView
    private lateinit var _tvWelcomeText: TextView
    private lateinit var _etMessage: EditText
    private lateinit var _btnSend: ImageButton
    private lateinit var messageList: MutableList<Message>
    private lateinit var chatbotAdapter: ChatbotAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeChatbotActivity()
        initializeAdapterToRecyclerView()

        _btnSend.setOnClickListener {
            val question = _etMessage.text.toString().trim()
            addToChat(question, Message.SENT_BY_ME)
            _etMessage.setText("")
            callAPI(question)
            _tvWelcomeText.visibility = View.GONE
        }
    }

    fun initializeChatbotActivity() {
        this.API_KEY = getString(R.string.openai_api_key)
        this.messageList = mutableListOf()
        this._rvChatbot = findViewById(R.id.rvChatbot)
        this._tvWelcomeText = findViewById(R.id.tvWelcomeText)
        this._etMessage = findViewById(R.id.etMessage)
        this._btnSend = findViewById(R.id.btnSend)
        this.chatbotAdapter = ChatbotAdapter(messageList)
    }

    fun initializeAdapterToRecyclerView() {
        this._rvChatbot.adapter = chatbotAdapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        this._rvChatbot.layoutManager = layoutManager
    }

    fun addToChat(message: String, sentBy: String) {
        runOnUiThread {
            messageList.add(Message(message, sentBy))
            chatbotAdapter.notifyDataSetChanged()
            _rvChatbot.smoothScrollToPosition(chatbotAdapter.itemCount)
        }
    }

    fun addResponse(response: String?) {
        messageList.removeAt(messageList.size - 1)
        addToChat(response!!, Message.SENT_BY_BOT)
    }

    // ChatGPT API Call
    fun callAPI(question: String) {
        val questionWithContext = "Batasi tanya jawab hanya seputar sneakers saja dan jawab tanpa ada simbol *. Pertanyaan: $question"

        messageList.add(Message("Typing...", Message.SENT_BY_BOT))

        val jsonBody = JSONObject()

        try {
            jsonBody.put("model", "gpt-4o")
            jsonBody.put("messages", JSONArray().put(
                JSONObject().apply {
                    put("role", "user")
                    put("content", questionWithContext)
                }
            ))
            jsonBody.put("max_tokens", 1000)
            jsonBody.put("temperature", 0)
        } catch (e: JSONException) {
            Log.d("JSONException", e.toString())
            e.printStackTrace()
        }

        val body: RequestBody = RequestBody.create(JSON, jsonBody.toString())
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API Error", e.message.toString())
                addResponse("Failed to load response due to ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body
                        if (responseBody != null) {
                            val responseString = responseBody.string()
                            Log.d("API Response", responseString)
                            val jsonObject = JSONObject(responseString)
                            val jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getJSONObject("message")
                                .getString("content").trim()
                            addResponse(result)
                        } else {
                            Log.e("API Error", "Body null")
                            addResponse("Body null")
                        }
                    } catch (e: JSONException) {
                        Log.e("JSONException", e.toString())
                        e.printStackTrace()
                    } catch (e: IOException) {
                        Log.e("IOException", e.toString())
                        e.printStackTrace()
                    }
                } else {
                    val errorBody = response.body?.string() ?: "No error body"
                    Log.e(
                        "API Error",
                        "Response code: ${response.code}, " +
                                "message: ${response.message}, " +
                                "body: $errorBody"
                    )
                    addResponse("Error: ${response.message}")
                }
            }
        })
    }

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }
}