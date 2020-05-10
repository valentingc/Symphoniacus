package at.fhv.teamb.symphoniacus.application;

import at.fhv.teamb.symphoniacus.domain.Duty;
import at.fhv.teamb.symphoniacus.domain.DutyCategory;
import at.fhv.teamb.symphoniacus.domain.Section;
import at.fhv.teamb.symphoniacus.persistence.PersistenceState;
import at.fhv.teamb.symphoniacus.persistence.dao.DutyDao;
import at.fhv.teamb.symphoniacus.persistence.model.DutyEntity;
import at.fhv.teamb.symphoniacus.persistence.model.MonthlyScheduleEntity;
import at.fhv.teamb.symphoniacus.persistence.model.SectionEntity;
import at.fhv.teamb.symphoniacus.persistence.model.SeriesOfPerformancesEntity;
import at.fhv.teamb.symphoniacus.persistence.model.WeeklyScheduleEntity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for finding {@link DutyEntity} objects based on a range of time and
 * {@link SectionEntity}.
 *
 * @author Nino Heinzle
 * @author Dominic Luidold
 */
public class DutyManager {
    private static final Logger LOG = LogManager.getLogger(DutyManager.class);
    private final MonthlyScheduleManager monthlyScheduleManager;
    private final WeeklyScheduleManager weeklyScheduleManager;
    protected DutyDao dutyDao;

    /**
     * Initialize the DutyManager.
     */
    public DutyManager() {
        this.monthlyScheduleManager = new MonthlyScheduleManager();
        this.weeklyScheduleManager = new WeeklyScheduleManager();
        this.dutyDao = new DutyDao();
    }

    /**
     * Returns the Monday of the week based on the {@link LocalDate}.
     *
     * @param givenDate The date to determine the monday of the week
     * @return A LocalDate representing the monday of the week
     */
    public static LocalDate getLastMondayDate(LocalDate givenDate) {
        // Will always jump back to last monday
        return givenDate.with(DayOfWeek.MONDAY);
    }

    /**
     * Converts {@link DutyEntity} objects to {@link Duty} objects.
     *
     * @param entities The entities to convert
     * @return A List of Duty objects
     */
    public static List<Duty> convertEntitiesToDomainObjects(List<DutyEntity> entities) {
        List<Duty> duties = new LinkedList<>();
        for (DutyEntity entity : entities) {
            duties.add(new Duty(entity));
        }
        return duties;
    }

