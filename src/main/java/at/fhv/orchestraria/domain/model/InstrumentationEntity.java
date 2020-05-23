package at.fhv.orchestraria.domain.model;

import at.fhv.orchestraria.domain.Imodel.*;

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
@Table(name = "instrumentation", schema = "ni128610_1sql8")
public class InstrumentationEntity implements IInstrumentation, Serializable {
    private int instrumentationId;
    private String name;
    private MusicalPieceEntity musicalPiece;
    private Collection<InstrumentationPositionEntity> instrumentationPositions;
    private Collection<SectionInstrumentationEntity> sectionInstrumentations;
    private Collection<SeriesOfPerformancesInstrumentationEntity> seriesInstrumentations;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentationId")
    @Override
    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    @Basic
    @Column(name = "name")
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentationEntity that = (InstrumentationEntity) o;
        return instrumentationId == that.instrumentationId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentationId, name);
    }

    @ManyToOne
    @JoinColumn(name = "musicalPieceId", referencedColumnName = "musicalPieceId", nullable = false)
    @Override
    public MusicalPieceEntity getMusicalPiece() {
        return musicalPiece;
    }

    public void setMusicalPiece(MusicalPieceEntity musicalPiece) {
        this.musicalPiece = musicalPiece;
    }

    @OneToMany(mappedBy = "instrumentation")
    public Collection<InstrumentationPositionEntity> getInstrumentationPositions() {
        return instrumentationPositions;
    }

    public void setInstrumentationPositions(Collection<InstrumentationPositionEntity> instrumentationPositionsByInstrumentationId) {
        this.instrumentationPositions = instrumentationPositionsByInstrumentationId;
    }

    @OneToMany(mappedBy = "instrumentation")
    public Collection<SectionInstrumentationEntity> getSectionInstrumentations() {
        return sectionInstrumentations;
    }

    public void setSectionInstrumentations(Collection<SectionInstrumentationEntity> sectionInstrumentationsByInstrumentationId) {
        this.sectionInstrumentations = sectionInstrumentationsByInstrumentationId;
    }

    @OneToMany(mappedBy = "instrumentation")
    public Collection<SeriesOfPerformancesInstrumentationEntity> getSeriesInstrumentations() {
        return seriesInstrumentations;
    }

    public void setSeriesInstrumentations(Collection<SeriesOfPerformancesInstrumentationEntity> seriesInstrumentations) {
        this.seriesInstrumentations = seriesInstrumentations;
    }

    /*
        Interface methods
     */

    @Transient
    @Override
    public Collection<IInstrumentationPosition> getIInstrumentationPositions(){
        return Collections.unmodifiableCollection(getInstrumentationPositions());
    }

    @Transient
    @Override
    public Collection<ISectionInstrumentation> getISectionInstrumentations(){
        return  Collections.unmodifiableCollection(getSectionInstrumentations());
    }

    @Transient
    @Override
    public Collection<ISeriesOfPerformancesInstrumentation> getISeriesOfPerformancesInstrumentation() {
        return Collections.unmodifiableCollection(getSeriesInstrumentations());
    }

}
