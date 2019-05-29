package com.nolia.robochat.domain

data class Chat(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val lastMessage: String
)

data class Message(
    val id: Long,
    val from: String,
    val content: String
)
