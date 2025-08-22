import React, { useEffect, useMemo, useRef, useState } from "react";
import { motion } from "framer-motion";
import classes from "./TourDetailRepeat.module.css";
import TourDetailRepeatCommon from "./TourDetailRepeatCommon";
import { RiArrowLeftSLine, RiArrowRightSLine } from "react-icons/ri";

const variants = {
  container: { opacity: 0, y: 8 },
  animate: { opacity: 1, y: 0, transition: { duration: 0.35 } },
};

const toNum = (v) => {
  const n = Number(String(v ?? "").replace(/[^\d.-]/g, ""));
  return Number.isFinite(n) ? n : 0;
};
const formatPrice = (a, b) => {
  const x = toNum(a), y = toNum(b);
  if (!x && !y) return null;
  if (x && y) return `${x.toLocaleString()} ~ ${y.toLocaleString()}원`;
  return `${(x || y).toLocaleString()}원`;
};
const formatSize = (m2, pyeong) => {
  const m = toNum(m2), p = toNum(pyeong);
  if (!m && !p) return null;
  if (m && p) return `${m}㎡ / ${p}평`;
  return m ? `${m}㎡` : `${p}평`;
};
const getImages = (room) => [
  { url: room.roomImg1, alt: room.roomImg1Alt },
  { url: room.roomImg2, alt: room.roomImg2Alt },
  { url: room.roomImg3, alt: room.roomImg3Alt },
  { url: room.roomImg4, alt: room.roomImg4Alt },
  { url: room.roomImg5, alt: room.roomImg5Alt },
].filter((i) => i.url && String(i.url).trim() !== "");

const AMENITIES = [
  { key: "roomBathFacility", label: "욕실" },
  { key: "roomAirCondition", label: "에어컨" },
  { key: "roomTv", label: "TV" },
  { key: "roomRefrigerator", label: "냉장고" },
  { key: "roomToiletries", label: "어메니티" },
  { key: "roomCook", label: "취사" },
  { key: "roomHairDryer", label: "헤어드라이어" },
];

const AmenityChips = ({ room }) => {
  const items = AMENITIES.filter(({ key }) => room[key] === "Y");
  if (items.length === 0) return null;
  return (
    <div className={classes.chips}>
      {items.map(({ label }) => (
        <span key={label} className={classes.chip}>{label}</span>
      ))}
    </div>
  );
};

const wrap = (i, len) => (i + len) % len;

const RoomCard = ({ room }) => {
  const images = useMemo(() => getImages(room), [room]);

  const imagesKey = useMemo(
    () => images.map((im) => im.url || "").join("|"),
    [images]
  );
  const [mainIdx, setMainIdx] = useState(0);
  useEffect(() => setMainIdx(0), [imagesKey]);

  const hasImages = images.length > 0;
  const offSeason = formatPrice(room.roomOffSeasonMinFee1, room.roomOffSeasonMinFee2);
  const peakSeason = formatPrice(room.roomPeakSeasonMinFee1, room.roomPeakSeasonMinFee2);
  const sizeText = formatSize(room.roomSize1, room.roomSize2);
  const base = toNum(room.roomBaseCount);
  const max = toNum(room.roomMaxCount);

  return (
    <article className={`${classes.roomCard} ${!hasImages ? classes.imageNone : ""}`}>
      <div className={classes.cardStroke} />
      {hasImages ? (
        <div className={classes.gallery}>
          <div className={classes.mainImageWrap}>
            <img
              className={classes.mainImage}
              src={images[mainIdx].url}
              alt={images[mainIdx].alt || room.roomTitle}
              loading="lazy"
              draggable={false}
            />
            {images.length > 1 && (
              <>
                <button
                  type="button"
                  className={`${classes.imgNavBtn} ${classes.imgPrev}`}
                  onClick={() => setMainIdx((i) => wrap(i - 1, images.length))}
                  aria-label="이전 사진"
                >
                  <RiArrowLeftSLine />
                </button>
                <button
                  type="button"
                  className={`${classes.imgNavBtn} ${classes.imgNext}`}
                  onClick={() => setMainIdx((i) => wrap(i + 1, images.length))}
                  aria-label="다음 사진"
                >
                  <RiArrowRightSLine />
                </button>
              </>
            )}
          </div>
        </div>
      ) : (
        <div className={classes.noImage}>이미지가 없습니다</div>
      )}

      <header className={classes.header}>
        <h3 className={classes.roomTitle}>{room.roomTitle}</h3>
        {room.roomIntro && <p className={classes.intro}>{room.roomIntro}</p>}
      </header>

      <div className={classes.metaRow}>
        {sizeText && <span className={classes.badge}>면적 {sizeText}</span>}
        {(base || max) ? (
          <span className={classes.badge}>정원 {base}{max && base !== max ? `–${max}` : ""}명</span>
        ) : null}
      </div>

      <AmenityChips room={room} />

      <div className={classes.priceArea}>
        {offSeason && (
          <div className={classes.priceBlock}>
            <span className={classes.priceLabel}>비수기</span>
            <strong className={classes.priceValue}>{offSeason}</strong>
          </div>
        )}
        {peakSeason && (
          <div className={classes.priceBlock}>
            <span className={classes.priceLabel}>성수기</span>
            <strong className={classes.priceValue}>{peakSeason}</strong>
          </div>
        )}
      </div>
    </article>
  );
};

