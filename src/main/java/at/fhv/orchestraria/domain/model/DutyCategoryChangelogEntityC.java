package at.fhv.orchestraria.domain.model;

import at.fhv.orchestraria.domain.Imodel.IDutyCategoryChangelog;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * @author generated by Intellij -  edited by Team C
 */

/*
 * Generated by IntelliJ
 */

@Entity
@Table(name = "dutyCategoryChangelog", schema = "ni128610_1sql8")
public class DutyCategoryChangelogEntityC implements IDutyCategoryChangelog, Serializable {
    private int dutyCategoryChangelogId;
    private Date startDate;
    private int points;
    private DutyCategoryEntityC dutyCategory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dutyCategoryChangelogId")
    @Override
    public int getDutyCategoryChangelogId() {
        return dutyCategoryChangelogId;
    }

    public void setDutyCategoryChangelogId(int dutyCategoryChangelogId) {
        this.dutyCategoryChangelogId = dutyCategoryChangelogId;
    }

    @Basic
    @Column(name = "startDate")
    @Override
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "points")
    @Override
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DutyCategoryChangelogEntityC that = (DutyCategoryChangelogEntityC) o;
        return dutyCategoryChangelogId == that.dutyCategoryChangelogId &&
                points == that.points &&
                Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dutyCategoryChangelogId, startDate, points);
    }

    @ManyToOne
    @JoinColumn(name = "dutyCategoryId", referencedColumnName = "dutyCategoryId", nullable = false)
    @Override
    public DutyCategoryEntityC getDutyCategory() {
        return dutyCategory;
    }

    public void setDutyCategory(DutyCategoryEntityC dutyCategory) {
        this.dutyCategory = dutyCategory;
    }
}
