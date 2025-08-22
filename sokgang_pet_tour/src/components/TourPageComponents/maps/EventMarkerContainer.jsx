import React, { useState } from "react";
import { CustomOverlayMap } from "react-kakao-maps-sdk";
import classes from "./EventMarkerContainer.module.css";
import { motion, AnimatePresence } from "framer-motion";

import {
  MdLocalHospital,
  MdLocalPharmacy,
  MdRestaurant,
  MdHotel,
  MdMuseum,
  MdShoppingBag,
  MdSportsSoccer,
  MdPlace,
} from "react-icons/md";

const CATEGORY_ICON = {
  "동물병원": MdLocalHospital,
  "동물약국": MdLocalPharmacy,
  "음식점": MdRestaurant,
  "쇼핑": MdShoppingBag,
  "숙박": MdHotel,
  "레포츠": MdSportsSoccer,
  "행사/공연/축제": MdMuseum,
  "문화시설": MdMuseum,
  "관광지": MdPlace,
};

const PIN_COLOR = "#E47C5F";

const CATEGORY_ACCENT = {
  "동물병원": "#2563eb",
  "동물약국": "#10b981",
  "음식점":   "#ef4444",
  "쇼핑":     "#a855f7",
  "숙박":     "#0ea5e9",
  "레포츠":   "#f59e0b",
  "행사/공연/축제": "#f97316",
  "문화시설": "#7c3aed",
  "관광지":   "#1d4ed8",
};

const EventMarkerContainer = ({ position, placeData, flag = false, onSelect }) => {

  const [isVisible, setIsVisible] = useState(flag);
  const catLabel = placeData.cat || placeData.category || "장소";
  const accent = CATEGORY_ACCENT[catLabel] || "#111827";
  const Icon = CATEGORY_ICON[catLabel] || MdPlace;

  const toggle = () => setIsVisible(v => !v);
  const goDetail = () => {
    if (placeData.cat === "동물병원" || placeData.cat === "동물약국"){
      onSelect(placeData,"vet");
    }else onSelect(placeData,"tour");
  }
  const PIN_Z   = 1000;
  const CARD_Z  = 2000; 
  
  return (
    <>
      <CustomOverlayMap zIndex={PIN_Z} position={position} clickable>
        <motion.button
            type="button"
            aria-label={`${catLabel} 위치`}
            className={classes.pinBtn}
            style={{ "--pin": PIN_COLOR }}
            onClick={toggle}
            initial={{ opacity: 0, scale: 0.65, y: -8 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.65, y: 8 }}
            transition={{ duration: 0.2 }}
        >
            <svg
            className={classes.pinSvg}
            viewBox="0 0 48 56"
            aria-hidden
            focusable="false"
            >
            <path
                d="M24 2c-10 0-18 7.6-18 17 0 8.6 6.6 15.4 15.3 25.3a2.8 2.8 0 0 0 4.4 0C35.4 34.6 42 27.8 42 19 42 9.6 34 2 24 2z"
                fill="var(--pin)"
                stroke="#fff"
                strokeWidth="3"
            />
            <ellipse cx="24" cy="12.5" rx="9.5" ry="4.5" fill="#fff" opacity=".08"/>
            </svg>
            <span className={classes.pinIcon}>
            <Icon size={20} />
            </span>
        </motion.button>
        <CustomOverlayMap zIndex={CARD_Z} position={position}>
          <AnimatePresence initial={false} >
            {isVisible && (
              <motion.div
                className={classes.overlayWrap}
                initial={{ opacity: 0, y: 6, scale: 0.98 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                exit={{ opacity: 0, y: 6, scale: 0.98 }}
                transition={{ duration: 0.18 }}
              >
                <div
                  className={classes.customoverlay}
                  style={{ "--accent": accent }}
                  onClick={(e) => e.stopPropagation()}
                >
                  <div className={classes.badge}>{catLabel}</div>

                  <div className={classes.name} title={placeData.name}>
                    {placeData.name}
                  </div>

                  {placeData.address && (
                    <div className={classes.addr} title={placeData.address}>
                      {placeData.address}
                    </div>
                  )}

                  <button
                    type="button"
                    className={classes.chevronBtn}
                    aria-label="상세 보기"
                    onClick={(e) => { e.stopPropagation(); goDetail(); }}
                  />

                  <div className={classes.pointer} aria-hidden />
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </CustomOverlayMap>
      </CustomOverlayMap>
    </>
  );
};

export default EventMarkerContainer;
