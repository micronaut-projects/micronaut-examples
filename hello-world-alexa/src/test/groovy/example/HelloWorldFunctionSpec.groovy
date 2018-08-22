/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package example

import com.amazon.ask.model.RequestEnvelope
import com.amazon.ask.model.ResponseEnvelope
import com.amazon.ask.model.services.Serializer
import com.amazon.ask.util.JacksonSerializer
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.IgnoreIf
import spock.lang.Specification

/**
 * @author Graeme Rocher
 * @since 1.0
 */
class HelloWorldFunctionSpec extends Specification {

    void "run function directly"() {
        given:
        def file = new File("src/test/groovy/example/inputEnvelope.json").newInputStream()
        Serializer serializer = new JacksonSerializer()
        RequestEnvelope requestEnvelope = serializer.deserialize(file as InputStream,RequestEnvelope.class)


        expect:
        ResponseEnvelope responseEnvelope = new HelloWorldFunction()
                .hello(requestEnvelope)
        assert responseEnvelope
        String responseEnvelopeString = serializer.serialize(responseEnvelope)
        assert responseEnvelopeString.substring(62,responseEnvelopeString.toString().length()) == "response\":{\"outputSpeech\":{\"type\":\"SSML\",\"ssml\":\"<speak>Welcome to the Alexa Skills Kit, you can say hello</speak>\"},\"card\":{\"type\":\"Simple\",\"title\":\"HelloWorld\",\"content\":\"Welcome to the Alexa Skills Kit, you can say hello\"},\"reprompt\":{\"outputSpeech\":{\"type\":\"SSML\",\"ssml\":\"<speak>Welcome to the Alexa Skills Kit, you can say hello</speak>\"}},\"shouldEndSession\":false}}"
    }


    void "run function as REST service"() {
        given:
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer)
        HelloClient client = server.getApplicationContext().getBean(HelloClient)
        def file = new File("src/test/groovy/example/inputEnvelope.json").newInputStream()
        Serializer serializer = new JacksonSerializer()
        RequestEnvelope requestEnvelope = serializer.deserialize(file as InputStream,RequestEnvelope.class)

        when:

        ResponseEnvelope responseEnvelope = client.hello(requestEnvelope).blockingGet()
        
        then:
        assert responseEnvelope

        cleanup:
        if(server != null)
            server.stop()
    }



    @IgnoreIf({
        return !new File("${System.getProperty("user.home")}/.aws/credentials").exists()
    })
    void "run execute function as lambda"() {
        given:
        ApplicationContext applicationContext = ApplicationContext.run(
                'aws.lambda.functions.hello.functionName':'hello-world',
                'aws.lambda.region':'us-east-1'
        )
        HelloClient client = applicationContext.getBean(HelloClient)
        def file = new File("src/test/groovy/example/inputEnvelope.json").newInputStream()
        Serializer serializer = new JacksonSerializer()
        RequestEnvelope requestEnvelope = serializer.deserialize(file as InputStream,RequestEnvelope.class)


        when:
        ResponseEnvelope responseEnvelope = client.hello(requestEnvelope).blockingGet()

        then:
        assert responseEnvelope

        cleanup:
        if(applicationContext != null)
            applicationContext.stop()
    }

}
