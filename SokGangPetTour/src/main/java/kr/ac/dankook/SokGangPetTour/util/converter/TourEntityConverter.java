package kr.ac.dankook.SokGangPetTour.util.converter;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourContentRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailImageRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailPetRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.entity.tour.*;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.*;

public class TourEntityConverter {


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
