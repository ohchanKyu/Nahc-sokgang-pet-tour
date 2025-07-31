package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TourContentDetailResponse {

   private TourContentResponse tourContentResponse;
   private List<TourDetailImageResponse> imagesInfo;
   private List<TourDetailPetResponse> petsInfo;

   private List<TourDetailRepeatCommonResponse> repeatCommons;
   private List<TourDetailRepeatRoomResponse> repeatRooms;

   private List<TourDetailIntroCultureResponse> introCultures;
   private List<TourDetailIntroFoodResponse> introFoods;
   private List<TourDetailIntroLeportsResponse> introLeports;
   private List<TourDetailIntroLodgingResponse> introLodgings;
   private List<TourDetailIntroShoppingResponse> introShopping;
   private List<TourDetailIntroTouristResponse> introTourists;
}
