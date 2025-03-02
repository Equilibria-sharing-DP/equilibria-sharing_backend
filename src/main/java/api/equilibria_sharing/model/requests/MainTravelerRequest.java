package api.equilibria_sharing.model.requests;

import api.equilibria_sharing.model.Gender;
import api.equilibria_sharing.model.TravelDocumentType;

import java.time.LocalDate;

/**
 * BookingRequest class - Just a placeholder class for Main traveler requests
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
public class MainTravelerRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDate;
    private TravelDocumentType travelDocumentType;
    private String documentNr;
    private String country;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String issuingAuthority;
    private String city;
    private int postalCode;
    private String street;
    private int houseNumber;
    private String addressAdditional;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDocumentNr() {
        return documentNr;
    }

    public void setDocumentNr(String documentNr) {
        this.documentNr = documentNr;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddressAdditional() {
        return addressAdditional;
    }

    public void setAddressAdditional(String addressAdditional) {
        this.addressAdditional = addressAdditional;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TravelDocumentType getTravelDocumentType() {
        return travelDocumentType;
    }

    public void setTravelDocumentType(TravelDocumentType travelDocumentType) {
        this.travelDocumentType = travelDocumentType;
    }
}
