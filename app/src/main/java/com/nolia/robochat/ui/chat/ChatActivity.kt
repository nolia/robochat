package com.nolia.robochat.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseActivity
import com.nolia.robochat.domain.Message
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_message.view.*

private const val EXTRA_CHAT_ID = "chat_id"
private const val EXTRA_CHAT_TITLE = "chat_title"

class ChatActivity : BaseActivity(R.layout.activity_chat), ChatView {

    companion object {
        fun createIntent(context: Context, chatId: Long, chatTitle: String): Intent = Intent(context, ChatActivity::class.java)
            .putExtra(EXTRA_CHAT_ID, chatId)
            .putExtra(EXTRA_CHAT_TITLE, chatTitle)
    }

    private val loadMessagesSubject = PublishSubject.create<Unit>()
    private val sendMessageSubject = PublishSubject.create<String>()

    private lateinit var presenter: ChatPresenter
    private lateinit var adapter: MessageRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        val chatId = intent.getLongExtra(EXTRA_CHAT_ID, -1)
        if (chatId == -1L) {
            finish()
            return
        }
        title = intent.getStringExtra(EXTRA_CHAT_TITLE) ?: getString(R.string.app_name)

        presenter = ChatPresenter(chatId)
        setupViews()

        presenter.bind(this)
    }

    override fun onResume() {
        super.onResume()
        loadMessagesSubject.onNext(Unit)
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(this).also {
            it.stackFromEnd = true
        }

        adapter = MessageRecyclerAdapter(this)
        recyclerView.adapter = adapter

        buttonSend.setOnClickListener {
            trySendMessage()
        }
    }

    private fun trySendMessage() {
        val text = messageEditText.text.toString()
        if (text.isBlank()) return

        sendMessageSubject.onNext(text)
        messageEditText.setText("")
    }

    override val loadMessagesEvent: Observable<Unit> = loadMessagesSubject
    override val sendMessageEvent: Observable<String> = sendMessageSubject

    override fun setMessages(messages: List<Message>) {
        adapter.setMessages(messages)
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    override fun addNewMessage(message: Message) {
        adapter.addMessage(message)
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }
}

private class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(message: Message) {
        itemView.messageText.text = message.content
        itemView.messageFrom.text = message.from

        val layout = itemView as? LinearLayout ?: return

        layout.gravity = if (message.from.toLowerCase().trim() == "you") {
            Gravity.END
        } else {
            Gravity.START
        }
    }
}

private class MessageRecyclerAdapter(context: Context) : RecyclerView.Adapter<MessageViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    private  var messageList: MutableList<Message> = mutableListOf()

    fun setMessages(list: List<Message>) {
        messageList.clear()
        messageList.addAll(list)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        MessageViewHolder(layoutInflater.inflate(R.layout.item_message, parent, false))

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) = holder.bind(messageList[position])
}
