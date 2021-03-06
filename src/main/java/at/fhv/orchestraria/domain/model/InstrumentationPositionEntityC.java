package at.fhv.orchestraria.domain.model;

import at.fhv.orchestraria.domain.Imodel.IDutyPosition;
import at.fhv.orchestraria.domain.Imodel.IInstrumentationPosition;
import at.fhv.orchestraria.domain.integrationInterfaces.IntegratableInstrumentationPosition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author generated by Intellij -  edited by Team C
 */

/*
 * Generated by IntelliJ
 */

@Entity
@Table(name = "instrumentationPosition", schema = "ni128610_1sql8")
public class InstrumentationPositionEntityC implements IInstrumentationPosition, Serializable, IntegratableInstrumentationPosition {
    private int instrumentationPositionId;
    private String positionDescription;
    private Collection<DutyPositionEntityC> dutyPositions;
    private SectionInstrumentationEntityC sectionInstrumentation;
    private InstrumentationEntityC instrumentation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentationPositionId")
    @Override
    public int getInstrumentationPositionId() {
        return instrumentationPositionId;
    }

    public void setInstrumentationPositionId(int instrumentationPositionId) {
        this.instrumentationPositionId = instrumentationPositionId;
    }

    @Basic
    @Column(name = "positiondescription")
    @Override
    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String predefinedSectionInstrumentation) {
        this.positionDescription = predefinedSectionInstrumentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentationPositionEntityC that = (InstrumentationPositionEntityC) o;
        return instrumentationPositionId == that.instrumentationPositionId &&
                Objects.equals(positionDescription, that.positionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentationPositionId, positionDescription);
    }

    @OneToMany(mappedBy = "instrumentationPosition")
    public Collection<DutyPositionEntityC> getDutyPositions() {
        return dutyPositions;
    }

    public void setDutyPositions(Collection<DutyPositionEntityC> dutyPositionsByInstrumentationPositionId) {
        this.dutyPositions = dutyPositionsByInstrumentationPositionId;
    }

    @ManyToOne
    @JoinColumn(name = "sectionInstrumentationId", referencedColumnName = "sectionInstrumentationId", nullable = false)
    @Override
    public SectionInstrumentationEntityC getSectionInstrumentation() {
        return sectionInstrumentation;
    }

    public void setSectionInstrumentation(SectionInstrumentationEntityC sectionInstrumentation) {
        this.sectionInstrumentation = sectionInstrumentation;
    }

    @ManyToOne
    @JoinColumn(name = "instrumentationId", referencedColumnName = "instrumentationId", nullable = false)
    @Override
    public InstrumentationEntityC getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(InstrumentationEntityC instrumentation) {
        this.instrumentation = instrumentation;
    }

    /*
     *Interface methods
     */

    @Transient
    @Override
    public Collection<IDutyPosition> getIDutyPositions(){
        return Collections.unmodifiableCollection(getDutyPositions());
    }
}
