/*
 * Copyright 2022 original authors
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import example.api.v1.Pet;
import example.api.v1.PetType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

/**
 * @author graemerocher
 * @author wetted
 * @since 1.0
 */
@MappedEntity
public class PetEntity extends Pet {

    @Id
    @GeneratedValue
    private String id;

    @BsonCreator
    @JsonCreator
    public PetEntity(
            @JsonProperty("vendor") @BsonProperty("vendor")
            String vendor,
            @JsonProperty("name") @BsonProperty("name")
            String name,
            @JsonProperty("image") @BsonProperty("image")
            String image
    ) {
        super(vendor, name, image);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public PetEntity type(PetType type) {
        return (PetEntity) super.type(type);
    }

    @Override
    public PetEntity slug(String slug) {
        return (PetEntity) super.slug(slug);
    }

    public void setSlug(String slug) {
        super.setSlug(slug);
    }

    public void setImage(String image) {
        super.setImage(image);
    }


    public void setType(PetType type) {
        super.setType(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetEntity)) return false;

        PetEntity petEntity = (PetEntity) o;

        return Objects.equals(id, petEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}