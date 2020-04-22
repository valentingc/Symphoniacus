package at.fhv.teamb.symphoniacus.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dutyCategory")
public class DutyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dutyCategoryId")
    private Integer dutyCategoryId;

    @Column(name = "type")
    private String type;

    @Column(name = "isRehearsal")
    private Boolean isRehearsal;

    @Column(name = "points")
    private Integer points;

    public Integer getDutyCategoryId() {
        return this.dutyCategoryId;
    }

    public void setDutyCategoryId(Integer dutyCategoryId) {
        this.dutyCategoryId = dutyCategoryId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsRehearsal() {
        return this.isRehearsal;
    }

    public void setIsRehearsal(Boolean isRehearsal) {
        this.isRehearsal = isRehearsal;
    }

    public Integer getPoints() {
        return this.points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}