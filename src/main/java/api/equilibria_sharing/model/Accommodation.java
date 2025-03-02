package api.equilibria_sharing.model;

import jakarta.persistence.*;

/**
 * Accommodation entity - for the company's accommodations
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */
@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String description;

    @ManyToOne
    private Address address;

    private int maxGuests;
    private double pricePerNight;

    public Accommodation(String name, String type, String description, Address address, int maxGuests, double pricePerNight) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.address = address;
        this.maxGuests = maxGuests;
        this.pricePerNight = pricePerNight;
    }

    public Accommodation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}

