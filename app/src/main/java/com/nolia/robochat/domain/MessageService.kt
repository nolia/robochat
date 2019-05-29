package com.nolia.robochat.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageService {

    fun getChats(): Single<List<Chat>>

    fun getMessages(chatId: Long): Single<List<Message>>
    fun getMessageUpdates(chatId: Long): Observable<Message>
    fun sendMessage(chatId: Long, text: String): Completable
}
