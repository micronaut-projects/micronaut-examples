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
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author graemerocher
 * @author wetted
 * @since 1.0
 */
@MappedEntity
public class PetEntity {

    @Id
    @GeneratedValue
    private String id;

    private String slug;

    @NotBlank
    private String image;

    @NotBlank
    private String name;
    protected PetType type = PetType.DOG;

    @NotBlank
    private String vendor;

//    @BsonCreator
    public PetEntity(
//            @BsonProperty("vendor")
            String vendor,
//            @BsonProperty("name")
            String name,
//            @BsonProperty("image")
            String image
    ) {
        this.vendor = vendor;
        this.name = name;
        this.image = image;
    }

    public PetEntity type(PetType type) {
        if(type != null) {
            this.type = type;
        }
        return this;
    }

    public PetEntity slug(String slug) {
        if(slug != null) {
            this.slug = slug;
        }
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotBlank
    public String getSlug() {
        if(slug != null) {
            return slug;
        }
        return vendor + "-" + name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetEntity petEntity = (PetEntity) o;
        return Objects.equals(id, petEntity.id)
                && Objects.equals(slug, petEntity.slug)
                && image.equals(petEntity.image)
                && name.equals(petEntity.name)
                && type == petEntity.type
                && vendor.equals(petEntity.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug, image, name, type, vendor);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", vendor='" + vendor + '\'' +
                ", slug='" + vendor + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}

///**
// * @author graemerocher
// * @since 1.0
// */
//public class PetEntity extends Pet {
//    @BsonCreator
//    @JsonCreator
//    public PetEntity(
//            @JsonProperty("vendor")
//            @BsonProperty("vendor") String vendor,
//            @JsonProperty("name")
//            @BsonProperty("name") String name,
//            @JsonProperty("image")
//            @BsonProperty("image") String image) {
//        super(vendor, name, image);
//    }
//
//    @Override
//    public PetEntity type(PetType type) {
//        return (PetEntity) super.type(type);
//    }
//
//    @Override
//    public PetEntity slug(String slug) {
//        return (PetEntity) super.slug(slug);
//    }
//
//    @Override
//    public void setSlug(String image) {
//        super.setSlug(image);
//    }
//
//    @Override
//    public void setImage(String image) {
//        super.setImage(image);
//    }
//
//    @Override
//    public void setType(PetType type) {
//        super.setType(type);
//    }
//}
