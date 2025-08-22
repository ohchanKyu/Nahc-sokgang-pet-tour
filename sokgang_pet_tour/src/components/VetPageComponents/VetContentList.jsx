import React, { useEffect, useState } from "react";
import classes from "./VetContentList.module.css";
import { getVetPlacesByDistService, getVetPlacesByTodayCount, getVetPlacesByFilterService } from "../../api/VetService";
import { FaLocationDot } from "react-icons/fa6";
import { MdLocalHospital, MdLocalPharmacy, MdLocalParking, MdPhone, MdSchedule } from "react-icons/md";
import { AnimatePresence, motion } from "framer-motion";

const CATEGORY_LABEL = { HOSPITAL: "동물병원", PHARMACY: "동물약국" };
const DAY_ORDER = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", "HOLIDAY"];
const DAY_LABEL = { MON: "월요일", TUE: "화요일", WED: "수요일", THU: "목요일", FRI: "금요일", SAT: "토요일", SUN: "일요일", HOLIDAY: "공휴일" };

const todayKey = () => ["SUN","MON","TUE","WED","THU","FRI","SAT"][new Date().getDay()];
const hhmm = (t) => (t ? t.slice(0,5) : "");

const VetContentList = ({ type = "count", onSelect, location }) => {

  const [places, setPlaces] = useState([]);
  const [loading, setLoading] = useState(false);
  const [expandedId, setExpandedId] = useState(null);

  const toggle = (id) => setExpandedId(prev => (prev === id ? null : id));

  useEffect(() => {
    let mounted = true;
    const fetchData = async () => {
      setLoading(true);
      try {
        if (type === "count") {
          const res = await getVetPlacesByTodayCount();
          if (mounted && res?.success) setPlaces((res.data || []).slice(0, 15));
          else if (mounted) setPlaces([]);

        } else if (type === "dist"){
          const lat = Number(location?.latitude);
          const lng = Number(location?.longitude);
          if (!Number.isFinite(lat) || !Number.isFinite(lng)) { if (mounted) setPlaces([]); }
          else {
            const res = await getVetPlacesByDistService(lat, lng);
            if (mounted && res?.success) {
              const arr = (res.data || []).slice(0, 15).map(p => ({
                ...p.vetPlaceResponse,
                distString: p.distanceStringFormat || null,
              }));
              setPlaces(arr);
            } else if (mounted) setPlaces([]);
          }
        }else if (type === "openHos"){
            const res = await getVetPlacesByFilterService("HOSPITAL",false,true);
            if (mounted && res?.success) setPlaces((res.data || []).slice(0,30));
            else if (mounted) setPlaces([]);
        }else{
            const res = await getVetPlacesByFilterService("PHARMACY",false,true);
            if (mounted && res?.success) setPlaces((res.data || []).slice(0,30));
            else if (mounted) setPlaces([]);
        }
      } finally {
        setTimeout(() => mounted && setLoading(false), 250);
      }
    };
    fetchData();
    return () => { mounted = false; };
  }, [type, location]);

  if (loading) {
    return (
      <div className={classes.list}>
        {Array.from({ length: 6 }).map((_, i) => (
          <div key={i} className={`${classes.item} ${classes.skel}`}>
            <div className={classes.body}>
              <div className={classes.line} />
              <div className={classes.lineShort} />
              <div className={classes.line} />
            </div>
          </div>
        ))}
      </div>
    );
  }
  if (!places.length) return <div className={classes.empty}>결과가 없습니다.</div>;

  const tKey = todayKey();

  return (
    <div className={classes.list}>
      {places.map((p) => {
        const title = p.placeName || "이름 없음";
        const cat = CATEGORY_LABEL[p.category] || "시설";
        const todayRec = (p.operatingHours || []).find(r => r.dayType === tKey);
        const todayText = todayRec
          ? (todayRec.open && todayRec.openTime && todayRec.closeTime)
              ? `${hhmm(todayRec.openTime)}–${hhmm(todayRec.closeTime)}`
              : "휴무"
          : "정보 없음";
        const allHours = [...(p.operatingHours || [])]
          .sort((a,b) => DAY_ORDER.indexOf(a.dayType) - DAY_ORDER.indexOf(b.dayType));
        const isOpenNow = !!p.open;

        const opened = expandedId === p.id;

        return (
          <div
            key={p.id}
            className={classes.item}
            onClick={() => onSelect?.(p,"vet")}
            role="button"
            tabIndex={0}
          >
            <div className={classes.body}>
              <div className={classes.header}>
                <div className={classes.title} title={title}>{title}</div>
                <div className={classes.badges}>
                  <span className={`${classes.badge} ${p.category === "HOSPITAL" ? classes.badgeHospital : classes.badgePharmacy}`}>
                    {p.category === "HOSPITAL" ? <MdLocalHospital size={14}/> : <MdLocalPharmacy size={14}/>}
                    {cat}
                  </span>
                  {type === "dist" && p.distString && (
                    <span className={`${classes.badge} ${classes.badgeDist}`}>{p.distString}</span>
                  )}
                  <span className={`${classes.badge} ${isOpenNow ? classes.badgeOpen : classes.badgeClosed}`}>
                    {isOpenNow ? "영업중" : "영업종료"}
                  </span>
                </div>
              </div>

              <div className={classes.metaRow}>
                <span className={classes.meta}><MdSchedule /> {todayText}</span>
                {typeof p.parking === "boolean" && (
                  <span className={classes.meta}><MdLocalParking /> {p.parking ? "주차 가능" : "주차 불가"}</span>
                )}
                {p.phoneNumber && (
                  <span className={classes.meta}><MdPhone /> {p.phoneNumber}</span>
                )}
              </div>

              {p.address && (
                <div className={classes.addr} title={p.address}>
                  <FaLocationDot /> {p.address}
                </div>
              )}

              <button
                type="button"
                className={classes.toggle}
                onClick={(e) => { e.stopPropagation(); toggle(p.id); }}
                aria-expanded={opened}
              >
                {opened ? "영업시간 접기" : "영업시간 전체 보기"}
                <span className={`${classes.caret} ${opened ? classes.caretUp : ""}`} aria-hidden />
              </button>
              <AnimatePresence initial={false}>
                {opened && (
                  <motion.div
                    key="schedule"
                    className={classes.schedule}
                    initial={{ opacity: 0, height: 0, y: -4 }}
                    animate={{ opacity: 1, height: "auto", y: 0 }}
                    exit={{ opacity: 0, height: 0, y: -4 }}
                    transition={{ duration: 0.18 }}
                    onClick={(e)=>e.stopPropagation()}
                  >
                    {allHours.map((r, idx) => (
                      <motion.div
                        key={`${p.id}-${r.dayType}-${idx}`}
                        className={classes.scheduleRow}
                        initial={{ opacity: 0, x: -4 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ duration: 0.15, delay: idx * 0.02 }}
                      >
                        <span className={classes.day}>{DAY_LABEL[r.dayType] || r.dayType}</span>
                        <span className={classes.time}>
                          {r.open && r.openTime && r.closeTime ? `${hhmm(r.openTime)}–${hhmm(r.closeTime)}` : "휴무"}
                        </span>
                      </motion.div>
                    ))}
                  </motion.div>
                )}
              </AnimatePresence>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default VetContentList;
