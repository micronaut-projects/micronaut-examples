package example.comments.repository;

import example.comments.domain.Comment;
import example.comments.domain.Topic;
import jakarta.inject.Singleton;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class CommentRepository {
    private final SessionFactory sessionFactory;
    private final TopicRepository topicRepository;

    public CommentRepository(SessionFactory sessionFactory, TopicRepository topicRepository) {
        this.sessionFactory = sessionFactory;
        this.topicRepository = topicRepository;
    }

    public Comment saveComment(@NotBlank String topicTitle, @NotBlank String poster, @NotBlank String content) {
        Topic topic = topicRepository.findByTitle(topicTitle);
        if (topic == null) {
            topic = new Topic().setTitle(topicTitle);
        }

        Comment comment = new Comment()
                .setPoster(poster)
                .setContent(content);
//        saveOrUpdate(comment); // not in groovy version
        topic.getComments().add(comment);
        topicRepository.saveOrUpdate(topic);
        return comment;
    }

    public void saveOrUpdate(Comment comment) {
        getSession().save(comment);
    }


    public Comment saveReply(@NotNull Comment replyTo, @NotBlank String poster, @NotBlank String content) {
        Comment comment = new Comment()
                .setPoster(poster)
                .setContent(content);
        saveOrUpdate(comment);

        replyTo.addToReplies(comment);
        saveOrUpdate(replyTo);
        return comment;
    }

    public Comment saveReply(@NotNull Long commentId, @NotBlank String poster, @NotBlank String content) {
        Comment replyTo = getSession().load(Comment.class, commentId);
        if (replyTo != null) {
            Comment comment = new Comment()
                    .setPoster(poster)
                    .setContent(content);
            replyTo.addToReplies(comment);
            saveOrUpdate(comment);
            saveOrUpdate(replyTo);
            return comment;
        }
        return null;
    }

    public List<Comment> findComments(String topicTitle) {

        // I didn'topic get this to work
//        String query = "MATCH (topic:Topic)-[:COMMENTS]->(c:Comment) " +
//                "WHERE topic.title = $title RETURN $c ORDER BY $c.dateCreated";
//        Map<String,Object> params = Collections.singletonMap( "title", topicTitle);
//        Iterable<Comment> comments = session.query(Comment.class, query, params);
//        return StreamSupport.stream(comments.spliterator(), false).collect(Collectors.toList());

        // this works
        Session session = getSession();
        Topic topic = session.load(Topic.class, topicTitle, 1);
        return topic.getComments();
    }

    // this is from the old Groovy code
//    @Cypher("""MATCH p=${Comment from}-[:REPLIES*..]->${Comment leaf}
//               WHERE ID($from) = $commentId
//               RETURN DISTINCT p""")
//    abstract List<Path> findCommentNeo4jPaths(Long commentId)

    /**
     * Translates the Neo4j graph data into a JSON representation of graph data
     *
     * @param commentId
     * @return
     */
    public Map<String, Object> findReplies(Long commentId) {
        // this needs to get the tree of reply comments rooted at the comment identified by commentId
        return Collections.emptyMap();

        // this is from the old Groovy code
//        List<grails.neo4j.Path<Comment, Comment>> paths = findCommentNeo4jPaths(commentId).collect { p -> p as grails.neo4j.Path<Comment, Comment> }
//
//        if(paths.isEmpty()) {
//            return Collections.emptyMap()
//        }
//
//        Map<String, Object> data = [:]
//        for(grails.neo4j.Path<Comment, Comment> p in paths) {
//            Iterator<Comment> comments = p.nodes().iterator()
//            Map<String, Object> current = data
//            // first entry is the root element
//            if(comments.hasNext()) {
//                Comment c = comments.next()
//                fillMap(current, c)
//            }
//
//            // remaining elements are the children
//            if(comments.hasNext()) {
//                List<Map> children = (List<Map>) current.computeIfAbsent("replies", { key -> []})
//                while(comments.hasNext()) {
//                    Comment c = comments.next()
//                    Map<String, Object> childData = children.find { it.get('id') == c.id }
//                    if(childData == null) {
//                        childData = [:]
//                        children.add(childData)
//                    }
//                    fillMap(childData, c)
//                    if(comments.hasNext()) {
//                        children = (List<Map>) childData.computeIfAbsent("replies", { key -> []})
//                    }
//                }
//            }
//        }
//        return data
    }

    private static void fillMap(Map<String, Object> current, Comment c) {
        current.put("id", c.getId());
        current.put("poster", c.getPoster());
        current.put("content", c.getContent());
    }

    public Collection<Comment> findAll() {
        return getSession().loadAll(Comment.class);
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

}
