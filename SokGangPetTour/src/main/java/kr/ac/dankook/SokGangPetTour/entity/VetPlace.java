package kr.ac.dankook.SokGangPetTour.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String operatingInfo;
    private String maxSizeInfo;
    private boolean isParking;

    @Enumerated(EnumType.STRING)
    private VetPlaceCategory category;
}
