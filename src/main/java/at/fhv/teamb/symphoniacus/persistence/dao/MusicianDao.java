package at.fhv.teamb.symphoniacus.persistence.dao;

import at.fhv.teamb.symphoniacus.persistence.BaseDao;
import at.fhv.teamb.symphoniacus.persistence.dao.interfaces.IMusicianDao;
import at.fhv.teamb.symphoniacus.persistence.model.MusicianEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IMusicianEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.ISectionEntity;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * DAO for Musicians.
 *
 * @author Valentin Goronjic
 * @author Theresa Gierer
 * @author Dominic Luidold
 */
public class MusicianDao extends BaseDao<IMusicianEntity>
    implements IMusicianDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IMusicianEntity> find(Integer key) {
        return this.find(MusicianEntity.class, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IMusicianEntity> findAllWithSectionAndActiveContract(ISectionEntity section) {
        TypedQuery<MusicianEntity> query = entityManager.createQuery(
            "SELECT m FROM MusicianEntity m "
                + "JOIN FETCH m.user "
                + "LEFT JOIN FETCH m.dutyPositions "
                + "INNER JOIN m.contractualObligations c "
                + "WHERE m.section = :section "
                + "AND c.startDate <= :startDate "
                + "AND c.endDate >= :endDate",
            MusicianEntity.class
        );
        query.setParameter("section", section);
        query.setParameter("startDate", LocalDate.now());
        query.setParameter("endDate", LocalDate.now());

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IMusicianEntity> findExternalsWithSection(ISectionEntity section) {
        TypedQuery<MusicianEntity> query = entityManager.createQuery(
            "SELECT m FROM MusicianEntity m "
                + "JOIN FETCH m.user u "
                + "WHERE u.firstName = :firstName "
                + "AND m.section = :section "
                + "AND m.contractualObligations IS EMPTY",
            MusicianEntity.class
        );

        query.setParameter("firstName", "Extern");
        query.setParameter("section", section);

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IMusicianEntity> persist(IMusicianEntity elem) {
        return this.persist(MusicianEntity.class, elem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IMusicianEntity> update(IMusicianEntity elem) {
        return this.update(MusicianEntity.class, elem);
    }

    @Override
    public boolean remove(IMusicianEntity elem) {
        return false;
    }

    /**
     * Returns Musician matching given userId.
     * @param id given User Id
     * @return Optional Musician
     */
    public Optional<IMusicianEntity> findMusicianByUserId(int id) {
        TypedQuery<MusicianEntity> query = entityManager.createQuery(
            "SELECT m FROM MusicianEntity m "
                + "WHERE m.user.userId = :uid",
            MusicianEntity.class
        );

        query.setParameter("uid", id);

        return Optional.of(query.getSingleResult());
    }

    /**
     * Returns Musician matching given userShortcut.
     * @param userShortcut Shortcut of Musician
     * @return Musician if found, else Optional empty
     */
    public Optional<IMusicianEntity> findMusicianByShortcut(String userShortcut) {
        TypedQuery<MusicianEntity> query = entityManager.createQuery(
                "SELECT m FROM MusicianEntity m "
                        + "WHERE m.user.shortcut = :us",
                MusicianEntity.class
        );

        query.setParameter("us", userShortcut);

        return Optional.of(query.getSingleResult());
    }
}
