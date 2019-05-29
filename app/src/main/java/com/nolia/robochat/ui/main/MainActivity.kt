package com.nolia.robochat.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseActivity
import com.nolia.robochat.domain.Chat
import com.nolia.robochat.ui.splash.SplashActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity(R.layout.activity_main), MainView {

    private val clickLogoutSubject: PublishSubject<Unit> = PublishSubject.create()
    private val reloadChatSubject = PublishSubject.create<Unit>()
    private val clickChatSubject = PublishSubject.create<Long>()

    private val mainPresenter = MainPresenter()

    private lateinit var adapter: ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatRecyclerAdapter(this, clickChatSubject::onNext)
        recyclerView.adapter = adapter

        mainPresenter.bind(this)
    }

    override fun onResume() {
        super.onResume()
        reloadChatSubject.onNext(Unit)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return super.onCreateOptionsMenu(menu)

        val item = menu.add(R.string.logout)
        item.setOnMenuItemClickListener {
            clickLogoutSubject.onNext(Unit)
            true
        }

        return true
    }

    override fun showProgress(progress: Boolean) {
        progressBar.visibility = if (progress) View.VISIBLE else View.GONE
        recyclerView.visibility = if (!progress) View.VISIBLE else View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override val clickLogoutEvent: Observable<Unit>
        get() = clickLogoutSubject

    override val clickChatEvent: Observable<Long>
        get() = clickChatSubject

    override val reloadChatEvent: Observable<Unit>
        get() = reloadChatSubject

    override fun setChats(chats: List<Chat>) {
        adapter.chats = chats
    }

    override fun openMessages(chatId: Long) {
        // Open messages activity
    }

    override fun restartSplash() {
        startActivity(
            Intent(this, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

private class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(chat: Chat) {
        itemView.chatTitle.text = chat.title
        itemView.chatMessage.text = chat.lastMessage
        Glide.with(itemView)
            .asBitmap()
            .load(chat.imageUrl)
            .into(itemView.chatImage)
    }
}

private class ChatRecyclerAdapter(
    private val context: Context,
    private val onChatClick: (Long) -> Unit
) : RecyclerView.Adapter<ChatViewHolder>() {

    var chats: List<Chat> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = chats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder =
        ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false))

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.bind(chat)
        holder.itemView.setOnClickListener {
            onChatClick(chat.id)
        }
    }
}
