package com.nolia.robochat.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nolia.robochat.domain.Chat
import com.nolia.robochat.fake.FakeMessageService
import com.nolia.robochat.robot.BaseRobotTest
import com.nolia.robochat.robot.main.mainRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatListTest : BaseRobotTest() {

    private val allChats = listOf(
        Chat(1, "Test #1", "", "Bender was here"),
        Chat(2, "Test #2", "", "Another chat"),
        Chat(3, "Test #3", "", "And another chat")
    )


    override fun beforeActivity() {
        given {
            isUserRegistered = true
            userPhoneNumber = "555-5555"
        }

        givenService<FakeMessageService> {
            chats = allChats
        }
    }

    @Test
    fun shouldShowChatList() {
        mainRobot {
            allChats.forEachIndexed { pos, chat ->
                chatItemShown(
                    pos = pos,
                    title = chat.title,
                    lastMessage = chat.lastMessage
                )
            }
        }

    }
}
