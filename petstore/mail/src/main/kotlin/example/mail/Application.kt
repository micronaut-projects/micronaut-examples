package example.mail


import io.micronaut.runtime.Micronaut

import javax.inject.Singleton

/**
 * @author sdelamo
 * @since 1.0
 */
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.run(Application::class.java)
    }
}
