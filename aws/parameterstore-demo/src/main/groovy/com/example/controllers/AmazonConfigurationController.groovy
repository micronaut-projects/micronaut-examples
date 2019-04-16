package com.example.controllers

import groovy.transform.Field
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.runtime.context.scope.Refreshable

/**
 * Controller that takes an injected AWS System Parameter store value
 *
 * @author Zachary Klein
 * @author Ryan Vanderwerf
 */
@Slf4j
@Refreshable
@Requires(env = Environment.AMAZON_EC2)
@Controller("/amazon-config")
class AmazonConfigurationController {

    //Resolved from /config/application in AWS Parameter Store
    @Value('${simple:not-set}')
    String simple

    //You can also use this syntax
    @Property(name = 'anothervalue', value="not set")
    Optional<String> anothervalue

    private String getAnothervalue() {
        anothervalue.orElse("default value")
    }

    //You can also use this syntax too but not recommended because error gets thrown if not found in parameter store
    @Property(name = 'yetanothervalue', value="not set")
    String yetanothervalue


    @Get("/")
    HttpResponse<String> index() {
        log.info "Running on Amazon EC2..."

        HttpResponse.ok("Running on Amazon EC2... simple: ${simple}  anothervalue=${getAnothervalue()} and yetanothervalue=${yetanothervalue}".toString())
    }
}
