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
package example.vendors;

import example.api.v1.Pet;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Transient;
import java.util.List;

@MappedEntity
public class Vendor extends example.api.v1.Vendor {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    @Transient
    public List<Pet> getPets() {
        return super.getPets();
    }

    Vendor pets(List<Pet> pets) {
        setPets(pets);
        return this;
    }

    @Override
    public String toString() {
        return String.format("Vendor{id='%s', name='%s'}", id, name);
    }
}
