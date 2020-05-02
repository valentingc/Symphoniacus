package at.fhv.teamb.symphoniacus.persistence.dao;

import at.fhv.teamb.symphoniacus.persistence.BaseDao;
import at.fhv.teamb.symphoniacus.persistence.model.SeriesOfPerformancesEntity;
import java.util.Optional;

public class SeriesOfPerformancesDao extends BaseDao<SeriesOfPerformancesEntity> {

    @Override
    public Optional<SeriesOfPerformancesEntity> find(Integer key) {
        return this.find(SeriesOfPerformancesEntity.class, key);
    }

    @Override
    public Optional<SeriesOfPerformancesEntity> persist(SeriesOfPerformancesEntity elem) {
        return this.persist(SeriesOfPerformancesEntity.class,elem);
    }

    @Override
    public Optional<SeriesOfPerformancesEntity> update(SeriesOfPerformancesEntity elem) {
        return Optional.empty();
    }

    @Override
    public Boolean remove(SeriesOfPerformancesEntity elem) {
        return null;
    }
}
