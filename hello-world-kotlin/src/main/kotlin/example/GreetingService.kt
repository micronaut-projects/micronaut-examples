package example

import javax.inject.Singleton

@Singleton
class GreetingService {

    fun greet(name: String) = "Hello $name"
}