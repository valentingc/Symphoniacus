package at.fhv.teamb.symphoniacus.persistence.dao;

import at.fhv.teamb.symphoniacus.persistence.BaseDao;
import at.fhv.teamb.symphoniacus.persistence.dao.interfaces.IDutyDao;
import at.fhv.teamb.symphoniacus.persistence.model.DutyEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IDutyCategoryEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IDutyEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IInstrumentationEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IMusicianEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.ISectionEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.ISeriesOfPerformancesEntity;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * DAO for Duty class.
 *
 * @author Valentin Goronjic
 * @author Dominic Luidold
 */
public class DutyDao extends BaseDao<IDutyEntity> implements IDutyDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IDutyEntity> find(Integer key) {
        return this.find(DutyEntity.class, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IDutyEntity> persist(IDutyEntity elem) {
        return this.persist(DutyEntity.class, elem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IDutyEntity> update(IDutyEntity elem) {
        return this.update(DutyEntity.class, elem);
    }

    @Override
    public boolean remove(IDutyEntity elem) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> findAllInWeek(LocalDateTime start) {
        return findAllInRange(start, start.plusDays(6));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> findAllInRange(LocalDateTime start, LocalDateTime end) {
        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "JOIN FETCH d.dutyCategory dc "
                + "LEFT JOIN FETCH d.seriesOfPerformances sop "
                + "WHERE d.start >= :start "
                + "AND d.end <= :end",
            DutyEntity.class
        );

        query.setMaxResults(300);
        query.setParameter("start", start);
        query.setParameter("end", end);

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> findAllInWeekWithSection(
        ISectionEntity section,
        LocalDateTime start,
        boolean isReadyForDutyScheduler,
        boolean isReadyForOrganisationManager,
        boolean isPublished
    ) {
        return findAllInRangeWithSection(
            section,
            start,
            start.plusDays(6),
            isReadyForDutyScheduler,
            isReadyForOrganisationManager,
            isPublished
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> findAllInRangeWithSection(
        ISectionEntity section,
        LocalDateTime start,
        LocalDateTime end,
        boolean isReadyForDutyScheduler,
        boolean isReadyForOrganisationManager,
        boolean isPublished
    ) {
        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "INNER JOIN d.sectionMonthlySchedules sms "
                + "INNER JOIN sms.section s "
                + "JOIN FETCH d.dutyCategory dc "
                + "LEFT JOIN FETCH d.seriesOfPerformances sop "
                + "WHERE d.start >= :start AND d.end <= :end "
                + "AND s.sectionId = :sectionId "
                + "AND sms.isReadyForDutyScheduler = :isReadyForDutyScheduler "
                + "AND sms.isReadyForOrganisationManager = :isReadyForOrganisationManager "
                + "AND sms.isPublished = :isPublished",
            DutyEntity.class
        );

        query.setMaxResults(300);
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("sectionId", section.getSectionId());
        query.setParameter("isReadyForDutyScheduler", isReadyForDutyScheduler);
        query.setParameter("isReadyForOrganisationManager", isReadyForOrganisationManager);
        query.setParameter("isPublished", isPublished);

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> getAllDutiesInRangeFromMusician(
        IMusicianEntity musician,
        LocalDate month
    ) {
        YearMonth yearMonth = YearMonth.from(month);
        LocalDate start = yearMonth.atDay(1); // Find first day of month
        LocalDate end = yearMonth.atEndOfMonth(); // Find last day of month
        LocalDateTime startWithTime = start.atStartOfDay();
        LocalDateTime endWithTime = end.atStartOfDay();

        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "JOIN FETCH d.dutyPositions dp "
                + "JOIN FETCH dp.musician m "
                + "JOIN FETCH m.contractualObligations co "
                + "JOIN FETCH d.dutyCategory "
                + "WHERE d.end <= :end "
                + "AND d.start >= :start "
                + "AND m = :musician "
                + "AND co.startDate <= :currentDate "
                + "AND co.endDate >= :currentDate",
            DutyEntity.class
        );

        query.setParameter("musician", musician);
        query.setParameter("start", startWithTime);
        query.setParameter("end", endWithTime);
        query.setParameter("currentDate", LocalDate.now());

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<IDutyEntity> getAllDutiesOfMusicians(
        List<IMusicianEntity> musicians,
        LocalDate month
    ) {
        YearMonth yearMonth = YearMonth.from(month);
        LocalDate start = yearMonth.atDay(1); // Find first day of month
        LocalDate end = yearMonth.atEndOfMonth(); // Find last day of month
        LocalDateTime startWithTime = start.atStartOfDay();
        LocalDateTime endWithTime = end.atStartOfDay();

        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "INNER JOIN d.dutyPositions dp "
                + "INNER JOIN dp.musician m "
                + "WHERE d.end <= :end "
                + "AND d.start >= :start AND m IN :musicians",
            DutyEntity.class
        );

        query.setParameter("start", startWithTime);
        query.setParameter("end", endWithTime);
        query.setParameter("musicians", musicians);

        return new LinkedHashSet<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> getOtherDutiesForSeriesOfPerformances(
        ISeriesOfPerformancesEntity sop,
        LocalDateTime dutyStart,
        Integer maxNumberOfDuties
    ) {
        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "INNER JOIN d.seriesOfPerformances sop "
                + "WHERE sop.seriesOfPerformancesId = :sopId "
                + "AND d.start < :dutyStart ",
            DutyEntity.class
        );

        query.setParameter("dutyStart", dutyStart);
        query.setParameter("sopId", sop.getSeriesOfPerformancesId());
        query.setMaxResults(maxNumberOfDuties);

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDutyEntity> getOtherDutiesForSection(
        IDutyEntity duty,
        ISectionEntity section,
        Integer maxNumberOfDuties
    ) {
        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                + "INNER JOIN d.sectionMonthlySchedules sms "
                + "WHERE sms.section.sectionId = :sectionId "
                + "AND d.seriesOfPerformances IS NULL "
                + "AND d.dutyCategory.dutyCategoryId = :dutyCategoryId ",
            DutyEntity.class
        );
        query.setParameter("sectionId", section.getSectionId());
        query.setParameter("dutyCategoryId", duty.getDutyId());
        query.setMaxResults(maxNumberOfDuties);

        return new LinkedList<>(query.getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesDutyAlreadyExists(
        ISeriesOfPerformancesEntity series,
        List<IInstrumentationEntity> instrumentations,
        LocalDateTime startingDate,
        LocalDateTime endingDate,
        IDutyCategoryEntity category
    ) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(d) FROM DutyEntity d "
                + "LEFT JOIN d.dutyPositions dp "
                + "LEFT JOIN dp.instrumentationPosition ip "
                + "WHERE ip.instrumentation IN :inst "
                + "AND d.start = :sDate "
                + "AND d.end = :eDate "
                + "AND d.seriesOfPerformances = :series "
                + "AND d.dutyCategory = :category ",
            Long.class
        );

        query.setParameter("series", series);
        query.setParameter("sDate", startingDate);
        query.setParameter("eDate", endingDate);
        query.setParameter("inst", instrumentations);
        query.setParameter("category", category);
        return (query.getSingleResult() >= 1);
    }

    /**
     * Finds future unscheduled duties where wishes can still be made for a
     * given Musician's section.
     * @param section Section of User
     * @return List of Duties
     */
    public List<IDutyEntity> findFutureUnscheduledDuties(ISectionEntity section) {
        TypedQuery<DutyEntity> query = entityManager.createQuery(
            "SELECT d FROM DutyEntity d "
                    + "INNER JOIN d.sectionMonthlySchedules sms "
                    + "INNER JOIN sms.monthlySchedule ms "
                    + "INNER JOIN sms.section s "
                    + "JOIN FETCH d.dutyCategory dc "
                    + "LEFT JOIN FETCH d.seriesOfPerformances sop "
                    + "WHERE d.start >= :start "
                    + "AND s.sectionId = :sectionId "
                    + "AND sms.isReadyForDutyScheduler = :isReadyForDutyScheduler "
                    + "AND sms.isReadyForOrganisationManager = :isReadyForOrganisationManager "
                    + "AND sms.isPublished = :isPublished "
                    + "AND ms.endWish >= :start",
            DutyEntity.class
        );

        query.setMaxResults(300);
        query.setParameter("start", LocalDateTime.now());
        query.setParameter("sectionId", section.getSectionId());
        query.setParameter("isReadyForDutyScheduler", false);
        query.setParameter("isReadyForOrganisationManager", false);
        query.setParameter("isPublished", false);

        return new LinkedList<>(query.getResultList());
    }
}
