import spock.lang.Specification

class SpockSpec extends Specification {

    def "validate something "() {

        given:
        def string1 = "hello,"
        def string2 = "world"

        when:
        def concatStrings = string1.concat(string2)
        then:
        concatStrings == "hello,world"

    }
}
