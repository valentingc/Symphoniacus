package at.fhv.teamb.symphoniacus.persistence.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vacation")
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacationId")
    private Integer vacationId;

    @Column(name = "musicianId")
    private Integer musicianId;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "isConfirmed")
    private Boolean isConfirmed;

    public Integer getVacationId() {
        return this.vacationId;
    }

    public void setVacationId(Integer vacationId) {
        this.vacationId = vacationId;
    }

    public Integer getMusicianId() {
        return this.musicianId;
    }

    public void setMusicianId(Integer musicianId) {
        this.musicianId = musicianId;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsConfirmed() {
        return this.isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}