package example.kafka;

//  Basically an interface for a non kafka/topic bean
public interface MockableListener {
    void handleEvent(String event);
}
