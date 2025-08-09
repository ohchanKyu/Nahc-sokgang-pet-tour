package kr.ac.dankook.SokGangPetTour.util.converter;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourContentRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailImageRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailPetRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.*;
import kr.ac.dankook.SokGangPetTour.entity.tour.*;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.*;

public class TourEntityConverter {

    public static TourContentResponse convertToTourContentResponse(TourContent tourContent){
        return TourContentResponse.builder()
                .contentId(tourContent.getContentId())
                .address(tourContent.getAddress())
                .detailAddress(tourContent.getDetailAddress())
                .postCode(tourContent.getPostCode())
                .areaCode(tourContent.getAreaCode())
                .sigunguCode(tourContent.getSigunguCode())
                .cat1(tourContent.getCat1())
                .cat2(tourContent.getCat2())
                .cat3(tourContent.getCat3())
                .contentTypeId(tourContent.getContentTypeId())
                .originalImageUrl(tourContent.getOriginalImageUrl())
                .thumbnailImageUrl(tourContent.getThumbnailImageUrl())
                .longitude(tourContent.getLongitude())
                .latitude(tourContent.getLatitude())
                .telephone(tourContent.getTelephone())
                .title(tourContent.getTitle())
                .overview(tourContent.getOverview()).build();
    }

    public static TourContent updateTourContentFromDto(TourContent entity, TourContentRequest dto) {
        entity.setAddress(dto.getAddress());
        entity.setDetailAddress(dto.getDetailAddress());
        entity.setPostCode(dto.getPostCode());
        entity.setAreaCode(dto.getAreaCode());
        entity.setSigunguCode(dto.getSigunguCode());
        entity.setCat1(dto.getCat1());
        entity.setCat2(dto.getCat2());
        entity.setCat3(dto.getCat3());
        entity.setContentTypeId(dto.getContentTypeId());
        entity.setOriginalImageUrl(dto.getOriginalImageUrl());
        entity.setThumbnailImageUrl(dto.getThumbnailImageUrl());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setTelephone(dto.getTelephone());
        entity.setTitle(dto.getTitle());
        return entity;
    }

    // TourDetailImage -> TourDetailImageResponse
    public static TourDetailImageResponse convertToTourDetailImageResponse(TourDetailImage image) {
        return TourDetailImageResponse.builder()
                .originImgUrl(image.getOriginImgUrl())
                .smallImgUrl(image.getSmallImgUrl())
                .imgName(image.getImgName())
                .build();
    }

    // TourDetailPet -> TourDetailPetResponse
    public static TourDetailPetResponse convertToTourDetailPetResponse(TourDetailPet pet) {
        return TourDetailPetResponse.builder()
                .relaAcdntRiskMtr(pet.getRelaAcdntRiskMtr())
                .acmpyTypeCd(pet.getAcmpyTypeCd())
                .relaPosesFclty(pet.getRelaPosesFclty())
                .relaFrnshPrdlst(pet.getRelaFrnshPrdlst())
                .acmpyPsblCpam(pet.getAcmpyPsblCpam())
                .relaRntlPrdlst(pet.getRelaRntlPrdlst())
                .acmpyNeedMtr(pet.getAcmpyNeedMtr())
                .build();
    }

    // TourDetailRepeatCommon -> TourDetailRepeatCommonResponse
    public static TourDetailRepeatCommonResponse convertToTourDetailRepeatCommonResponse(TourDetailRepeatCommon common) {
        return TourDetailRepeatCommonResponse.builder()
                .infoName(common.getInfoName())
                .infoText(common.getInfoText())
                .build();
    }

