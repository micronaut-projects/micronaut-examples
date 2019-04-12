package example.mail.ses;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import javax.inject.Singleton;

@Singleton
@Requires(property = "aws.access-key-id")
public class AWSCredentialsProviderService implements AWSCredentialsProvider {

    @Value("${aws.access-key-id}")
    String accessKey;

    @Value("${aws.secret-key}")
    String secretKey;

    @Override
    public AWSCredentials getCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Override
    public void refresh() {

    }
}
