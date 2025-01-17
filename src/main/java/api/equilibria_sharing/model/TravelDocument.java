package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class TravelDocument {
    @Id
    private String passportNr;

    private String country;

    private LocalDate issueDate;

    private String issuingAuthority;

    public String getPassportNr() {
        return passportNr;
    }

    public void setPassportNr(String passportNr) {
        this.passportNr = passportNr;
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

    public void setIssuingAuthority(String issuing_authority) {
        this.issuingAuthority = issuing_authority;
    }
}