    // TourDetailRepeatRoom -> TourDetailRepeatRoomResponse
    public static TourDetailRepeatRoomResponse convertToTourDetailRepeatRoomResponse(TourDetailRepeatRoom room) {
        return TourDetailRepeatRoomResponse.builder()
                .roomInfoNo(room.getRoomInfoNo())
                .roomTitle(room.getRoomTitle())
                .roomSize1(room.getRoomSize1())
                .roomSize2(room.getRoomSize2())
                .roomCount(room.getRoomCount())
                .roomBaseCount(room.getRoomBaseCount())
                .roomMaxCount(room.getRoomMaxCount())
                .roomOffSeasonMinFee1(room.getRoomOffSeasonMinFee1())
                .roomOffSeasonMinFee2(room.getRoomOffSeasonMinFee2())
                .roomPeakSeasonMinFee1(room.getRoomPeakSeasonMinFee1())
                .roomPeakSeasonMinFee2(room.getRoomPeakSeasonMinFee2())
                .roomIntro(room.getRoomIntro())
                .roomBathFacility(room.getRoomBathFacility())
                .roomBath(room.getRoomBath())
                .roomHomeTheater(room.getRoomHomeTheater())
                .roomAirCondition(room.getRoomAirCondition())
                .roomTv(room.getRoomTv())
                .roomPc(room.getRoomPc())
                .roomCable(room.getRoomCable())
                .roomInternet(room.getRoomInternet())
                .roomRefrigerator(room.getRoomRefrigerator())
                .roomToiletries(room.getRoomToiletries())
                .roomSofa(room.getRoomSofa())
                .roomCook(room.getRoomCook())
                .roomTable(room.getRoomTable())
                .roomHairDryer(room.getRoomHairDryer())
                .roomImg1(room.getRoomImg1())
                .roomImg1Alt(room.getRoomImg1Alt())
                .roomImg2(room.getRoomImg2())
                .roomImg2Alt(room.getRoomImg2Alt())
                .roomImg3(room.getRoomImg3())
                .roomImg3Alt(room.getRoomImg3Alt())
                .roomImg4(room.getRoomImg4())
                .roomImg4Alt(room.getRoomImg4Alt())
                .roomImg5(room.getRoomImg5())
                .roomImg5Alt(room.getRoomImg5Alt())
                .build();
    }

    // TourDetailIntroCulture -> TourDetailIntroCultureResponse
    public static TourDetailIntroCultureResponse convertToTourDetailIntroCultureResponse(TourDetailIntroCulture culture) {
        return TourDetailIntroCultureResponse.builder()
                .accomCountCulture(culture.getAccomCountCulture())
                .chkCreditCardCulture(culture.getChkCreditCardCulture())
                .infoCenterCulture(culture.getInfoCenterCulture())
                .parkingCulture(culture.getParkingCulture())
                .parkingFee(culture.getParkingFee())
                .restDateCulture(culture.getRestDateCulture())
                .useFee(culture.getUseFee())
                .useTimeCulture(culture.getUseTimeCulture())
                .scale(culture.getScale())
                .build();
    }

    // TourDetailIntroFood -> TourDetailIntroFoodResponse
    public static TourDetailIntroFoodResponse convertToTourDetailIntroFoodResponse(TourDetailIntroFood food) {
        return TourDetailIntroFoodResponse.builder()
                .chkCreditCardFood(food.getChkCreditCardFood())
                .firstMenu(food.getFirstMenu())
                .infoCenterFood(food.getInfoCenterFood())
                .kidsFacility(food.getKidsFacility())
                .openTimeFood(food.getOpenTimeFood())
                .packing(food.getPacking())
                .parkingFood(food.getParkingFood())
                .reservationFood(food.getReservationFood())
                .restDateFood(food.getRestDateFood())
                .smoking(food.getSmoking())
                .treatMenu(food.getTreatMenu())
                .build();
    }

    // TourDetailIntroLeports -> TourDetailIntroLeportsResponse
    public static TourDetailIntroLeportsResponse convertToTourDetailIntroLeportsResponse(TourDetailIntroLeports leports) {
        return TourDetailIntroLeportsResponse.builder()
                .accomCountLeports(leports.getAccomCountLeports())
                .chkCreditCardLeports(leports.getChkCreditCardLeports())
                .expAgeRangeLeports(leports.getExpAgeRangeLeports())
                .infoCenterLeports(leports.getInfoCenterLeports())
                .openPeriod(leports.getOpenPeriod())
                .parkingFeeLeports(leports.getParkingFeeLeports())
                .parkingLeports(leports.getParkingLeports())
                .reservation(leports.getReservation())
                .restDateLeports(leports.getRestDateLeports())
                .useFeeLeports(leports.getUseFeeLeports())
                .useTimeLeports(leports.getUseTimeLeports())
                .build();
    }

