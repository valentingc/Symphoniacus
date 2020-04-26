package at.fhv.teamb.symphoniacus.persistence.model;

import at.fhv.teamb.symphoniacus.application.type.DomainUserType;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "shortcut")
    private String shortcut;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "city")
    private String city;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name = "street")
    private String street;

    @Column(name = "streetNumber")
    private String streetNumber;

    @Column(name = "passwordSalt")
    private String passwordSalt;

    @OneToOne(mappedBy = "user")
    private MusicianEntity musician;

    @OneToMany(mappedBy = "user")
    private List<AdministrativeAssistantEntity> administrativeAssistants = new LinkedList<>();

    @Transient
    protected DomainUserType type; // TODO

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShortcut() {
        return this.shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public MusicianEntity getMusician() {
        return this.musician;
    }

    public void setMusician(MusicianEntity musician) {
        this.musician = musician;
        musician.setUser(this);
    }

    public List<AdministrativeAssistantEntity> getAdministrativeAssistants() {
        return this.administrativeAssistants;
    }

    public void addAdministrativeAssistant(AdministrativeAssistantEntity administrativeAssistant) {
        this.administrativeAssistants.add(administrativeAssistant);
        administrativeAssistant.setUser(this);
    }

    public void removeAdministrativeAssistant(
        AdministrativeAssistantEntity administrativeAssistant
    ) {
        this.administrativeAssistants.remove(administrativeAssistant);
        administrativeAssistant.setUser(null);
    }

    public DomainUserType getType() { // TODO
        return this.type;
    }

    public void setType(DomainUserType type) { // TODO
        this.type = type;
    }
}