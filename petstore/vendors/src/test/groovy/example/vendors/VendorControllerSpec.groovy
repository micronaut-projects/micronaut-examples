/*
 * Copyright 2018 original authors
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
package example.vendors

import example.api.v1.Name
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.reactor.http.client.ReactorHttpClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

/**
 * @author graemerocher
 * @since 1.0
 */
@MicronautTest
@Property(name = "consul.client.enabled", value = "false")
class VendorControllerSpec extends Specification {

    @Value('${vendors.api.version}')
    String apiVersion

    @Inject
    @Client('/${vendors.api.version}/vendors')
    ReactorHttpClient client;

    void "find non-existing vendor should return 404"() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/99'))

        then:
        HttpClientResponseException exception = thrown()

        exception.response
        exception.status == HttpStatus.NOT_FOUND
    }

    void 'test list vendors'() {
        when:
        HttpRequest<?> request = HttpRequest.GET("/list")
        List<Vendor> vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor))

        then:
        vendors.size() == 0

        when:
        request = HttpRequest.POST('/', [name: 'Pet Smart'])
        client.toBlocking().exchange(request)
        request = HttpRequest.POST('/', [name: 'Chewy'])
        client.toBlocking().exchange(request)
        request = HttpRequest.GET("/list")
        vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor))

        then:
        vendors.size() == 2
    }

    @Unroll
    void "vendor CRUD operations"() {
        given: "verify post/create operation - adding #entries.value"
        HttpRequest<?> request = HttpRequest.POST('/', [name: 'Pet Smart'])

        when:
        HttpResponse<?> response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED

        when:
        request = HttpRequest.POST('/', [name: 'Chewy'])
        response = client.toBlocking().exchange(request, Vendor)

        then:
        response.status == HttpStatus.CREATED
        response.body.get().name == 'Chewy'

        when:
        Long chewyId = response.body.get().id

        then:
        noExceptionThrown()

        when: "verify get/read operation"
        URI chewyUri = UriBuilder.of("/").path(""+chewyId).build()
        Vendor vendor = client.toBlocking().retrieve(chewyUri.toString(), Vendor)

        then:
        vendor.name == 'Chewy'

        when: "verify list operation"
        request = HttpRequest.GET("/list")
        List<Vendor> vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor))

        then:
        vendors.size() == 2

        when: "verify names operation"
        request = HttpRequest.GET("/names")
        List<Name> names = client.toBlocking().retrieve(request, Argument.listOf(Name))

        then:
        names.size() == 2
        names.stream().map(Name::getName).collect(Collectors.toList()).sort() == ['Chewy', 'Pet Smart'] as List
    }
}