    // TourDetailIntroLodging -> TourDetailIntroLodgingResponse
    public static TourDetailIntroLodgingResponse convertToTourDetailIntroLodgingResponse(TourDetailIntroLodging lodging) {
        return TourDetailIntroLodgingResponse.builder()
                .accomCountLodging(lodging.getAccomCountLodging())
                .benikia(lodging.getBenikia())
                .checkInTime(lodging.getCheckInTime())
                .checkOutTime(lodging.getCheckOutTime())
                .chkCooking(lodging.getChkCooking())
                .foodPlace(lodging.getFoodPlace())
                .goodStay(lodging.getGoodStay())
                .hanok(lodging.getHanok())
                .infoCenterLodging(lodging.getInfoCenterLodging())
                .parkingLodging(lodging.getParkingLodging())
                .pickup(lodging.getPickup())
                .roomCount(lodging.getRoomCount())
                .reservationLodging(lodging.getReservationLodging())
                .roomType(lodging.getRoomType())
                .scaleLodging(lodging.getScaleLodging())
                .subFacility(lodging.getSubFacility())
                .barbecue(lodging.getBarbecue())
                .beauty(lodging.getBeauty())
                .beverage(lodging.getBeverage())
                .bicycle(lodging.getBicycle())
                .campfire(lodging.getCampfire())
                .fitness(lodging.getFitness())
                .karaoke(lodging.getKaraoke())
                .publicBath(lodging.getPublicBath())
                .publicPc(lodging.getPublicPc())
                .sauna(lodging.getSauna())
                .seminar(lodging.getSeminar())
                .sports(lodging.getSports())
                .refundRegulation(lodging.getRefundRegulation())
                .build();
    }

    // TourDetailIntroShopping -> TourDetailIntroShoppingResponse
    public static TourDetailIntroShoppingResponse convertToTourDetailIntroShoppingResponse(TourDetailIntroShopping shopping) {
        return TourDetailIntroShoppingResponse.builder()
                .chkCreditCard(shopping.getChkCreditCard())
                .infoCenterShopping(shopping.getInfoCenterShopping())
                .openTime(shopping.getOpenTime())
                .parkingShopping(shopping.getParkingShopping())
                .restDateShopping(shopping.getRestDateShopping())
                .restRoom(shopping.getRestRoom())
                .saleItem(shopping.getSaleItem())
                .scaleShopping(shopping.getScaleShopping())
                .build();
    }

    // TourDetailIntroTourist -> TourDetailIntroTouristResponse
    public static TourDetailIntroTouristResponse convertToTourDetailIntroTouristResponse(TourDetailIntroTourist tourist) {
        return TourDetailIntroTouristResponse.builder()
                .accomCount(tourist.getAccomCount())
                .chkCreditCard(tourist.getChkCreditCard())
                .expAgeRange(tourist.getExpAgeRange())
                .expGuide(tourist.getExpGuide())
                .heritage1(tourist.getHeritage1())
                .heritage2(tourist.getHeritage2())
                .heritage3(tourist.getHeritage3())
                .infoCenter(tourist.getInfoCenter())
                .openDate(tourist.getOpenDate())
                .parking(tourist.getParking())
                .restDate(tourist.getRestDate())
                .useSeason(tourist.getUseSeason())
                .useTime(tourist.getUseTime())
                .build();
    }
    public static TourDetailRepeatCommon convertToTourDetailRepeatCommon(TourDetailRepeatCommonRequest item, TourContent tourContent){
        return TourDetailRepeatCommon.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .infoText(item.getInfoText())
                .infoName(item.getInfoName()).build();
    }

    public static TourDetailImage convertToTourDetailImage(TourDetailImageRequest item, TourContent tourContent){
        return TourDetailImage.builder()
                .tourContent(tourContent)
                .imgName(item.getImgName())
                .smallImgUrl(item.getSmallImgUrl())
                .originImgUrl(item.getOriginImgUrl()).build();
    }

