import React, { useState } from "react";
import {
  getPetTourSyncList,
  getPetTourCommonInfo,
  getPetTourAccompanyInfo,
  getPetTourImageInfo,
  getPetTourRepeatInfo,
  getPetTourIntroInfo,
} from "../../api/ExternalTourService";

import {
  saveSyncDataService,
  saveOverviewDataService,
  savePetDataService,
  saveImageDataService,
  saveRepeatCommonDataService,
  saveRepeatRoomDataService,
  saveIntroCultureDataService,
  saveIntroFoodDataService,
  saveIntroLeportsDataService,
  saveIntroLodgingDataService,
  saveIntroShoppingDataService,
  saveIntroTouristDataService,
} from "../../api/TourSyncService";

import { toast } from "react-toastify";
import classes from "./DataSync.module.css";

const introTypeMap = {
  "12": "tourist",
  "14": "culture",
  "15": "festival",
  "28": "leports",
  "32": "lodging",
  "38": "shopping",
  "39": "food",
};

const DataSync = () => {
  const [loading, setLoading] = useState({
    syncList: false,
    syncSave: false,
    overview: false,
    intro: false,
    repeat: false,
    images: false,
    pet: false,
  });

  const setBusy = (k, v) => setLoading((s) => ({ ...s, [k]: v }));

  const readLocal = () => {
    const data = localStorage.getItem("petTourData");
    return data ? JSON.parse(data) : null;
  };
  const getSyncListHandler = async () => {
    setBusy("syncList", true);
    const tid = toast.loading("목록을 수집하고 있어요…");
    try {
      const response = await getPetTourSyncList();
      const list = response?.response?.body?.items?.item ?? [];
      const mapped = list.map((item) => ({
        contentId: item.contentid,
        address: item.addr1,
        detailAddress: item.addr2,
        postCode: item.zipcode,
        areaCode: item.areacode,
        sigunguCode: item.sigungucode,
        cat1: item.cat1,
        cat2: item.cat2,
        cat3: item.cat3,
        contentTypeId: item.contenttypeid,
        originalImageUrl: item.firstimage,
        thumbnailImageUrl: item.firstimage2,
        longitude: item.mapx,
        latitude: item.mapy,
        telephone: item.tel,
        title: item.title,
        overview: "",
      }));
      localStorage.setItem("petTourData", JSON.stringify(mapped));
      toast.update(tid, {
        render: `수집 완료! 로컬에 ${mapped.length.toLocaleString()}건 저장되었습니다.`,
        type: "success",
        isLoading: false,
        autoClose: 2000,
      });
    } catch (e) {
      toast.update(tid, {
        render: "수집 중 오류가 발생했어요.",
        type: "error",
        isLoading: false,
        autoClose: 2500,
      });
    } finally {
      setBusy("syncList", false);
    }
  };

  const showAndSaveSyncDataHandler = async () => {
    setBusy("syncSave", true);
    const data = readLocal();
    if (!data) {
      toast.info("저장된 로컬 데이터가 없습니다.");
      setBusy("syncSave", false);
      return;
    }
    const tid = toast.loading(`서버에 ${data.length.toLocaleString()}건 저장 중…`);
    try {
      await saveSyncDataService(data);
      toast.update(tid, {
        render: "서버 저장 완료!",
        type: "success",
        isLoading: false,
        autoClose: 1800,
      });
    } catch (e) {
      toast.update(tid, {
        render: "서버 저장에 실패했어요.",
        type: "error",
        isLoading: false,
        autoClose: 2200,
      });
    } finally {
      setBusy("syncSave", false);
    }
  };

  const getOverviewInfoHandler = async (contentId) => {
    try {
      const res = await getPetTourCommonInfo(contentId);
      const ov = res?.response?.body?.items?.item?.[0]?.overview ?? "";
      await saveOverviewDataService(contentId,ov);
      return true;
    } catch {
      return false;
    }
  };

  const saveAllOverviewInfo = async () => {
    const data = readLocal();
    if (!data) return toast.info("먼저 ‘전체 데이터 수집’을 진행해 주세요.");
    setBusy("overview", true);
    const tid = toast.loading("공통 정보(overview) 저장 중…");
    let ok = 0, fail = 0;
    for (const it of data) (await getOverviewInfoHandler(it.contentId)) ? ok++ : fail++;
    toast.update(tid, {
      render: `Overview 저장 완료 · 성공 ${ok} / 실패 ${fail}`,
      type: fail ? "warning" : "success",
      isLoading: false,
      autoClose: 2200,
    });
    setBusy("overview", false);
  };

  const getIntroInfoHandler = async (contentId, contentTypeId) => {
    const introType = introTypeMap[contentTypeId];
    if (!introType) return true;
    try {
      const res = await getPetTourIntroInfo(contentId, contentTypeId);
      const raw = res?.response?.body?.items?.item;
      const list = Array.isArray(raw) ? raw : [raw];

      const mapped = list.map((item) => {
        switch (introType) {
          case "tourist":
            return {
              contentTypeId,
              accomCount: item.accomcount,
              chkCreditCard: item.chkcreditcard,
              expAgeRange: item.expagerange,
              expGuide: item.expguide,
              heritage1: item.heritage1,
              heritage2: item.heritage2,
              heritage3: item.heritage3,
              infoCenter: item.infocenter,
              openDate: item.opendate,
              parking: item.parking,
              restDate: item.restdate,
              useSeason: item.useseason,
              useTime: item.usetime,
            };
          case "culture":
            return {
              contentTypeId,
              accomCountCulture: item.accomcountculture,
              chkCreditCardCulture: item.chkcreditcardculture,
              discountInfo: item.discountinfo,
              infoCenterCulture: item.infocenterculture,
              parkingCulture: item.parkingculture,
              parkingFee: item.parkingfee,
              restDateCulture: item.restdateculture,
              useFee: item.usefee,
              useTimeCulture: item.usetimeculture,
              scale: item.scale,
            };
          case "festival":
            return {
              contentTypeId,
              ageLimit: item.agelimit,
              bookingPlace: item.bookingplace,
              discountInfoFestival: item.discountinfofestival,
              eventEndDate: item.eventenddate,
              eventHomePage: item.eventhomepage,
              eventPlace: item.eventplace,
              eventStartDate: item.eventstartdate,
              festivalGrade: item.festivalgrade,
              placeInfo: item.placeinfo,
              playTime: item.playtime,
              program: item.program,
              spendTimeFestival: item.spendtimefestival,
              sponsor1: item.sponsor1,
              sponsor1Tel: item.sponsor1tel,
              sponsor2: item.sponsor2,
              sponsor2Tel: item.sponsor2tel,
              subEvent: item.subevent,
              useTimeFestival: item.usetimefestival,
            };
          case "leports":
            return {
              contentTypeId,
              accomCountLeports: item.accomcountleports,
              chkCreditCardLeports: item.chkcreditcardleports,
              expAgeRangeLeports: item.expagerangeleports,
              infoCenterLeports: item.infocenterleports,
              openPeriod: item.openperiod,
              parkingFeeLeports: item.parkingfeeleports,
              parkingLeports: item.parkingleports,
              reservation: item.reservation,
              restDateLeports: item.restdateleports,
              scaleLeports: item.scaleleports,
              useFeeLeports: item.usefeeleports,
              useTimeLeports: item.usetimeleports,
            };
          case "lodging":
            return {
              contentTypeId,
              accomCountLodging: item.accomcountlodging,
              benikia: item.benikia,
              checkInTime: item.checkintime,
              checkOutTime: item.checkouttime,
              chkCooking: item.chkcooking,
              foodPlace: item.foodplace,
              goodStay: item.goodstay,
              hanok: item.hanok,
              infoCenterLodging: item.infocenterlodging,
              parkingLodging: item.parkinglodging,
              pickup: item.pickup,
              roomCount: item.roomcount,
              reservationLodging: item.reservationlodging,
              reservationUrl: item.reservationurl,
              roomType: item.roomtype,
              scaleLodging: item.scalelodging,
              subFacility: item.subfacility,
              barbecue: item.barbecue,
              beauty: item.beauty,
              beverage: item.beverage,
              bicycle: item.bicycle,
              campfire: item.campfire,
              fitness: item.fitness,
              karaoke: item.karaoke,
              publicBath: item.publicbath,
              publicPc: item.publicpc,
              sauna: item.sauna,
              seminar: item.seminar,
              sports: item.sports,
              refundRegulation: item.refundregulation,
            };
          case "shopping":
            return {
              contentTypeId,
              chkCreditCard: item.chkcreditcardshopping,
              cultureCenter: item.culturecenter,
              fairDay: item.fairday,
              infoCenterShopping: item.infocentershopping,
              openDateShopping: item.opendateshopping,
              openTime: item.opentime,
              parkingShopping: item.parkingshopping,
              restDateShopping: item.restdateshopping,
              restRoom: item.restroom,
              saleItem: item.saleitem,
              saleItemCost: item.saleitemcost,
              scaleShopping: item.scaleshopping,
              shopGuide: item.shopguide,
            };
          case "food":
            return {
              contentTypeId,
              chkCreditCardFood: item.chkcreditcardfood,
              discountInfoFood: item.discountinfofood,
              firstMenu: item.firstmenu,
              infoCenterFood: item.infocenterfood,
              kidsFacility: item.kidsfacility,
              openDateFood: item.opendatefood,
              openTimeFood: item.opentimefood,
              packing: item.packing,
              parkingFood: item.parkingfood,
              reservationFood: item.reservationfood,
              restDateFood: item.restdatesfood ?? item.restdatefood,
              scaleFood: item.scalefood,
              seat: item.seat,
              smoking: item.smoking,
              treatMenu: item.treatmenu,
            };
          default:
            return null;
        }
      });

      if (mapped.length) {
        switch (introType) {
          case "tourist":
            await saveIntroTouristDataService(contentId,mapped);
            break;
          case "culture":
            await saveIntroCultureDataService(contentId,mapped);
            break;
          case "leports":
            await saveIntroLeportsDataService(contentId,mapped);
            break;
          case "lodging":
            await saveIntroLodgingDataService(contentId,mapped);
            break;
          case "shopping":
            await saveIntroShoppingDataService(contentId,mapped);
            break;
          case "food":
            await saveIntroFoodDataService(contentId,mapped);
            break;
          default:
            break;
        }
      }
      return true;
    } catch {
      return false;
    }
  };

  const saveAllIntroInfo = async () => {
    const data = readLocal();
    if (!data) return toast.info("먼저 ‘전체 데이터 수집’을 진행해 주세요.");
    setBusy("intro", true);
    const tid = toast.loading("소개(Intro) 정보 저장 중…");
    let ok = 0,
      fail = 0;
    for (const it of data) {
      const res = await getIntroInfoHandler(it.contentId, it.contentTypeId);
      res ? ok++ : fail++;
    }
    toast.update(tid, {
      render: `Intro 저장 완료 · 성공 ${ok} / 실패 ${fail}`,
      type: fail ? "warning" : "success",
      isLoading: false,
      autoClose: 2200,
    });
    setBusy("intro", false);
  };

  const getRepeatInfoHandler = async (contentId, contentTypeId) => {
    try {
      const res = await getPetTourRepeatInfo(contentId, contentTypeId);
      const items = res?.response?.body?.items?.item ?? [];
      if (contentTypeId === "32") {
        const mapped = items.map((item) => ({
          roomInfoNo: item.roominfono,
          roomTitle: item.roomtitle,
          roomSize1: item.roomsize1,
          roomSize2: item.roomsize2,
          roomCount: item.roomcount,
          roomBaseCount: item.roombasecount,
          roomMaxCount: item.roommaxcount,
          roomOffSeasonMinFee1: item.roomoffseasonminfee1,
          roomOffSeasonMinFee2: item.roomoffseasonminfee2,
          roomPeakSeasonMinFee1: item.roompeakseasonminfee1,
          roomPeakSeasonMinFee2: item.roompeakseasonminfee2,
          roomIntro: item.roomintro,
          roomBathFacility: item.roombathfacility,
          roomBath: item.roombath,
          roomHomeTheater: item.roomhometheater,
          roomAirCondition: item.roomaircondition,
          roomTv: item.roomtv,
          roomPc: item.roompc,
          roomCable: item.roomcable,
          roomInternet: item.roominternet,
          roomRefrigerator: item.roomrefrigerator,
          roomToiletries: item.roomtoiletries,
          roomSofa: item.roomsofa,
          roomCook: item.roomcook,
          roomTable: item.roomtable,
          roomHairDryer: item.roomhairdryer,
          roomImg1: item.roomimg1,
          roomImg1Alt: item.roomimg1alt,
          roomImg2: item.roomimg2,
          roomImg2Alt: item.roomimg2alt,
          roomImg3: item.roomimg3,
          roomImg3Alt: item.roomimg3alt,
          roomImg4: item.roomimg4,
          roomImg4Alt: item.roomimg4alt,
          roomImg5: item.roomimg5,
          roomImg5Alt: item.roomimg5alt,
          contentTypeId: contentTypeId,
        }));
        await saveRepeatRoomDataService(contentId, mapped);
      } else {
        const mapped = items.map((item) => ({
          infoName: item.infoname,
          infoText: item.infotext,
          contentTypeId: item.contenttypeid,
        }));
        await saveRepeatCommonDataService(contentId, mapped);
      }
      return true;
    } catch {
      return false;
    }
  };

  const saveAllRepeatInfo = async () => {
    const data = readLocal();
    if (!data) return toast.info("먼저 ‘전체 데이터 수집’을 진행해 주세요.");
    setBusy("repeat", true);
    const tid = toast.loading("반복 정보 저장 중…");
    let ok = 0,
      fail = 0;
    for (const it of data) (await getRepeatInfoHandler(it.contentId, it.contentTypeId)) ? ok++ : fail++;
    toast.update(tid, {
      render: `반복 정보 저장 완료 · 성공 ${ok} / 실패 ${fail}`,
      type: fail ? "warning" : "success",
      isLoading: false,
      autoClose: 2200,
    });
    setBusy("repeat", false);
  };
  const getImageInfoHandler = async (contentId) => {
    try {
      const res = await getPetTourImageInfo(contentId);
      const list = res?.response?.body?.items?.item ?? [];
      const mapped = list.map((item) => ({
        originImgUrl: item.originimgurl,
        imgName: item.imgname,
        smallImgUrl: item.smallimageurl,
      }));
      await saveImageDataService(contentId,mapped);
      return true;
    } catch {
      return false;
    }
  };

  const saveAllImageInfo = async () => {
    const data = readLocal();
    if (!data) return toast.info("먼저 ‘전체 데이터 수집’을 진행해 주세요.");
    setBusy("images", true);
    const tid = toast.loading("이미지 정보 저장 중…");
    let ok = 0,
      fail = 0;
    for (const it of data) (await getImageInfoHandler(it.contentId)) ? ok++ : fail++;
    toast.update(tid, {
      render: `이미지 저장 완료 · 성공 ${ok} / 실패 ${fail}`,
      type: fail ? "warning" : "success",
      isLoading: false,
      autoClose: 2200,
    });
    setBusy("images", false);
  };


  const getAccompanyInfoHandler = async (contentId) => {
    try {
      const res = await getPetTourAccompanyInfo(contentId);
      const list = res?.response?.body?.items?.item ?? [];
      const mapped = list.map((item) => ({
        relaAcdntRiskMtr: item.relaAcdntRiskMtr,
        acmpyTypeCd: item.acmpyTypeCd,
        relaPosesFclty: item.relaPosesFclty,
        relaFrnshPrdlst: item.relaFrnshPrdlst,
        etcAcmpyInfo: item.etcAcmpyInfo,
        acmpyPsblCpam: item.acmpyPsblCpam,
        relaRntlPrdlst: item.relaRntlPrdlst,
        acmpyNeedMtr: item.acmpyNeedMtr,
      }));
      await savePetDataService(contentId,mapped);
      return true;
    } catch {
      return false;
    }
  };

  const saveAllAccompanyInfo = async () => {
    const data = readLocal();
    if (!data) return toast.info("먼저 ‘전체 데이터 수집’을 진행해 주세요.");
    setBusy("pet", true);
    const tid = toast.loading("동반(펫) 정보 저장 중…");
    let ok = 0,
      fail = 0;
    for (const it of data) (await getAccompanyInfoHandler(it.contentId)) ? ok++ : fail++;
    toast.update(tid, {
      render: `동반 정보 저장 완료 · 성공 ${ok} / 실패 ${fail}`,
      type: fail ? "warning" : "success",
      isLoading: false,
      autoClose: 2200,
    });
    setBusy("pet", false);
  };

  return (
    <div className={classes.syncWrap}>
      <h1 className={classes.pageTitle}>데이터 동기화</h1>
      <p className={classes.desc}>투어 데이터에 대한 동기화를 진행할 수 있습니다.</p>
      <div className={classes.grid}>
        <section className={classes.card}>
          <h2 className={classes.cardTitle}>전체 데이터 수집</h2>
          <p className={classes.cardText}>공공데이터 목록을 수집해 로컬에 저장합니다.</p>
          <div className={classes.btnRow}>
            <button className={classes.btnPrimary} onClick={getSyncListHandler} disabled={loading.syncList}>
              수집하기
            </button>
            <button className={classes.btnGhost} onClick={showAndSaveSyncDataHandler} disabled={loading.syncSave}>
              수집 데이터 저장
            </button>
          </div>
        </section>

        <section className={classes.card}>
          <h2 className={classes.cardTitle}>반려동물 동반 정보</h2>
          <p className={classes.cardText}>accompany(동반 가능 등) 상세 정보 저장</p>
          <button className={classes.btnPrimary} onClick={saveAllAccompanyInfo} disabled={loading.pet}>
            수집 및 저장
          </button>
        </section>

        <section className={classes.card}>
          <h2 className={classes.cardTitle}>이미지 정보</h2>
          <p className={classes.cardText}>장소 이미지 원본/썸네일 저장</p>
          <button className={classes.btnPrimary} onClick={saveAllImageInfo} disabled={loading.images}>
            수집 및 저장
          </button>
        </section>

        <section className={classes.card}>
          <h2 className={classes.cardTitle}>반복 정보</h2>
          <p className={classes.cardText}>객실/공통 반복 정보 저장</p>
          <button className={classes.btnPrimary} onClick={saveAllRepeatInfo} disabled={loading.repeat}>
            수집 및 저장
          </button>
        </section>

        <section className={classes.card}>
          <h2 className={classes.cardTitle}>소개(Intro) 정보</h2>
          <p className={classes.cardText}>콘텐츠 타입별 소개 필드 저장</p>
          <button className={classes.btnPrimary} onClick={saveAllIntroInfo} disabled={loading.intro}>
            수집 및 저장
          </button>
        </section>

        <section className={classes.card}>
          <h2 className={classes.cardTitle}>공통 개요(Overview)</h2>
          <p className={classes.cardText}>상세 개요 텍스트 저장</p>
          <button className={classes.btnPrimary} onClick={saveAllOverviewInfo} disabled={loading.overview}>
            수집 및 저장
          </button>
        </section>
      </div>
    </div>
  );
};

export default DataSync;
