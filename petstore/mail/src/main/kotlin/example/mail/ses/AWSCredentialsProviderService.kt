package example.mail.ses

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
@Requires(property = "aws.accessKeyId")
class AWSCredentialsProviderService : AWSCredentialsProvider {

    @Value("\${aws.accessKeyId}")
    internal var accessKey: String? = null

    @Value("\${aws.secretKey}")
    internal var secretKey: String? = null

    override fun getCredentials(): AWSCredentials {
        return BasicAWSCredentials(accessKey!!, secretKey!!)
    }

    override fun refresh() {

    }
}
