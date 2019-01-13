/*
 * Copyright 2017 original authors
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
package example

import com.amazon.ask.Skills
import com.amazon.ask.model.RequestEnvelope
import com.amazon.ask.model.ResponseEnvelope
import example.handlers.*

/**
 * @author Ryan Vanderwerf
 * @since 1.0
 */

String hello() {
    System.out.println("System env properties:")
    Map<String,String> props = System.getenv()
    props.keySet().each { String key ->
        System.out.println("key=${key} value=${props.get(key)}")
    }
    System.out.println("system props end")

    ClassLoader cl = ClassLoader.getSystemClassLoader();

    URL[] urls = ((URLClassLoader)cl).getURLs();
    System.out.println("system classloader:")
    for(URL url: urls){
        System.out.println(url.getFile());
    }
    System.out.println("system classloader end")
    System.out.println("system properties begin:")
    Properties properties = System.getProperties()
    properties.keySet().each { def key ->
        System.out.println("key=${key} value=${properties.get(key)}")
    }
    System.out.println("system properties end")
}