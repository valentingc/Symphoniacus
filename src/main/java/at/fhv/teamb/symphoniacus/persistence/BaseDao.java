package at.fhv.teamb.symphoniacus.persistence;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class acts as a base for all DAOs by handling the {@link EntityManagerFactory} and
 * {@link EntityManager} and therefore managing the database connections.
 *
 * @param <T> The type of DAO, based on JPA entities
 */
public abstract class BaseDao<T> implements Dao<T> {
    private static final Logger LOG = LogManager.getLogger(BaseDao.class);
    protected static EntityManagerFactory entityManagerFactory =
        Persistence.createEntityManagerFactory(
            "mysqldb"
        );
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();

    /**
     * Finds the object based on the provided primary key.
     *
     * @param clazz The class to use
     * @param key   The primary key to use
     * @return The object
     */
    @SuppressWarnings("unchecked")
    protected Optional<T> find(Class<?> clazz, Integer key) {
        // Disable cache because duty positions could be set.
        //entityManager.clear();
        return Optional.ofNullable((T) entityManager.find(clazz, key));
    }

    /**
     * Persists an object.
     *
     * @param clazz Class of the object
     * @param elem  The object to persist
     * @return Optional.empty if persisting not possible
     */
    protected Optional<T> persist(Class<?> clazz, T elem) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(elem);
            transaction.commit();
            return Optional.of(elem);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOG.error("Could not persist element", e);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing object.
     *
     * @param clazz Class of the object
     * @param elem  The object to update
     * @return Optional.empty if updating not possible
     */
    protected Optional<T> update(Class<?> clazz, T elem) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(elem);
            transaction.commit();
            return Optional.of(elem);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOG.error("Could not update element", e);
        }
        return Optional.empty();
    }

    /**
     * Returns all objects from a class.
     *
     * @param clazz Class of the object
     * @return A List of objects
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll(Class<?> clazz) {
        // Class simpleName can hopefully not be changed by the user
        // so, we don't check for SQL injection here...
        return (List<T>) entityManager.createQuery(
            "Select t from " + clazz.getSimpleName() + " t",
            clazz
        ).getResultList();
    }
}
