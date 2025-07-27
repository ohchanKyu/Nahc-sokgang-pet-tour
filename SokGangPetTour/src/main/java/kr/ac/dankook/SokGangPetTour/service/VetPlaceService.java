package kr.ac.dankook.SokGangPetTour.service;

import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.VetPlaceOperatingHour;
import kr.ac.dankook.SokGangPetTour.repository.VetPlaceOperatingHourRepository;
import kr.ac.dankook.SokGangPetTour.repository.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.VetPlaceEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetPlaceService {

    private final VetPlaceRepository vetPlaceRepository;
    private final VetPlaceOperatingHourRepository vetPlaceOperatingHourRepository;

    @Transactional(readOnly = true)
    public List<VetPlaceResponse> getAllVetPlace(){
        List<VetPlace> lists = vetPlaceRepository.findAll();
        return lists.stream().map(VetPlaceEntityConverter::convertToVetPlaceResponse).toList();
    }
}