    /**
     * Returns a loaded duty from its id.
     *
     * @param dutyId The identifier of this duty
     * @return A minimal-loaded duty
     */
    public Optional<Duty> loadDutyDetails(Integer dutyId) {
        Optional<DutyEntity> dutyEntity = this.dutyDao.find(dutyId);

        if (dutyEntity.isPresent()) {
            Duty d = new Duty(dutyEntity.get());
            return Optional.of(d);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Finds all duties within a full week (any Date can be given).
     * converts the given Date the last Monday
     *
     * @param start is any given Date
     * @return a List of all matching duties
     */
    public List<Duty> findAllInWeek(LocalDate start) {
        List<DutyEntity> entityList = this.dutyDao.findAllInWeek(
            getLastMondayDate(start).atStartOfDay()
        );

        List<Duty> dutyList = new LinkedList<>();
        for (DutyEntity entity : entityList) {
            dutyList.add(new Duty(entity));
        }

        return dutyList;
    }

    /**
     * Finds all duties in a within the week of a given Date for a section.
     *
     * @param sectionOfUser The section of the current user
     * @param start         A LocalDate that represents the start
     * @return A List of the matching duties
     */
    public List<Duty> findAllInWeekWithSection(
        SectionEntity sectionOfUser,
        LocalDate start
    ) {
        return convertEntitiesToDomainObjects(
            this.dutyDao.findAllInWeekWithSection(
                sectionOfUser,
                getLastMondayDate(start).atStartOfDay(),
                false,
                false,
                false
            )
        );
    }

    /**
     * Finds all duties in a specific range of time for a section.
     *
     * @param sectionOfUser The section of the current user
     * @param start         A LocalDate that represents the start
     * @param end           A LocalDate that represents the end
     * @return A List of the matching duties
     */
    public List<Duty> findAllInRangeWithSection(
        SectionEntity sectionOfUser,
        LocalDate start,
        LocalDate end
    ) {
        return convertEntitiesToDomainObjects(
            this.dutyDao.findAllInRangeWithSection(
                sectionOfUser,
                start.atStartOfDay(),
                end.atStartOfDay(),
                true, // TODO - Add logic to determine which parameters are true
                false,
                false
            )
        );
    }

    /**
     * Finds all duties in a specific range of time.
     *
     * @param start A LocalDate that represents the start
     * @param end   A LocalDate that represents the end
     * @return A List of the matching duties
     */
    public List<Duty> findAllInRange(LocalDate start, LocalDate end) {
        return convertEntitiesToDomainObjects(
            this.dutyDao.findAllInRange(
                start.atStartOfDay(),
                end.atStartOfDay()
            )
        );
    }

    /**
     * TODO JAVADOC.
     *
     * @return
     */
    public Optional<List<Duty>> getOtherDutiesForSopOrSection(
        Duty duty,
        Section section,
        Integer numberOfDuties
    ) {
        // Look whether it is a SoP or not.
        if (duty == null) {
            LOG.error("Cannot getLastDuties when duty is null");
            return Optional.empty();
        }

        SeriesOfPerformancesEntity sop = duty
            .getEntity()
            .getSeriesOfPerformances();

        List<DutyEntity> resultList = null;
        if (sop.getSeriesOfPerformancesId() != null) {
            // get last duties for this SoP
            resultList = this.dutyDao
                .getOtherDutiesForSeriesOfPerformances(sop, duty.getEntity().getStart(),
                    numberOfDuties);
        } else {
            // get last duties of section
            // TODO change this go get last 5 non-series of performances-duties
            resultList = this.dutyDao.getOtherDutiesForSection(
                duty.getEntity(),
                section.getEntity(),
                numberOfDuties
            );
        }

        if (resultList == null || resultList.isEmpty()) {
            LOG.error("No results found for getOtherDutiesForSopOrSection");
            return Optional.empty();
        }

        return Optional.of(
            convertEntitiesToDomainObjects(
                resultList
            )
        );
    }

    /**
     * Creates a new {@link Duty} domain object based on given data.
     *
     * @param dutyCategory The duty category to use
     * @param description  The description to use
     * @param timeOfDay    The time of day description
     * @param start        The start of the duty
     * @param end          The end of the duty
     * @param sop          The {@link SeriesOfPerformancesEntity} to use
     * @return A duty domain object
     */
    public Duty createDuty(
        DutyCategory dutyCategory,
        String description,
        String timeOfDay,
        LocalDateTime start,
        LocalDateTime end,
        SeriesOfPerformancesEntity sop
    ) {
        // Get monthly schedule entity
        MonthlyScheduleEntity monthlyScheduleEntity =
            this.monthlyScheduleManager.createIfNotExists(YearMonth.from(start.toLocalDate()));
        // Get weekly schedule entity
        WeeklyScheduleEntity weeklyScheduleEntity =
            this.weeklyScheduleManager.createIfNotExists(start.toLocalDate(), start.getYear());

        // Add weekly schedule to monthly schedule and vice versa
        monthlyScheduleEntity.addWeeklySchedule(weeklyScheduleEntity);

        // Create duty entity
        DutyEntity dutyEntity = new DutyEntity();

        // Add duty to weekly schedule and vice versa
        weeklyScheduleEntity.addDuty(dutyEntity);

        // Fill duty entity with data
        dutyEntity.setDutyCategory(dutyCategory.getEntity());
        dutyEntity.setDescription(description);
        dutyEntity.setTimeOfDay(timeOfDay);
        dutyEntity.setStart(start);
        dutyEntity.setEnd(end);
        dutyEntity.setSeriesOfPerformances(sop);

        // Return domain object
        return new Duty(dutyEntity);
    }

    /**
     * Persists a new {@link Duty} object to the database.
     *
     * <p>The method will subsequently change the {@link PersistenceState} of the object
     * from {@link PersistenceState#EDITED} to {@link PersistenceState#PERSISTED}, provided
     * that the database update was successful.
     *
     * @param duty The duty to save
     */
    public void save(Duty duty) {
        Optional<DutyEntity> persisted = this.dutyDao.persist(duty.getEntity());

        if (persisted.isPresent()) {
            duty.setPersistenceState(PersistenceState.PERSISTED);
            LOG.debug(
                "Persisted duty {{}, '{}'}",
                duty.getEntity().getDutyId(),
                duty.getTitle()
            );
        } else {
            LOG.error(
                "Could not persist duty {{}, '{}'}",
                duty.getEntity().getDutyId(),
                duty.getTitle()
            );
        }
    }

    /**
     * Persists an existing {@link Duty} object to the database.
     *
     * <p>The method will subsequently change the {@link PersistenceState} of the object
     * from {@link PersistenceState#EDITED} to {@link PersistenceState#PERSISTED}, provided
     * that the database update was successful.
     *
     * @param duty The duty to update
     */
    public void update(Duty duty) {
        Optional<DutyEntity> persisted = this.dutyDao.update(duty.getEntity());

        if (persisted.isPresent()) {
            duty.setPersistenceState(PersistenceState.PERSISTED);
            LOG.debug(
                "Persisted duty {{}, '{}'}",
                duty.getEntity().getDutyId(),
                duty.getTitle()
            );
        } else {
            LOG.error(
                "Could not persist duty {{}, '{}'}",
                duty.getEntity().getDutyId(),
                duty.getTitle()
            );
        }
    }
}
