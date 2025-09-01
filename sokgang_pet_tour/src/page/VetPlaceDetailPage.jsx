import React, { useEffect, useMemo, useState } from "react";
import classes from "./VetPlaceDetailPage.module.css";
import { getVetPlaceDetailService } from "../api/VetService";
import { toast } from "react-toastify";
import { motion, AnimatePresence } from "framer-motion";

import {
  MdLocalPharmacy,
  MdLocalHospital,
  MdPlace,
  MdAccessTime,
  MdPhone,
  MdContentCopy,
  MdLocalParking,
  MdPets,
  MdExpandMore,
  MdExpandLess,
  MdDirections
} from "react-icons/md";
import Loading from "../components/LayoutComponents/Loading";

const naverMapURL = 'http://map.naver.com/index.nhn?';
const DAY_ORDER = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", "HOLIDAY"];
const DAY_KR_FULL = {
  MON: "월요일",
  TUE: "화요일",
  WED: "수요일",
  THU: "목요일",
  FRI: "금요일",
  SAT: "토요일",
  SUN: "일요일",
  HOLIDAY: "공휴일",
};
const formatTime = (t) => (t ? t.slice(0, 5) : "—");

const CategoryBadge = ({ category }) => {
  const isPharmacy = category === "PHARMACY";
  const isHospital = category === "HOSPITAL";
  const Icon = isPharmacy ? MdLocalPharmacy : isHospital ? MdLocalHospital : MdPets;
  const label = isPharmacy ? "동물약국" : isHospital ? "동물병원" : "반려케어";
  return (
    <span className={`${classes.badge} ${classes.cat}`}>
      <Icon size={14} />
      {label}
    </span>
  );
};

const StatusBadge = ({ open }) => (
  <span className={`${classes.badge} ${open ? classes.open : classes.closed}`}>
    <MdAccessTime size={14} />
    {open ? "영업중" : "영업종료"}
  </span>
);

const VetPlaceDetailPage = ({ id, location }) => {

  const [place, setPlace] = useState(null);
  const [expanded, setExpanded] = useState(false);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const fetchPlaceData = async () => {
      setLoading(true);
      const placeResponse = await getVetPlaceDetailService(id);
      if (placeResponse?.success) setPlace(placeResponse.data);
      setLoading(false);
    };
    fetchPlaceData();
  }, [id]);

  const {
    placeName,
    address,
    phoneNumber,
    category,
    maxSizeInfo,
    parking,
    operatingHours = [],
    open,
  } = place ?? {};

  const hoursSorted = useMemo(() => {
    const byKey = Object.fromEntries(operatingHours.map((o) => [o.dayType, o]));
    return DAY_ORDER.map((d) => byKey[d]).filter(Boolean);
  }, [operatingHours]);

  const copyAddress = async () => {
    try {
      await navigator.clipboard.writeText(address || "");
      toast.success("주소를 복사하였습니다.", { position: "top-center", autoClose: 500 });
    } catch {
      toast.error("주소 복사에 실패하였습니다.\n다시 시도해주세요.", { position: "top-center", autoClose: 800 });
    }
  };

  const openNaverDirections = () => {
        if (!place) return;
        
        const startName = "현재 위치";
        const startLat = location.latitude;
        const startLng = location.longitude;

        const destName = placeName || "목적지";
        const destLat = place.latitude;
        const destLng = place.longitude;
        let url = `${naverMapURL}slng=${startLng}&slat=${startLat}&stext=${startName}&elng=${destLng}&elat=${destLat}&pathType=0&showMap=true&etext=${destName}&menu=route`;
        window.open(url, "_blank", "noopener,noreferrer");
  };

  if (!place) return null;

  return (
    <section className={classes.panel}>
       {loading && <Loading />}
      <div className={classes.header}>
        <h1 className={classes.title}>{placeName}</h1>
        <div className={classes.metaRow}>
          <CategoryBadge category={category} />
          <StatusBadge open={!!open} />
        </div>
        <div className={classes.metaColumn}>
          <div className={classes.metaLine}>
            <MdAccessTime size={14} />
            <span>
              {(() => {
                const weekday =
                  operatingHours.find((o) => ["MON","TUE","WED","THU","FRI"].includes(o.dayType)) ||
                  operatingHours[0];
                return weekday
                  ? `${formatTime(weekday.openTime)}–${formatTime(weekday.closeTime)}`
                  : "시간 정보 없음";
              })()}
            </span>
          </div>

          {typeof parking === "boolean" && (
            <div className={classes.metaLine}>
              <MdLocalParking size={14} />
              <span>주차 {parking ? "가능" : "불가"}</span>
            </div>
          )}

          {phoneNumber && (
            <div className={classes.metaLine}>
              <MdPhone size={14} />
              <span>{phoneNumber}</span>
            </div>
          )}

          {address && (
            <div className={classes.metaLine}>
              <MdPlace size={14} />
              <span>{address}</span>
            </div>
          )}
        </div>
      </div>
      <div className={classes.actionsRow}>
        <button 
            type="button" className={classes.noActionBtn}>
            <MdPets size={14} />
            {maxSizeInfo}
        </button>
        <motion.button 
            whileHover={{ scale : 1.05 }}
            type="button" className={classes.actionBtn} onClick={copyAddress}>
          <MdContentCopy size={14} />
          주소 복사
        </motion.button>
        <motion.button 
            whileHover={{ scale : 1.05 }}
            type="button" className={classes.actionBtn} onClick={openNaverDirections}>
          <MdDirections size={16} />
          길찾기
        </motion.button>
      </div>
      <div className={classes.section}>
        <button
          type="button"
          className={classes.collapseBtn}
          onClick={() => setExpanded((v) => !v)}
          aria-expanded={expanded}
        >
          <span>운영시간</span>
          {expanded ? <MdExpandLess size={16} /> : <MdExpandMore size={16} />}
        </button>

        <AnimatePresence initial={false}>
          {expanded && (
            <motion.div
              key="hours"
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: "auto", opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.22, ease: "easeInOut" }}
              className={classes.hoursWrapper}
            >
              <ul className={classes.hoursList}>
                {hoursSorted.map((h) => (
                  <li key={h.dayType} className={classes.hoursItem}>
                    <span className={classes.dayLabel}>{DAY_KR_FULL[h.dayType]}</span>
                    <span className={classes.timeLabel}>
                      {h.open ? `${formatTime(h.openTime)}–${formatTime(h.closeTime)}` : "휴무"}
                    </span>
                  </li>
                ))}
              </ul>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </section>
  );
};

export default VetPlaceDetailPage;
