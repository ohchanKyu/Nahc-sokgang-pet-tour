package kr.ac.dankook.SokGangPetTour.service;

import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.PlaceEntityType;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourStatic;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetStatic;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourStaticRepository;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetStaticRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter;
import kr.ac.dankook.SokGangPetTour.util.converter.VetPlaceEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceStaticService {

    private final VetPlaceRepository vetPlaceRepository;
    private final VetStaticRepository vetStaticRepository;
    private final TourContentRepository tourContentRepository;
    private final TourStaticRepository tourStaticRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void countUp(String id, PlaceEntityType entityType){

        LocalDate now = LocalDate.now();
        if (entityType == PlaceEntityType.VET){
            vetStaticRepository.upsertCount(EncryptionUtil.decrypt(id), now);
        }else{
            tourStaticRepository.upsertCount(id,now);
        }
    }

    public List<VetPlaceResponse> getVetPlaceByCount(){
        LocalDate now = LocalDate.now();

        List<VetPlace> statics = vetStaticRepository.findByDayOrderByCount(now);
        List<VetPlace> places = vetPlaceRepository.findAll();

        Set<Long> staticsId = statics.stream()
                .map(VetPlace::getId).collect(Collectors.toSet());
        List<VetPlace> noStatics =  places.stream()
                        .filter(place -> !staticsId.contains(place.getId()))
                                .toList();
        List<VetPlace> merged = new ArrayList<>(statics);
        merged.addAll(noStatics);

        return merged.stream().map(item ->
                    VetPlaceEntityConverter.convertToVetPlaceResponse(item,false)
                ).toList();
    }

    public List<TourContentResponse> getTourPlaceByCount(){
        LocalDate now = LocalDate.now();

        List<TourContent> statics = tourStaticRepository.findByDayOrderByCount(now);
        List<TourContent> places = tourContentRepository.findAll();

        Set<String> staticsId = statics.stream()
                .map(TourContent::getContentId).collect(Collectors.toSet());
        List<TourContent> noStatics =  places.stream()
                .filter(place -> !staticsId.contains(place.getContentId()))
                .toList();
        List<TourContent> merged = new ArrayList<>(statics);
        merged.addAll(noStatics);
        return merged.stream().map(TourEntityConverter::convertToTourContentResponse).toList();
    }
}
