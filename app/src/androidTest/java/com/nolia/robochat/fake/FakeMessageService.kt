package com.nolia.robochat.fake

import com.nolia.robochat.domain.Chat
import com.nolia.robochat.domain.Message
import com.nolia.robochat.domain.MessageService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class FakeMessageService :
    IdlingService("fake_message_service"),
    MessageService {

    var chats: List<Chat> = emptyList()
    var messages: List<Message> = emptyList()

    var onMessageSent: (String) -> Unit = {}

    init {
        indlingDelay = 200L
    }

    private val messageSubject = PublishSubject.create<Message>()

    override fun getChats(): Single<List<Chat>> = Single
        .just(chats)
        .withIdling()

    override fun getMessages(chatId: Long): Single<List<Message>> = Single
        .just(messages)
        .withIdling()

    override fun getMessageUpdates(chatId: Long): Observable<Message> =
        messageSubject

    override fun sendMessage(chatId: Long, text: String): Completable = Completable.fromAction {
        onMessageSent(text)
    }
}
