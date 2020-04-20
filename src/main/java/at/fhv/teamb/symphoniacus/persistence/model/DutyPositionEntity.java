package at.fhv.teamb.symphoniacus.persistence.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dutyPosition")
public class DutyPositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dutyPositionId")
    private Integer dutyPositionId;

    @Column(name = "description")
    private String description;

    @Column(name = "instrumentationPositionId")
    private Integer instrumentationPositionId;

    @Column(name = "dutyId", updatable = false, insertable = false)
    private Integer dutyId;

    @Column(name = "sectionId")
    private Integer sectionId;

    //Many-To-One Part for DUTY Table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dutyId")
    private DutyEntity duty;

    @ManyToMany(mappedBy = "dutyPositions")
    private List<MusicianEntity> musicians = new LinkedList<>();

    public DutyEntity getDuty() {
        return this.duty;
    }

    public void setDuty(DutyEntity dutyEntity) {
        this.duty = dutyEntity;
    }

    public Integer getDutyPositionId() {
        return this.dutyPositionId;
    }

    public void setDutyPositionId(Integer dutyPositionId) {
        this.dutyPositionId = dutyPositionId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInstrumentationPositionId() {
        return this.instrumentationPositionId;
    }

    public void setInstrumentationPositionId(Integer instrumentationPositionId) {
        this.instrumentationPositionId = instrumentationPositionId;
    }

    public Integer getDutyId() {
        return this.dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public Integer getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

}
