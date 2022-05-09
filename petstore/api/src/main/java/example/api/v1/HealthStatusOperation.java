package example.api.v1;

import io.micronaut.http.annotation.Get;
import io.reactivex.Single;

public interface HealthStatusOperation {
    @Get("/health")
    Single<HealthStatus> health();
}