    public static TourDetailPet convertToTourDetailPet(TourDetailPetRequest item, TourContent tourContent){
        return TourDetailPet.builder()
                .tourContent(tourContent)
                .relaAcdntRiskMtr(item.getRelaAcdntRiskMtr())
                .acmpyTypeCd(item.getAcmpyTypeCd())
                .relaPosesFclty(item.getRelaPosesFclty())
                .relaFrnshPrdlst(item.getRelaFrnshPrdlst())
                .etcAcmpyInfo(item.getEtcAcmpyInfo())
                .acmpyPsblCpam(item.getAcmpyPsblCpam())
                .relaRntlPrdlst(item.getRelaRntlPrdlst())
                .acmpyNeedMtr(item.getAcmpyNeedMtr())
                .build();
    }

    public static TourContent convertToTourContent(TourContentRequest item){
        return TourContent.builder()
                .contentId(item.getContentId())
                .address(item.getAddress())
                .detailAddress(item.getDetailAddress())
                .postCode(item.getPostCode())
                .areaCode(item.getAreaCode())
                .sigunguCode(item.getSigunguCode())
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .contentTypeId(item.getContentTypeId())
                .originalImageUrl(item.getOriginalImageUrl())
                .thumbnailImageUrl(item.getThumbnailImageUrl())
                .longitude(item.getLongitude())
                .latitude(item.getLatitude())
                .telephone(item.getTelephone())
                .title(item.getTitle())
                .overview(item.getOverview()).build();
    }

    public static TourDetailRepeatRoom convertToTourDetailRepeatRoom(
            TourDetailRepeatRoomRequest item,
            TourContent tourContent
    ) {
        return TourDetailRepeatRoom.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .roomInfoNo(item.getRoomInfoNo())
                .roomTitle(item.getRoomTitle())
                .roomSize1(item.getRoomSize1())
                .roomSize2(item.getRoomSize2())
                .roomCount(item.getRoomCount())
                .roomBaseCount(item.getRoomBaseCount())
                .roomMaxCount(item.getRoomMaxCount())
                .roomOffSeasonMinFee1(item.getRoomOffSeasonMinFee1())
                .roomOffSeasonMinFee2(item.getRoomOffSeasonMinFee2())
                .roomPeakSeasonMinFee1(item.getRoomPeakSeasonMinFee1())
                .roomPeakSeasonMinFee2(item.getRoomPeakSeasonMinFee2())
                .roomIntro(item.getRoomIntro())
                .roomBathFacility(item.getRoomBathFacility())
                .roomBath(item.getRoomBath())
                .roomHomeTheater(item.getRoomHomeTheater())
                .roomAirCondition(item.getRoomAirCondition())
                .roomTv(item.getRoomTv())
                .roomPc(item.getRoomPc())
                .roomCable(item.getRoomCable())
                .roomInternet(item.getRoomInternet())
                .roomRefrigerator(item.getRoomRefrigerator())
                .roomToiletries(item.getRoomToiletries())
                .roomSofa(item.getRoomSofa())
                .roomCook(item.getRoomCook())
                .roomTable(item.getRoomTable())
                .roomHairDryer(item.getRoomHairDryer())
                .roomImg1(item.getRoomImg1())
                .roomImg1Alt(item.getRoomImg1Alt())
                .roomImg2(item.getRoomImg2())
                .roomImg2Alt(item.getRoomImg2Alt())
                .roomImg3(item.getRoomImg3())
                .roomImg3Alt(item.getRoomImg3Alt())
                .roomImg4(item.getRoomImg4())
                .roomImg4Alt(item.getRoomImg4Alt())
                .roomImg5(item.getRoomImg5())
                .roomImg5Alt(item.getRoomImg5Alt())
                .build();
    }