const RoomCarousel = ({ rooms }) => {
  const [idx, setIdx] = useState(0);
  const vpRef = useRef(null);
  const [vpW, setVpW] = useState(350);

  const roomsKey = useMemo(
    () => rooms.map((r) => String(r.roomInfoNo ?? r.roomTitle ?? "")).join("|"),
    [rooms]
  );

  useEffect(() => {
    const el = vpRef.current;
    const measure = () => setVpW(el?.offsetWidth || 350);
    measure();
    window.addEventListener("resize", measure);
    return () => window.removeEventListener("resize", measure);
  }, [roomsKey]);

  useEffect(() => setIdx(0), [roomsKey]);

  const count = rooms.length;
  const goTo = (i) => setIdx(i);

  if (count === 0) return null;

  return (
    <div className={classes.carousel} key={roomsKey}>
      <div className={classes.carouselViewport} ref={vpRef}>
        <motion.div
          className={classes.carouselTrack}
          animate={{ x: -idx * vpW }}
          transition={{ type: "spring", stiffness: 260, damping: 30 }}
          style={{ width: count * vpW }}
        >
          {rooms.map((room, i) => (
            <div
              key={(room.roomInfoNo ?? room.roomTitle ?? i) + ":" + i}
              className={classes.slide}
              style={{ width: vpW }}
            >
              <RoomCard room={room} />
            </div>
          ))}
        </motion.div>
      </div>

      {count > 1 && (
        <div className={classes.dots}>
          {rooms.map((_, i) => (
            <button
              key={i}
              type="button"
              className={`${classes.dot} ${i === idx ? classes.dotActive : ""}`}
              onClick={() => goTo(i)}
              aria-label={`${i + 1}번 객실`}
            />
          ))}
        </div>
      )}
    </div>
  );
};

const TourDetailRepeat = ({ tourDetail, typeId }) => {

  const rooms = useMemo(
    () => Array.isArray(tourDetail?.repeatRooms) ? [...tourDetail.repeatRooms] : [],
    [tourDetail?.repeatRooms]
  );
  const commons = tourDetail?.repeatCommons || [];

  const roomsKey = useMemo(
    () => rooms.map((r) => String(r.roomInfoNo ?? r.roomTitle ?? "")).join("|"),
    [rooms]
  );

  return (
    <>
        {String(typeId) === "32" && rooms.length > 0 && (
          <motion.div className={classes.wrapper} variants={variants} initial="container" animate="animate">
            <h2 className={classes.sectionTitle}>객실 안내</h2>
            <RoomCarousel key={roomsKey} rooms={rooms} />
          </motion.div>
        )}
      <TourDetailRepeatCommon items={commons} />
    </>
    
  );
};

export default TourDetailRepeat;
