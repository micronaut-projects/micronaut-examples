package websocket.chat

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.websocket.WebSocketClient
import io.micronaut.websocket.annotation.ClientWebSocket
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.reactivex.rxjava3.core.Flowable
import org.reactivestreams.Publisher
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import jakarta.inject.Inject
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

import java.util.concurrent.ConcurrentLinkedDeque

@Stepwise
@MicronautTest
class ChatWebSocketSpec extends Specification {

    @Inject ApplicationContext ctx

    @Inject @Shared EmbeddedServer server

    @AutoCleanup @Shared TestWebSocketClient client1
    @AutoCleanup @Shared TestWebSocketClient client2
    @AutoCleanup @Shared TestWebSocketClient client3

    PollingConditions conditions = new PollingConditions(timeout: 5, initialDelay: 0.05, factor: 1.25)

    void setupSpec() {
        client1 = createWebSocketClient(server.port, 'topic_1', 'adam')
        client2 = createWebSocketClient(server.port, 'topic_1', 'anna')
        client3 = createWebSocketClient(server.port, 'topic_2', 'ben')
    }

    void 'should broadcast message when users connect to specific topic'() {
        expect:
        conditions.eventually {
            assert client2.replies.asList() == ['[anna] Joined!']

            assert client1.replies.asList() == ['[adam] Joined!', '[anna] Joined!']

            assert client3.replies.asList() == ['[ben] Joined!']
        }
    }

    void 'should broadcast message to all users inside the topic'() {
        when:
        client1.send 'Hello everyone!'

        then:
        conditions.eventually {
            assert client1.replies.peekLast() == '[adam] Hello everyone!'

            assert client2.replies.peekLast() == '[adam] Hello everyone!'

            assert client3.replies.size() == 1
        }

        when:
        client2.send 'Hi adam!'

        then:
        conditions.eventually {
            assert client1.replies.peekLast() == '[anna] Hi adam!'

            assert client2.replies.peekLast() == '[anna] Hi adam!'

            assert client3.replies.size() == 1
        }

        when:
        client3.send 'Is there anybody out there?'

        then:
        conditions.eventually {
            assert client3.replies.size() == 2

            assert client3.replies.peekLast() == '[ben] Is there anybody out there?'

            assert client1.replies.peekLast() != '[ben] Is there anybody out there?'

            assert client2.replies.peekLast() != '[ben] Is there anybody out there?'
        }
    }

    void 'should broadcast message when user disconnects from the chat'() {
        when:
        client2.close()

        then:
        conditions.eventually {
            assert client1.replies.peekLast() == '[anna] Disconnected!'
        }
    }

    private TestWebSocketClient createWebSocketClient(int port, String topic, String username) {
        WebSocketClient webSocketClient = ctx.getBean(WebSocketClient)

        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient, "ws://localhost:${port}/ws/chat/${topic}/${username}")

        return Flowable.fromPublisher(client).blockingFirst()
    }

    @CompileStatic
    @ClientWebSocket
    static abstract class TestWebSocketClient implements AutoCloseable {

        final Collection<String> replies = new ConcurrentLinkedDeque<>()

        @OnOpen
        void onOpen() { }

        @OnMessage
        void onMessage(String message) {
            replies.add(message)
        }

        @OnClose
        void onClose() { }

        abstract void send(String message)

    }

}
