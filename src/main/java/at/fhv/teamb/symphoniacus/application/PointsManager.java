package at.fhv.teamb.symphoniacus.application;

import at.fhv.teamb.symphoniacus.domain.Points;
import at.fhv.teamb.symphoniacus.persistence.dao.ContractualObligationDao;
import at.fhv.teamb.symphoniacus.persistence.dao.DutyCategoryChangeLogDao;
import at.fhv.teamb.symphoniacus.persistence.dao.DutyDao;
import at.fhv.teamb.symphoniacus.persistence.model.ContractualObligationEntity;
import at.fhv.teamb.symphoniacus.persistence.model.DutyCategoryChangelogEntity;
import at.fhv.teamb.symphoniacus.persistence.model.DutyCategoryEntity;
import at.fhv.teamb.symphoniacus.persistence.model.DutyEntity;
import at.fhv.teamb.symphoniacus.persistence.model.MusicianEntity;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * UseCase Controller responsible for delivering all requested Points of a musician w/ timespan.
 *
 * @author Danijel Antonijevic
 * @author Nino Heinzle
 */
public class PointsManager {
    private static final Logger LOG = LogManager.getLogger(PointsManager.class);
    private DutyDao dutyDao;
    private ContractualObligationDao conDao;
    private DutyCategoryChangeLogDao dutyCatChangeDao;

    public PointsManager() {
        dutyDao = new DutyDao();
        conDao = new ContractualObligationDao();
        dutyCatChangeDao = new DutyCategoryChangeLogDao();
    }

    /**
     * Responsible for giving the Debit (Soll-Punkte)
     * Points from a given Musician as a Points Object (implements getValue).
     *
     * @param musician point of interest
     * @return Points domain object containing the number of Points (getValue)
     */
    public Optional<Points> getDebitPointsFromMusician(MusicianEntity musician) {

        Optional<ContractualObligationEntity> conEntity =
            conDao.getContractualObligation(musician);
        if (conEntity.isPresent()) {
            return (Points.calcDebitPoints(conEntity.get()));
        }
        return Optional.empty();
    }

    /**
     * Responsible for giving the Gained (Ist-Punkte)
     * Points from a given musician+month as a Points Object (implements getValue).
     *
     * @param musician point of interest
     * @param month    can be any day of any month
     * @return Points domain object containing the number of Points (getValue)
     */
    public Optional<Points> getGainedPointsForMonthFromMusician(
        MusicianEntity musician,
        LocalDate month
    ) {
        DutyDao dutyDao = new DutyDao();


        // get all duties from a musician in a given month
        List<DutyEntity> duties = dutyDao.getAllDutiesInRangeFromMusician(musician, month);
        Set<DutyCategoryEntity> dutyCategories = new LinkedHashSet<>();

        // get all dutyCategories to all duties
        for (DutyEntity d : duties) {
            dutyCategories.add(d.getDutyCategory());
        }

        List<DutyCategoryChangelogEntity> dutyCategoryChangelogs = new LinkedList<>();

        // get all dutyCategoryChangelogs to all dutyCategories
        for (DutyCategoryEntity dc : dutyCategories) {
            List<DutyCategoryChangelogEntity> changelogEntityList =
                dutyCatChangeDao.getDutyCategoryChangeLog(dc);
            if (changelogEntityList != null) {
                dutyCategoryChangelogs.addAll(changelogEntityList);
            }
        }
        return (Points.calcGainedPoints(duties, dutyCategoryChangelogs));
    }

    /**
     * Responsible for giving the Gained (Saldo-Punkte)
     * Points from a given musician+month as a Points Object (implements getValue).
     *
     * @param musician point of interest
     * @param month    can be any day of any month
     * @return Points domain object containing the number of Points (getValue)
     */
    public Optional<Points> getBalanceFromMusician(MusicianEntity musician, LocalDate month) {
        // get all duties from a musician in a given month
        List<DutyEntity> duties = dutyDao.getAllDutiesInRangeFromMusician(musician, month);
        Set<DutyCategoryEntity> dutyCategories = new LinkedHashSet<>();

        // get all dutyCategories to all duties
        for (DutyEntity d : duties) {
            dutyCategories.add(d.getDutyCategory());
        }
        return Points.calcBalancePoints(duties, dutyCategories);
    }
}
