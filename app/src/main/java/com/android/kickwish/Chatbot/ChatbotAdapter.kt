package com.android.kickwish.Chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.R

class ChatbotAdapter(var messageList: List<Message>) :
    RecyclerView.Adapter<ChatbotAdapter.ChatbotViewHolder>() {

    class ChatbotViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val _llLeftChat = view.findViewById<LinearLayout>(R.id.llLeftChat)
        val _llRightChat = view.findViewById<LinearLayout>(R.id.llRightChat)
        val _tvLeftChat = view.findViewById<TextView>(R.id.tvLeftChat)
        val _tvRightChat = view.findViewById<TextView>(R.id.tvRightChat)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatbotAdapter.ChatbotViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatbotViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatbotAdapter.ChatbotViewHolder, position: Int) {
        var message = messageList[position]

        if (message.sentBy == Message.SENT_BY_ME) {
            holder._llLeftChat.visibility = View.GONE
            holder._llRightChat.visibility = View.VISIBLE
            holder._tvRightChat.setText(message.message)
        } else {
            holder._llLeftChat.visibility = View.VISIBLE
            holder._llRightChat.visibility = View.GONE
            holder._tvLeftChat.setText(message.message)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}