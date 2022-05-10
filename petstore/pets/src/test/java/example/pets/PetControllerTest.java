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
package example.pets;

import example.api.v1.PetType;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author graemerocher
 * @author wetted
 * @since 1.0
 */
public class PetControllerTest extends BaseMongoDataTest {

    @Test
    void emptyDatabaseContainsNoPets() {
        assertEquals(0, petClient.list().size());
    }

    @Test
    void testInteractionWithTheController() {
        List<PetEntity> pets = petClient.list();
        assertThat(pets.size()).isEqualTo(0);

        PetEntity expected = new PetEntity("Fred", "Harry","photo-1457914109735-ce8aba3b7a79.jpeg")
                .type(PetType.CAT);

        HttpResponse<PetEntity> response = petClient.save(expected);
        assertEquals(HttpStatus.CREATED,response.getStatus());

        Optional<PetEntity> body = response.getBody();
        assertThat(body.isPresent()).isTrue();
        PetEntity harry = body.get();
        assertAll(
                () -> assertThat(harry.getImage()).isEqualTo(expected.getImage()),
                () -> assertThat(harry.getName()).isEqualTo(expected.getName()),
                () -> assertThat(harry.getType()).isEqualTo(expected.getType())
        );

        pets.addAll(petClient.list());
        assertAll(
                () -> assertThat(pets.size()).isEqualTo(1),
                () -> assertThat(pets.iterator().next()).isEqualTo(harry)
        );
    }

    @Test
    public void testNextFindByVendor() {

        HttpResponse<PetEntity> response = petClient.save(
                new PetEntity("Fred", "Ron","photo-1442605527737-ed62b867591f.jpeg")
                        .type(PetType.DOG));
        assertEquals(HttpStatus.CREATED,response.getStatus());
        assertThat(response.getBody().isPresent()).isTrue();
        PetEntity ron = response.getBody().get();

        assertThat(petClient.findByVendor("Fred").size()).isEqualTo(1);

        response = petClient.save(new PetEntity("Fred", "Harry","photo-1457914109735-ce8aba3b7a79.jpeg")
                .type(PetType.CAT));
        PetEntity harry = response.getBody().get();

        // this should not add another record
        petClient.save(new PetEntity("Fred", "Harry","photo-1457914109735-ce8aba3b7a79.jpeg")
                .type(PetType.CAT));
        assertThat(petClient.findByVendor("Fred").size()).isEqualTo(2);

        Optional<PetEntity> random = petClient.random();
        assertThat(random.isPresent()).isTrue();
        assertThat(random.get()).isIn(ron, harry);
    }

    @Test
    public void testRandom() {
        HttpResponse<PetEntity> response = petClient.save(
                new PetEntity("Fred", "Ron","photo-1442605527737-ed62b867591f.jpeg")
                        .type(PetType.DOG));
        assertThat(response.getBody().isPresent()).isTrue();
        PetEntity ron = response.getBody().get();

        response = petClient.save(
                new PetEntity("Fred", "Harry","photo-1457914109735-ce8aba3b7a79.jpeg")
                        .type(PetType.CAT));
        assertThat(response.getBody().isPresent()).isTrue();
        PetEntity harry = response.getBody().get();

        response = petClient.save(
                new PetEntity("Dean", "Brahms","photo-1442605527737-ed62b867591f.jpeg")
                        .type(PetType.DOG));
        assertThat(response.getBody().isPresent()).isTrue();
        PetEntity brahms = response.getBody().get();

        Optional<PetEntity> random = petClient.random();
        assertThat(random.isPresent()).isTrue();
        assertThat(random.get()).isIn(ron, harry, brahms);
    }


    @Test
    void testValidationFailsOnBadPet() {
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> petClient.save(new PetEntity("", "","")));
        Arrays.asList(
                "save.pet.name: must not be blank",
                "save.pet.image: must not be blank",
                "save.pet.vendor: must not be blank"
        ).forEach(s -> assertThat(exception.getMessage()).contains(s));
    }

    @Test
    void testSameSlugReturnsSamePet() {
        HttpResponse<PetEntity> response = petClient.save(
                new PetEntity("Fred", "Ron","photo-1442605527737-ed62b867591f.jpeg")
                        .type(PetType.DOG));
        assertThat(response.getBody().isPresent()).isTrue();
        PetEntity ron = response.getBody().get();
        assertThat(ron.getSlug()).isNotNull();

        response = petClient.save(
                new PetEntity("Fred", "Ron","photo-1442605527737-ed62b867591f.jpeg")
                        .type(PetType.DOG));
        assertThat(response.getBody().isPresent()).isTrue();
        assertThat(response.getBody().get()).isEqualTo(ron);
    }
}
