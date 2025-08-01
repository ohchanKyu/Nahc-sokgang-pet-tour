package kr.ac.dankook.SokGangPetTour.entity.vetPlace;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vet_place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VetPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String address;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    private String holidayInfo;
    private String maxSizeInfo;
    private boolean isParking;

    @OneToMany(mappedBy = "vetPlace", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VetPlaceOperatingHour> operatingHours = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private VetPlaceCategory category;

    @Builder
    public VetPlace(boolean isParking, String maxSizeInfo, String holidayInfo,
                    double longitude, double latitude, String phoneNumber,
                    String address, String placeName, VetPlaceCategory category) {
        this.isParking = isParking;
        this.maxSizeInfo = maxSizeInfo;
        this.holidayInfo = holidayInfo;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.placeName = placeName;
        this.category = category;
    }

    public void addOperatingHour(VetPlaceOperatingHour hour) {
        this.operatingHours.add(hour);
        hour.setVetPlace(this);
    }
}
