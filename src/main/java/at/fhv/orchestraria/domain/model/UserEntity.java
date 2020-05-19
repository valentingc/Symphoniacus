package at.fhv.orchestraria.domain.model;

import at.fhv.orchestraria.UserInterface.Usermanagement.ManageableUser;
import at.fhv.orchestraria.domain.Imodel.IUser;
import at.fhv.orchestraria.domain.integrationInterfaces.IntegratableUser;
import at.fhv.orchestraria.domain.integrationInterfaces.PasswordableUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author generated by Intellij -  edited by Team C
 */

/*
 * Generated by IntelliJ
 */
@Entity
@Table(name = "user", schema = "ni128610_1sql8")
public class UserEntity implements IUser, ManageableUser, PasswordableUser, Serializable, IntegratableUser {
    private int userId;
    private String firstName;
    private String lastName;
    private String shortcut;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String zipCode;
    private String country;
    private String street;
    private String streetNumber;
    private String passwordSalt;
    private AdministrativeAssistantEntity administrativeAssistant;
    private MusicianEntity musician;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    @Override
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "firstName")
    @Override
    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName")
    @Override
    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "shortcut")
    @Override
    public String getShortcut() {
        return shortcut;
    }


    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Basic
    @Column(name = "email")
    @Override
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone")
    @Override
    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "password")
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "city")
    @Override
    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "zipCode")
    @Override
    public String getZipCode() {
        return zipCode;
    }


    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "country")
    @Override
    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "street")
    @Override
    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "streetNumber")
    @Override
    public String getStreetNumber() {
        return streetNumber;
    }


    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Basic
    @Column(name = "passwordSalt")
    @Override
    public String getPasswordSalt() {
        return passwordSalt;
    }

    @Override
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId == that.userId &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(shortcut, that.shortcut) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(password, that.password) &&
                Objects.equals(city, that.city) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(country, that.country) &&
                Objects.equals(street, that.street) &&
                Objects.equals(streetNumber, that.streetNumber) &&
                Objects.equals(passwordSalt, that.passwordSalt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, shortcut, email, phone, password, city, zipCode, country, street, streetNumber, passwordSalt);
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE)
    @Override
    public AdministrativeAssistantEntity getAdministrativeAssistant() {
        return administrativeAssistant;
    }


    public void setAdministrativeAssistant(AdministrativeAssistantEntity administrativeAssistant) {
        this.administrativeAssistant = administrativeAssistant;
    }

    @OneToOne(mappedBy = "user" ,cascade = CascadeType.MERGE)
    @Override
    public MusicianEntity getMusician() {
        return musician;
    }


    public void setMusician(MusicianEntity musician) {
        this.musician = musician;
    }


    /*
     * Interface methods
     */

}
