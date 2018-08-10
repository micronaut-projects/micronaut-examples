package example.storefront

import example.api.v1.HealthStatus
import example.storefront.client.v1.Comment
import example.storefront.client.v1.CommentClient
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Parameter
import io.reactivex.Single
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author zacharyklein
 * @since 1.0
 */
@Singleton
@Controller("/comment")
@CompileStatic
class CommentController {

    final CommentClient commentClient

    CommentController(CommentClient commentClient) {
        this.commentClient = commentClient
    }

    @Get('/health')
    Single<HealthStatus> health() {
        commentClient.health().onErrorReturn({ new HealthStatus('DOWN') })
    }

    @Get('/{topic}')
    List<Comment> topics(@Parameter String topic) {
        commentClient.list topic
    }

    @Get('/{topic}/{id}')
    Map<String, Object> thread(Long id) {
        commentClient.expand id
    }

    @Post('/{topic}')
    HttpStatus addTopic(String topic, @Body Comment comment) {
        commentClient.add topic, comment.poster, comment.content
    }

    @Post('/{topic}/{id}')
    HttpStatus addReply(@Parameter Long id, @Body Comment comment) {
        commentClient.addReply id, comment.poster, comment.content
    }
}
