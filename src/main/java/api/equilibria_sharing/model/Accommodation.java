package api.equilibria_sharing.model;

import jakarta.persistence.*;

@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private int maxGuests;

    private double pricePerNight;

    public Accommodation() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public Accommodation (String name, String type, String description, Address address, Integer maxGuests, Double pricePerNight) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.address = address;
        this.maxGuests = maxGuests;
        this.pricePerNight = pricePerNight;
    }
}
