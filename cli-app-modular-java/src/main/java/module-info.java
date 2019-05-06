module cli.app.modular.java {
    requires io.micronaut.runtime;
    requires io.micronaut.configuration.picocli;
    requires io.micronaut.inject;
    requires info.picocli;

    requires transitive java.sql;
}