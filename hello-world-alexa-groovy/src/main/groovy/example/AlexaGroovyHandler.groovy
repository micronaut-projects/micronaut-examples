package example

import com.amazon.ask.Skills
import com.amazon.ask.model.RequestEnvelope
import com.amazon.ask.model.ResponseEnvelope
import example.handlers.CancelandStopIntentHandler
import example.handlers.HelloWorldIntentHandler
import example.handlers.HelpIntentHandler
import example.handlers.LaunchRequestHandler
import example.handlers.SessionEndedRequestHandler
import io.micronaut.function.aws.MicronautRequestHandler

class AlexaGroovyHandler extends MicronautRequestHandler<RequestEnvelope, ResponseEnvelope> {
    @Override
    ResponseEnvelope execute(RequestEnvelope input) {
        System.out.println("decoded request envelope")
        return Skills.standard()
                .addRequestHandlers(
                new CancelandStopIntentHandler(),
                new HelloWorldIntentHandler(),
                new HelpIntentHandler(),
                new LaunchRequestHandler(),
                new SessionEndedRequestHandler())
                .withSkillId("amzn1.ask.skill.69aafd3f-1c78-4613-92a3-8588a955c7c6")
                .build().invoke(input)
    }
}
