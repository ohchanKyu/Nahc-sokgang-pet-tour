import { useMemo } from "react";
import classes from "./TourDetailIntro.module.css";
import { motion } from "framer-motion";

const variants = {
  container: { opacity: 0, y: 8 },
  animate: { opacity: 1, y: 0, transition: { duration: 0.35 } },
  list: { transition: { staggerChildren: 0.06 } },
  item: { opacity: 0, y: 6, transition: { duration: 0.25 } },
  itemShow: { opacity: 1, y: 0, transition: { duration: 0.25 } },
};


const introTypeMap = {
  "12": "tourist",
  "14": "culture",
  "15": "festival",
  "28": "leports",
  "32": "lodging",
  "38": "shopping",
  "39": "food",
};

const isFilled = (v) => {
  if (v === null || v === undefined) return false;
  const s = String(v).trim();
  if (s === "" || s === "0") return false;
  return true;
};

const html = (s) => ({ __html: String(s || "") });

const Row = ({ label, value }) => {
  if (!isFilled(value)) return null;
  return (
    <div className={classes.row}>
      <div className={classes.label}>{label}</div>
      <div className={classes.value} dangerouslySetInnerHTML={html(value)} />
    </div>
  );
};

const Chips = ({ title, items }) => {
  const chipItems = (items || []).filter(isFilled);
  if (chipItems.length === 0) return null;
  return (
    <div className={classes.chipsWrap}>
      {title && <div className={classes.chipsTitle}>{title}</div>}
      <div className={classes.chips}>
        {chipItems.map((t, i) => (
          <span key={`${t}-${i}`} className={classes.chip}>{t}</span>
        ))}
      </div>
    </div>
  );
};

const SectionCard = ({ title, children }) => {
  return (
    <section className={classes.card}>
      <div className={classes.cardBar} />
      <h3 className={classes.cardTitle}>{title}</h3>
      <div className={classes.body}>{children}</div>
    </section>
  );
};

const useIntroData = (tourDetail, typeId) => {
  const key = introTypeMap[String(typeId)];
  return useMemo(() => {
    if (!tourDetail) return {};
    switch (key) {
      case "tourist":
        return { list: tourDetail.introTourists || [], type: "tourist" };
      case "culture":
        return { list: tourDetail.introCultures || [], type: "culture" };
      case "leports":
        return { list: tourDetail.introLeports || [], type: "leports" };
      case "lodging":
        return { list: tourDetail.introLodgings || [], type: "lodging" };
      case "shopping":
        return { list: tourDetail.introShopping || [], type: "shopping" };
      case "food":
        return { list: tourDetail.introFoods || [], type: "food" };
      case "festival":
        return { list: tourDetail.introFestivals || [], type: "festival" }; 
      default:
        return { list: [], type: undefined };
    }
  }, [tourDetail, key]);
};

