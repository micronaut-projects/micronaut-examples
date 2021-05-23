package com.example.bootstrap

import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceStartedEvent

import javax.inject.Singleton

/**
 * Show environment EC2 metadata self-discovered on boot
 * @author Zachary Klein
 */
@Slf4j
@Requires(env = Environment.AMAZON_EC2)
@Singleton
class Bootstrap implements ApplicationEventListener<ServiceStartedEvent> {

    @Override
    void onApplicationEvent(ServiceStartedEvent event) {
        log.info "Service Started..."

        //Access AWS metadata and log it out
        event.source.metadata.each { val ->
            log.info "${val.key}: ${val.value}"
        }
    }
}