    public static TourDetailIntroCulture convertToTourDetailIntroCulture(
            TourDetailIntroCultureRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroCulture.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .accomCountCulture(item.getAccomCountCulture())
                .chkCreditCardCulture(item.getChkCreditCardCulture())
                .infoCenterCulture(item.getInfoCenterCulture())
                .parkingCulture(item.getParkingCulture())
                .parkingFee(item.getParkingFee())
                .restDateCulture(item.getRestDateCulture())
                .useFee(item.getUseFee())
                .useTimeCulture(item.getUseTimeCulture())
                .scale(item.getScale())
                .build();
    }
    public static TourDetailIntroFood convertToTourDetailIntroFood(
            TourDetailIntroFoodRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroFood.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .chkCreditCardFood(item.getChkCreditCardFood())
                .firstMenu(item.getFirstMenu())
                .infoCenterFood(item.getInfoCenterFood())
                .kidsFacility(item.getKidsFacility())
                .openTimeFood(item.getOpenTimeFood())
                .packing(item.getPacking())
                .parkingFood(item.getParkingFood())
                .reservationFood(item.getReservationFood())
                .restDateFood(item.getRestDateFood())
                .smoking(item.getSmoking())
                .treatMenu(item.getTreatMenu())
                .build();
    }
    public static TourDetailIntroLeports convertToTourDetailIntroLeports(
            TourDetailIntroLeportsRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroLeports.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .accomCountLeports(item.getAccomCountLeports())
                .chkCreditCardLeports(item.getChkCreditCardLeports())
                .expAgeRangeLeports(item.getExpAgeRangeLeports())
                .infoCenterLeports(item.getInfoCenterLeports())
                .openPeriod(item.getOpenPeriod())
                .parkingFeeLeports(item.getParkingFeeLeports())
                .parkingLeports(item.getParkingLeports())
                .reservation(item.getReservation())
                .restDateLeports(item.getRestDateLeports())
                .useFeeLeports(item.getUseFeeLeports())
                .useTimeLeports(item.getUseTimeLeports())
                .build();
    }

    public static TourDetailIntroLodging convertToTourDetailIntroLodging(
            TourDetailIntroLodgingRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroLodging.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .accomCountLodging(item.getAccomCountLodging())
                .benikia(item.getBenikia())
                .checkInTime(item.getCheckInTime())
                .checkOutTime(item.getCheckOutTime())
                .chkCooking(item.getChkCooking())
                .foodPlace(item.getFoodPlace())
                .goodStay(item.getGoodStay())
                .hanok(item.getHanok())
                .infoCenterLodging(item.getInfoCenterLodging())
                .parkingLodging(item.getParkingLodging())
                .pickup(item.getPickup())
                .roomCount(item.getRoomCount())
                .reservationLodging(item.getReservationLodging())
                .reservationUrl(item.getReservationUrl())
                .roomType(item.getRoomType())
                .scaleLodging(item.getScaleLodging())
                .subFacility(item.getSubFacility())
                .barbecue(item.getBarbecue())
                .beauty(item.getBeauty())
                .beverage(item.getBeverage())
                .bicycle(item.getBicycle())
                .campfire(item.getCampfire())
                .fitness(item.getFitness())
                .karaoke(item.getKaraoke())
                .publicBath(item.getPublicBath())
                .publicPc(item.getPublicPc())
                .sauna(item.getSauna())
                .seminar(item.getSeminar())
                .sports(item.getSports())
                .refundRegulation(item.getRefundRegulation())
                .build();
    }

    public static TourDetailIntroShopping convertToTourDetailIntroShopping(
            TourDetailIntroShoppingRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroShopping.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .chkCreditCard(item.getChkCreditCard())
                .infoCenterShopping(item.getInfoCenterShopping())
                .openTime(item.getOpenTime())
                .parkingShopping(item.getParkingShopping())
                .restDateShopping(item.getRestDateShopping())
                .restRoom(item.getRestRoom())
                .saleItem(item.getSaleItem())
                .scaleShopping(item.getScaleShopping())
                .build();
    }

    public static TourDetailIntroTourist convertToTourDetailIntroTourist(
            TourDetailIntroTouristRequest item,
            TourContent tourContent
    ) {
        return TourDetailIntroTourist.builder()
                .content(tourContent)
                .contentTypeId(item.getContentTypeId())
                .accomCount(item.getAccomCount())
                .chkCreditCard(item.getChkCreditCard())
                .expAgeRange(item.getExpAgeRange())
                .expGuide(item.getExpGuide())
                .heritage1(item.getHeritage1())
                .heritage2(item.getHeritage2())
                .heritage3(item.getHeritage3())
                .infoCenter(item.getInfoCenter())
                .openDate(item.getOpenDate())
                .parking(item.getParking())
                .restDate(item.getRestDate())
                .useSeason(item.getUseSeason())
                .useTime(item.getUseTime())
                .build();
    }

}
