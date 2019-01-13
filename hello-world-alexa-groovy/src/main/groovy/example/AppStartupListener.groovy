package example

import groovy.util.logging.Slf4j
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceStartedEvent

@Slf4j
class AppStartupListener implements ApplicationEventListener<ServiceStartedEvent> {
    @Override
    void onApplicationEvent(ServiceStartedEvent event) {
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
            System.out.println("key=${key} value=${props.get(key)}")
        }
        System.out.println("system properties end")
    }
}
