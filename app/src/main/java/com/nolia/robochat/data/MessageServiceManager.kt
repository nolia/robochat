package com.nolia.robochat.data

import android.content.Context
import com.nolia.robochat.R
import com.nolia.robochat.domain.Chat
import com.nolia.robochat.domain.Message
import com.nolia.robochat.domain.MessageService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MessageServiceManager(private val context: Context) : MessageService {

    private val robots = listOf(
        ChatRobot(1, "Bender", R.drawable.ic_bender) {
            when (it.trim().toLowerCase()) {
                "Hi", "Hello" -> it
                else -> "Kill all humans!"
            }
        },
        ChatRobot(2, "R2D2", R.drawable.ic_r2d2) { "Beep-bee-bee-boop-bee-doo-weep" },
        ChatRobot(3, "Hal9000", R.drawable.ic_hal_9000) {
            when (it.trim().toLowerCase()) {
                "Hi", "Hello" -> "Good morning, Dave."
                else -> "I am afraid I can't do that Dave."
            }
        }
    )
    private val robotsMap = robots.associateBy { it.chatId }

    override fun getChats(): Single<List<Chat>> {
        return Single.fromCallable {
            robots.map { chatRobot ->
                Chat(
                    chatRobot.chatId,
                    chatRobot.name,
                    "android.resource://${context.packageName}/${chatRobot.imageRes}",
                    chatRobot.messages.lastOrNull()?.content ?: "Click to start"
                )
            }

        }
            .delay(500, TimeUnit.MILLISECONDS, Schedulers.computation())
    }

    override fun getMessages(chatId: Long): Single<List<Message>> = Single.fromCallable {
        robotsMap[chatId]?.messages ?: emptyList<Message>()
    }

    override fun getMessageUpdates(chatId: Long): Observable<Message> =
        robotsMap[chatId]?.newMessageChannel ?: Observable.never<Message>()

    override fun sendMessage(chatId: Long, text: String): Completable = Completable.fromAction {
        val robot = robotsMap[chatId] ?: return@fromAction
        robot.sendMessage(text)

        // Send a reply.
        Completable.fromAction { robot.sendReply() }
            .delay(Random.nextLong(1000, 1500), TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribe()
    }
}

private class ChatRobot(
    val chatId: Long,
    val name: String,
    val imageRes: Int,
    initialMessages: List<Message> = emptyList(),
    val onReplyTo: (String) -> String
) {

    val messages: MutableList<Message> = mutableListOf<Message>().apply {
        addAll(initialMessages)
    }

    val newMessageChannel = PublishSubject.create<Message>()

    private fun nextMessageId(): Long = (messages.lastOrNull()?.id ?: chatId * 1000) + 1

    fun sendMessage(text: String, from: String = "You") {
        val message = Message(
            nextMessageId(),
            from,
            text
        )
        messages.add(message)
        newMessageChannel.onNext(message)
    }

    fun sendReply() {
        val lastMessage = messages.lastOrNull() ?: return
        if (lastMessage.from == name) return

        val reply = onReplyTo(lastMessage.content)
        if (reply == "") return

        sendMessage(reply, name)
    }

}
