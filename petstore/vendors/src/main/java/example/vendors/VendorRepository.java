package example.vendors;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import org.reactivestreams.Publisher;

import javax.validation.constraints.NotBlank;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface VendorRepository extends ReactorCrudRepository<Vendor, Long> {

    @SingleResult
    Publisher<Vendor> save(@NonNull @NotBlank String name);

    @SingleResult
    Publisher<Vendor> findByName(@NonNull @NotBlank String name);

    @NonNull
    default Vendor update() {
        throw new UnsupportedOperationException("not used");
    }
}