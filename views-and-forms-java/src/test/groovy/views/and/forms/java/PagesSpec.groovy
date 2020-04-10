package views.and.forms.java

import geb.spock.GebSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Shared

import javax.inject.Inject

@MicronautTest
class PagesSpec extends GebSpec {

    @AutoCleanup
    @Shared
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    def "verify home page" () {
        given:
        browser.baseUrl = embeddedServer.URL.toString()

        when:
        to HomePage

        then:
        at HomePage

    }

    def "verify valid name get to thankyou page" () {
        given:
        browser.baseUrl = embeddedServer.URL.toString()

        when:
        to HomePage
        $("input", name: "userName").value("Bob")
        $("input[type=submit]").click()

        then:
        at ThankyouPage

    }

    def "verify invalid name get to home page" () {
        given:
        browser.baseUrl = embeddedServer.URL.toString()

        when:
        to HomePage
        $("input", name: "userName").value("B")
        $("input[type=submit]").click()

        then:
        at HomePage

    }

    def "verify select fruit go to thankyou page" () {
        given:
        browser.baseUrl = embeddedServer.URL.toString()

        when:
        to HomePage
        $("input", name: "userName").value("Bob")

        $("form").fruitChosen = ["banana", "mango", "grapes"]

        $("input[type=submit]").click()

        then:
        at ThankyouPage

    }

    def "verify select too many fruit go to home page" () {
        given:
        browser.baseUrl = embeddedServer.URL.toString()

        when:
        to HomePage
        $("input", name: "userName").value("Bob")

        $("form").fruitChosen = ["banana", "mango", "grapes", "star"]

        $("input[type=submit]").click()

        then:
        at HomePage

    }

}