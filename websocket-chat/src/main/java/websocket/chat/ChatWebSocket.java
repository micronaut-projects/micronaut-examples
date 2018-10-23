package websocket.chat;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@ServerWebSocket("/ws/chat/{topic}/{username}")
public class ChatWebSocket {
    private WebSocketBroadcaster broadcaster;
    
    public ChatWebSocket(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public Publisher<String> onOpen(String topic, String username, WebSocketSession session) {
        String msg = "[" + username + "] Joined!";
        return broadcaster.broadcast(msg, isValid(topic));
    }

    @OnMessage
    public Publisher<String> onMessage(
            String topic,
            String username,
            String message,
            WebSocketSession session) {
        String msg = "[" + username + "] " + message;
        return broadcaster.broadcast(msg, isValid(topic));
    }

    @OnClose
    public Publisher<String> onClose(
            String topic,
            String username,
            WebSocketSession session) {
        String msg = "[" + username + "] Disconnected!";
        return broadcaster.broadcast(msg, isValid(topic));
    }

    private Predicate<WebSocketSession> isValid(String topic) {
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