const TourDetailIntro = ({ tourDetail, typeId }) => {

  const { list, type } = useIntroData(tourDetail, typeId);

  if (!list || list.length === 0){
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
          <SectionCard title="관광정보 안내">
            <div key='관광정보 안내 추가 정보가 없습니다.' className={classes.card}>
              <div className={classes.row}>
                <div className={classes.label}>관광정보 안내 추가 정보가 없습니다.</div>
              </div>
            </div>

          </SectionCard>
      </motion.div>
    )
  }

  const data = list[0];

  if (type === "tourist") {
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="관광정보 안내">
          <Row label="관람 가능 연령" value={data.expAgeRange} />
          <Row label="해설/가이드" value={data.expGuide} />
          <Row label="관광안내" value={data.infoCenter} />
          <Row label="개장일" value={data.openDate} />
          <Row label="이용 시즌" value={data.useSeason} />
          <Row label="이용 시간" value={data.useTime} />
          <Row label="주차" value={data.parking} />
          <Row label="휴무일" value={data.restDate} />
          <Row label="신용카드" value={data.chkCreditCard} />
          {isFilled(data.accomCount) && (
            <Chips title="수용 인원" items={[data.accomCount]} />
          )}
          <Chips
            title="문화재"
            items={[
              data.heritage1 !== "0" ? "유형문화재" : "",
              data.heritage2 !== "0" ? "기념물" : "",
              data.heritage3 !== "0" ? "사적" : "",
            ]}
          />
        </SectionCard>
      </motion.div>
    );
  }

  if (type === "culture") {
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="문화시설 안내">
          <Row label="관람정원" value={data.accomCountCulture} />
          <Row label="이용 시간" value={data.useTimeCulture} />
          <Row label="이용 요금" value={data.useFee} />
          <Row label="주차" value={data.parkingCulture} />
          <Row label="주차요금" value={data.parkingFee} />
          <Row label="휴관일" value={data.restDateCulture} />
          <Row label="문의" value={data.infoCenterCulture} />
          <Row label="신용카드" value={data.chkCreditCardCulture} />
          <Row label="규모" value={data.scale} />
        </SectionCard>
      </motion.div>
    );
  }

  if (type === "leports") {
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="레포츠 안내">
          <Row label="이용 연령" value={data.expAgeRangeLeports} />
          <Row label="운영 기간" value={data.openPeriod} />
          <Row label="이용 시간" value={data.useTimeLeports} />
          <Row label="이용 요금" value={data.useFeeLeports} />
          <Row label="예약" value={data.reservation} />
          <Row label="문의" value={data.infoCenterLeports} />
          <Row label="주차" value={data.parkingLeports} />
          <Row label="주차요금" value={data.parkingFeeLeports} />
          <Row label="휴무일" value={data.restDateLeports} />
          <Row label="신용카드" value={data.chkCreditCardLeports} />
          {isFilled(data.accomCountLeports) && (
            <Chips title="수용 인원" items={[data.accomCountLeports]} />
          )}
        </SectionCard>
      </motion.div>
    );
  }

  if (type === "lodging") {
    const amenityChips = [
      data.barbecue !== "0" ? "바비큐" : "",
      data.campfire !== "0" ? "캠프파이어" : "",
      data.fitness !== "0" ? "피트니스" : "",
      data.karaoke !== "0" ? "노래방" : "",
      data.publicBath !== "0" ? "대중탕" : "",
      data.publicPc !== "0" ? "공용 PC" : "",
      data.sauna !== "0" ? "사우나" : "",
      data.seminar !== "0" ? "세미나실" : "",
      data.sports !== "0" ? "스포츠시설" : "",
      data.benikia !== "0" ? "베니키아" : "",
      data.goodStay !== "0" ? "굿스테이" : "",
      data.hanok !== "0" ? "한옥" : "",
    ];
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="숙박 안내">
          <Row label="체크인" value={data.checkInTime} />
          <Row label="체크아웃" value={data.checkOutTime} />
          <Row label="객실 수" value={data.roomCount} />
          <Row label="객실 형태" value={data.roomType} />
          <Row label="조리" value={data.chkCooking} />
          <Row label="부대시설" value={data.subFacility} />
          <Row label="식음공간" value={data.foodPlace} />
          <Row label="픽업" value={data.pickup} />
          <Row label="주차" value={data.parkingLodging} />
          <Row label="예약" value={data.reservationLodging} />
          <Row label="예약(웹)" value={data.reservationUrl} />
          <Row label="문의" value={data.infoCenterLodging} />
          <Row label="환불규정" value={data.refundRegulation} />
          <Chips title="특징/인증" items={amenityChips} />
        </SectionCard>
      </motion.div>
    );
  }

  if (type === "shopping") {
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="쇼핑 안내">
          <Row label="운영 시간" value={data.openTime} />
          <Row label="휴무일" value={data.restDateShopping} />
          <Row label="판매 품목" value={data.saleItem} />
          <Row label="화장실" value={data.restRoom} />
          <Row label="주차" value={data.parkingShopping} />
          <Row label="문의" value={data.infoCenterShopping} />
          <Row label="신용카드" value={data.chkCreditCard} />
          <Row label="규모" value={data.scaleShopping} />
        </SectionCard>
      </motion.div>
    );
  }

  if (type === "food") {
    const chips = [
      data.smoking,
      data.kidsFacility !== "0" ? "키즈시설" : "",
    ];
    return (
      <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
        <SectionCard title="음식점 안내">
          <Row label="운영 시간" value={data.openTimeFood} />
          <Row label="휴무일" value={data.restDateFood} />
          <Row label="주차" value={data.parkingFood} />
          <Row label="포장" value={data.packing}/>
          <Row label="예약" value={data.reservationFood} />
          <Row label="문의" value={data.infoCenterFood} />
          <Row label="신용카드" value={data.chkCreditCardFood} />
          <Row label="대표 메뉴" value={data.firstMenu} />
          <Row label="취급 메뉴" value={data.treatMenu} />
          <Chips title="기타" items={chips} />
        </SectionCard>
      </motion.div>
    );
  }
  return null;
};

export default TourDetailIntro;
