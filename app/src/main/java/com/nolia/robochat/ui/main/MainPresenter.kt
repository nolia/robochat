package com.nolia.robochat.ui.main

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.base.WithError
import com.nolia.robochat.base.WithProgress
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import com.nolia.robochat.domain.Chat
import com.nolia.robochat.domain.MessageService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface MainView : BaseView, WithProgress, WithError {

    val clickLogoutEvent: Observable<Unit>
    val reloadChatEvent: Observable<Unit>
    val clickChatEvent: Observable<Long>

    fun restartSplash()
    fun setChats(chats: List<Chat>)
    fun openMessages(chatId: Long, title: String)

}

class MainPresenter : BasePresenter<MainView>() {

    private val authService: AuthService by inject()
    private val messageService: MessageService by inject()
    private var chatList: List<Chat> = emptyList()

    override fun onBind(view: MainView) {
        super.onBind(view)

        viewDisposable.addAll(
            view.clickLogoutEvent
                .subscribe {
                    doLogout(view)
                },

            view.clickChatEvent
                .subscribe { id ->
                    val chat = chatList.firstOrNull { it.id == id } ?: return@subscribe
                    view.openMessages(id, chat.title)
                },

            // Load messages.
            view.reloadChatEvent
                .concatMapSingle {
                    messageService.getChats()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .withErrorAndProgress()
                }
                .doOnNext { chatList = it }
                .subscribe(view::setChats)
        )
    }

    private fun doLogout(view: MainView) {
        viewDisposable.addAll(
            authService.logout()
                .subscribe {
                    view.restartSplash()
                }
        )
    }

}
