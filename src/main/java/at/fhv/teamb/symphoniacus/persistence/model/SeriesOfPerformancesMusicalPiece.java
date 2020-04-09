package at.fhv.teamb.symphoniacus.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seriesOfPerformances_musicalPiece")
public class SeriesOfPerformancesMusicalPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seriesOfPerformances_musicalPieceId")
    private Integer seriesOfPerformancesMusicalPieceId;

    @Column(name = "musicalPieceId", insertable = false, updatable = false)
    private Integer musicalPieceId;

    @Column(name = "seriesOfPerformancesId")
    private Integer seriesOfPerformancesId;

    //Many-To-One Part for MUSICALPIECE Table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicalPieceId")
    private MusicalPiece musicalPiece;

    public MusicalPiece getMusicalPiece() {
        return this.musicalPiece;
    }

    public void setMusicalPiece(MusicalPiece musicalPiece) {
        this.musicalPiece = musicalPiece;
    }

    public Integer getSeriesOfPerformancesMusicalPieceId() {
        return this.seriesOfPerformancesMusicalPieceId;
    }

    public void setSeriesOfPerformancesMusicalPieceId(Integer seriesOfPerformancesMusicalPieceId) {
        this.seriesOfPerformancesMusicalPieceId = seriesOfPerformancesMusicalPieceId;
    }

    public Integer getMusicalPieceId() {
        return this.musicalPieceId;
    }

    public void setMusicalPieceId(Integer musicalPieceId) {
        this.musicalPieceId = musicalPieceId;
    }

    public Integer getSeriesOfPerformancesId() {
        return this.seriesOfPerformancesId;
    }

    public void setSeriesOfPerformancesId(Integer seriesOfPerformancesId) {
        this.seriesOfPerformancesId = seriesOfPerformancesId;
    }
}
