package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * TravelDocument class - stores all traveldocument characteristics
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Entity
public class TravelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TravelDocumentType type;

    private String documentNr;

    private String country;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String issuingAuthority;

    public TravelDocumentType getType() {
        return type;
    }

    public void setType(TravelDocumentType type) {
        this.type = type;
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

    public void setIssuingAuthority(String issuing_authority) {
        this.issuingAuthority = issuing_authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "TravelDocument{" +
                "id=" + id +
                ", type=" + type +
                ", documentNr='" + documentNr + '\'' +
                ", country='" + country + '\'' +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                '}';
    }
}
