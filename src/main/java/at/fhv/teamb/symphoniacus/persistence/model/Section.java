package at.fhv.teamb.symphoniacus.persistence.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "section")
public class Section {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "sectionId")
    private Integer sectionId;

    @Column(name = "sectionShortcut")
    private String sectionShortcut;

    @Column(name = "description")
    private String description;


    //One-To-Many Part for SECTIONMONTHLYSCHEDULE Table
    @OneToMany(mappedBy = "section", orphanRemoval = true)
    @JoinColumn(name = "sectionId")

    private Set<SectionMonthlySchedule> sectionMonthlyScheduleSet = new HashSet<SectionMonthlySchedule>();
    public Set<SectionMonthlySchedule> getSectionMonthlyScheduleSet() {
        return this.sectionMonthlyScheduleSet;
    }
    public void setSectionMonthlyScheduleSet(Set<SectionMonthlySchedule> sectionMonthlyScheduleSet) {
        this.sectionMonthlyScheduleSet = sectionMonthlyScheduleSet;
    }

    public void addSectionMonthlySchedule(SectionMonthlySchedule sectionMonthlySchedule) {
        this.sectionMonthlyScheduleSet.add(sectionMonthlySchedule);
        sectionMonthlySchedule.setSection(this);
    }


    //Getter and Setter
    public Integer getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionShortcut() {
        return this.sectionShortcut;
    }

    public void setSectionShortcut(String sectionShortcut) {
        this.sectionShortcut = sectionShortcut;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
