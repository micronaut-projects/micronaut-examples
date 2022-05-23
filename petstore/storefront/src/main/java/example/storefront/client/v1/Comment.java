package example.storefront.client.v1;

import java.util.Date;

/**
 * @author zacharyklein
 * @since 1.0
 */
public class Comment implements example.api.v1.Comment {
    private Long id;
    private String poster;
    private String content;
    private Date dateCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    @Override
    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }
}
