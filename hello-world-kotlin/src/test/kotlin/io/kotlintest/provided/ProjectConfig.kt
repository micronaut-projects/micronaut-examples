package io.kotlintest.provided

import io.kotlintest.AbstractProjectConfig
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension

object ProjectConfig : AbstractProjectConfig() {
    override fun listeners() = listOf(MicronautKotlinTestExtension)
    override fun extensions() = listOf(MicronautKotlinTestExtension)
}
