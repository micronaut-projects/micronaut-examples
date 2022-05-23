package example.api.v1;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;

public interface HealthStatusOperation {
    @Get("/health")
    @SingleResult
    Publisher<HealthStatus> health();
}
