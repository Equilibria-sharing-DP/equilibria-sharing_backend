package api.equilibria_sharing.model;


import jakarta.persistence.*;

/**
 * Address class
 *
 * @author Manuel Fellner
 * @version 23.11.2024
 */
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city;

    private String community;

    private int postalCode;

    private String street;

    private int houseNumber;

    private String addressAdditional;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
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
}
