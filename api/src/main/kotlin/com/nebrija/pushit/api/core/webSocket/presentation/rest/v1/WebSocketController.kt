package com.nebrija.pushit.api.core.webSocket.presentation.rest.v1

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import com.nebrija.pushit.api.posit.domain.model.Posit

@Controller
class WebSocketController {

    @MessageMapping("/newPost")
    @SendTo("/topic/posts")
    fun sendPost(post: Posit): Posit {
        return post
    }
}
