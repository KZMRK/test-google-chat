package com.kazmiruk.testgooglechat;

import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.*;
import com.google.api.services.chat.v1.model.Thread;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/google")
@RequiredArgsConstructor
public class GoogleChatController {

    private final HangoutsChat chatService;

    @PostMapping("/space")
    public ResponseEntity<Space> createSpace() throws IOException {
        Space space = new Space()
                .setSpaceType("SPACE")
                .setDisplayName("Dima");
        Space createdSpace = chatService
                .spaces()
                .create(space)
                .execute();
        return ResponseEntity.ok(createdSpace);
    }

    @PostMapping("/spaces/{spaceId}/messages")
    public ResponseEntity<Message> sendMessage(@PathVariable String spaceId) throws IOException {
        Message message = new Message()
                .setText("Hi");
        Message sendedMessage = chatService
                .spaces()
                .messages()
                .create("spaces/" + spaceId, message)
                .execute();
        return ResponseEntity.ok(sendedMessage);
    }

    @PostMapping("/spaces/{spaceId}/messages/{messageId}/reactions")
    public ResponseEntity<Reaction> sendReaction(
            @PathVariable String spaceId,
            @PathVariable String messageId
    ) throws IOException {
        Reaction reaction = new Reaction()
                .setEmoji(new Emoji().setUnicode("\uD83D\uDE0A"));
        Reaction sendedReaction = chatService
                .spaces()
                .messages()
                .reactions()
                .create("spaces/" + spaceId + "/messages/" + messageId, reaction)
                .execute();
        return ResponseEntity.ok(sendedReaction);
    }

    @GetMapping("/spaces/{spaceId}/messages")
    public ResponseEntity<ListMessagesResponse> getAllMessages(@PathVariable String spaceId) throws IOException {
        ListMessagesResponse messages = chatService
                .spaces()
                .messages()
                .list("spaces/" + spaceId)
                .execute();

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/spaces/{spaceId}/threads/{threadId}")
    public ResponseEntity<Message> sendMessageInThread(
            @PathVariable String spaceId,
            @PathVariable String threadId
    ) throws IOException {
        Message message = new Message()
                .setText("Привіт");

        Message sendedThreadMessage = chatService
                .spaces()
                .messages()
                .create("spaces/" + spaceId , message)
                .execute();
        return ResponseEntity.ok(sendedThreadMessage);
    }
}
