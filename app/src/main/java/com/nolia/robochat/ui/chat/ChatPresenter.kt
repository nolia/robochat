package com.nolia.robochat.ui.chat

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.Message
import com.nolia.robochat.domain.MessageService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ChatView : BaseView {

    val loadMessagesEvent: Observable<Unit>
    val sendMessageEvent: Observable<String>

    fun setMessages(messages: List<Message>)
    fun addNewMessage(message: Message)
}

class ChatPresenter(private val chatId: Long) : BasePresenter<ChatView>() {

    private val messageService: MessageService by inject()

    override fun onBind(view: ChatView) {
        super.onBind(view)

        viewDisposable.addAll(
            view.loadMessagesEvent
                .concatMapSingle { messageService.getMessages(chatId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setMessages),

            messageService.getMessageUpdates(chatId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::addNewMessage),

            view.sendMessageEvent
                .subscribe { text ->
                    messageService.sendMessage(chatId, text)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
        )
    }

}
