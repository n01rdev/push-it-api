package com.nebrija.pushit.api.core.webSocket.infrastructure.posit

import com.nebrija.pushit.api.posit.domain.model.Posit
import com.nebrija.pushit.api.posit.domain.notifier.IPostNotifier
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class WebSocketPostNotifier(private val messagingTemplate: SimpMessagingTemplate) : IPostNotifier {

    override fun notifyNewPost(post: Posit) {
        messagingTemplate.convertAndSend("/topic/posts", post)
    }
}