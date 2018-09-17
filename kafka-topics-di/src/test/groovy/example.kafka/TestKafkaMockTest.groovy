import example.kafka.DefaultMockableListener
import example.kafka.ExampleClient
import example.kafka.ExampleService
import example.kafka.MockableListener
import io.micronaut.configuration.kafka.config.AbstractKafkaConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.core.util.CollectionUtils
import org.spockframework.mock.MockUtil
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class TestKafkaMockTest extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run( CollectionUtils.mapOf(
            "kafka.bootstrap.servers", 'localhost:9092',
            AbstractKafkaConfiguration.EMBEDDED, true,
            AbstractKafkaConfiguration.EMBEDDED_TOPICS, ["foo.bar"]
    )).registerSingleton(ExampleService, Mock(ExampleService))

    @Shared
    MockUtil mockUtil = new MockUtil()

    ExampleService mockedService = applicationContext.getBean(ExampleService)

    def setup() {
        mockUtil.attachMock(mockedService, this)
    }

    def cleanup() {
        mockUtil.detachMock(mockedService)
    }

    def "mocked kafka bean is available for testing"() {
        given:
        PollingConditions conditions = new PollingConditions(timeout: 5, delay: 1)
        def event = UUID.randomUUID().toString()
        ExampleClient client = applicationContext.getBean(ExampleClient)

        when:
        //  Send a message via kafka client
        client.send(event)

        then:
        //  wait for messaging
        conditions.eventually {
            //  fails due to the fact that beans with @Topic won't allow mocked beans
            assert 1 * mockedService.doEvent(event)
        }
    }

    def "mock bean is available for testing"() {
        given:
        //  A bean that doesn't have kafka enabled due to no Topic defined
        MockableListener listener = applicationContext.getBean(DefaultMockableListener)
        def event = UUID.randomUUID().toString()

        when:
        //  forcing method call as we can't go through kafka messaging due to the fact kafka prevents mocked beans injection
        listener.handleEvent(event)

        then:
        //  succeeds because we are able to inject a mocked bean
        1 * mockedService.doEvent(event)

    }

}
