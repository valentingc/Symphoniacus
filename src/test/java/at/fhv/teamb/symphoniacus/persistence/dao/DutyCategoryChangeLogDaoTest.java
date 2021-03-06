package at.fhv.teamb.symphoniacus.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.fhv.teamb.symphoniacus.persistence.dao.interfaces.IDutyCategoryChangeLogDao;
import at.fhv.teamb.symphoniacus.persistence.model.DutyCategoryEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IDutyCategoryChangelogEntity;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IDutyCategoryEntity;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Tests for ContractualObligation.
 *
 * @author Nino Heinzle
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DutyCategoryChangeLogDaoTest {
    private static final Logger LOG = LogManager.getLogger(DutyCategoryChangeLogDaoTest.class);
    private IDutyCategoryChangeLogDao dao;

    @BeforeAll
    void initialize() {
        dao = new DutyCategoryChangeLogDao();
    }

    /**
     * Tests if returning List is not null.
     * Tests if the given DutyCategory.Id matches with every DutyCategoryChangeLog.DutyCategoryId
     * in the returning List of DutyCategoryChangelogEntity.
     * Working DB Connection on the Dao is required.
     */
    @Test
    void getDutyCategoryChangeLog_ShouldReturnListOfCategoryChangeLog() {
        // Given
        IDutyCategoryEntity categoryEntity = new DutyCategoryEntity();
        categoryEntity.setDutyCategoryId(3);

        List<IDutyCategoryChangelogEntity> changelogEntityList =
            this.dao.getDutyCategoryChangelogs(categoryEntity);

        assertNotNull(changelogEntityList, "The returning List has to be not null");

        if (!changelogEntityList.isEmpty()) {
            for (IDutyCategoryChangelogEntity entity : changelogEntityList) {
                assertEquals(
                    entity.getDutyCategory().getDutyCategoryId(),
                    categoryEntity.getDutyCategoryId()
                );
            }
        }
    }
}