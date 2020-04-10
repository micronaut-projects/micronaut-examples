package micronaut.neo4j.example.repository;

import micronaut.neo4j.example.domain.Musician;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Singleton;

@Singleton
public class MusicianRepository {

    private SessionFactory sessionFactory;

    public MusicianRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Musician findById(Integer id) {
        return getSession().load(Musician.class, id);
    }

    public void save(Musician musician) {
        getSession().save(musician);
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

}
