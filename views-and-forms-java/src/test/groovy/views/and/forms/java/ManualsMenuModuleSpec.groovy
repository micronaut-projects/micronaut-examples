package views.and.forms.java

import geb.spock.GebSpec
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest

import javax.inject.Inject

@MicronautTest
class ManualsMenuModuleSpec extends GebSpec {

    @Inject
    EmbeddedServer embeddedServer

    def "can access The Book of Geb via homepage"() {
        given:
        to GebHomePage

        when:
        manualsMenu.open()
        manualsMenu.links[0].click()

        then:
        at TheBookOfGebPage
    }

    def "verify home page" () {
        given:
        browser.baseUrl = "http://localhost:8080"
  //      go "http://localhost:8080"

        when:
        to HomePage

        then:
        at HomePage

    }

//    def "verify session based authentication works"() {
//        given:
//        browser.baseUrl = "http://localhost:${embeddedServer.port}"
//        browser.baseUrl = "http://localhost:8080"
//
//        when:
//        to Home
//
//        then:
//        at Home
//
//        when:
//        viewsAndForms.HomePage homePage = browser.page viewsAndForms.HomePage
//
//        then: 'As we are not logged in, there is no username'
//        homePage.username() == null
//
//        when: 'click the login link'
//        homePage.login()
//
//        then:
//        at LoginPage
//
//        when: 'fill the login form, with invalid credentials'
//        LoginPage loginPage = browser.page LoginPage
//        loginPage.login('foo', 'foo')
//
//        then: 'the user is still in the login form'
//        at LoginPage
//
//        and: 'and error is displayed'
//        loginPage.hasErrors()
//
//        when: 'fill the form with wrong credentials'
//        loginPage.login('sherlock', 'foo')
//
//        then: 'we get redirected to the home page'
//        at LoginFailedPage
//
//        when: 'fill the form with valid credentials'
//        loginPage.login('sherlock', 'password')
//
//        then: 'we get redirected to the home page'
//        at viewsAndForms.HomePage
//
//        when:
//        homePage = browser.page viewsAndForms.HomePage
//
//        then: 'the username is populated'
//        homePage.username() == 'sherlock'
//
//        when: 'click the logout button'
//        homePage.logout()
//
//        then: 'we are in the home page'
//        at viewsAndForms.HomePage
//
//        when:
//        homePage = browser.page viewsAndForms.HomePage
//
//        then: 'but we are no longer logged in'
//        homePage.username() == null
//    }
}