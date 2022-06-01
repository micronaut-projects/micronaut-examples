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
package example.function.tweet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

/**
 * @author graemerocher
 * @since 1.0
 */
public class UpdateResult {

    private final URL url;
    private final long createdAt;

    @JsonCreator
    public UpdateResult(@JsonProperty("url") URL url, @JsonProperty("createdAt") long createdAt) {
        this.url = url;
        this.createdAt = createdAt;
    }

}
