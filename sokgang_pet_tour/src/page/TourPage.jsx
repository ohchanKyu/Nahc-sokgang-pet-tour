import React, { useState } from 'react';
import classes from './TourPage.module.css';
import Footer from '../components/LayoutComponents/Footer';
import KeywordForm from '../components/TourPageComponents/KeywordForm';
import FilterForm from '../components/TourPageComponents/FilterForm';
import KakaoMap from '../components/TourPageComponents/maps/KakaoMap';
import { motion, AnimatePresence } from 'framer-motion';
import SettingBar from '../components/TourPageComponents/SettingBar';
import TourContentList from '../components/TourPageComponents/TourContentList';
import { useGeoLocation } from "../hooks/useGeoLocation";
import TourPlaceDetailPage from './TourPlaceDetailPage';
import VetPlaceDetailPage from './VetPlaceDetailPage';
import VetKeywordForm from '../components/VetPageComponents/VetKeywordForm';
import VetContentList from '../components/VetPageComponents/VetContentList';
import { IoIosArrowBack } from "react-icons/io";


const geolocationOptions = {
    enableHighAccuracy: true,
    timeout: 1000 * 10,
    maximumAge: 1000 * 3600 * 24,
}

const TourPage = () => {
   
  const [mode, setMode] = useState("tour");
  const [subType, setSubType] = useState('count');
  const { location } = useGeoLocation(geolocationOptions);
  const [mapLocation, setMapLocation] = useState({});
  const [placeId,setPlaceId] = useState({});
  const [title,setTitle] = useState('');
  const [placeType,setPlaceType] = useState('');
  const [isVisible,setIsVisible] = useState(false);

  const panelVariants = { hidden:{opacity:0, y:6}, show:{opacity:1, y:0} };

  const handleVisible = () => {
    setIsVisible(false);
  };

  const handleMode = (mode) => {
     setMode(mode);
     setSubType('count');
  };

  const setDetailContents = async (content,placeType) => {
      
      const id = content.contentId || content.id;
      const title = content.title || content.placeName || content.name;
      setPlaceId(id);
      setPlaceType(placeType);
      setTitle(title);
      setMapLocation({ latitude : content.latitude, longitude : content.longitude });
      setIsVisible(true);
  };

  const handleSubTypeChange = (type) => {
    setSubType(type);
  };

  return (
    <>
      <main className={classes.mainContainer}>
        <div className={classes.sectionContainer}>
          <div className={classes.modeContainer}>
              <SettingBar
                mode={mode}
                onChangeMode={handleMode}
              />
          </div>
          <AnimatePresence mode="wait">
              {mode === "tour" && (
                <motion.div key="tour" variants={panelVariants} initial="hidden" animate="show" exit="hidden">
                  <KeywordForm onDetail={setDetailContents}/>
                  <div className={classes.subTypeSelector}>
                    <span className={`${classes.subType} ${subType === "count" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("count")}> 금일 조회순 </span>
                    <span className={`${classes.subType} ${subType === "dist" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("dist")}>  거리순  </span>
                    <span className={`${classes.subType} ${subType === "filter" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("filter")}>  분류 검색 </span>
                  </div>
                  {subType !== "filter" && <TourContentList type={subType} onSelect={setDetailContents} location={location}/>}
                  {subType === "filter" && <FilterForm onSelect={setDetailContents}/>}
                </motion.div>
              )}  
              {mode === "vet" && (
                <motion.div key="vet" variants={panelVariants} initial="hidden" animate="show" exit="hidden">
                  <VetKeywordForm onSelect={setDetailContents}/>
                  <div className={classes.subTypeSelector}>
                    <span
                      className={`${classes.subType} ${subType === "count" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("count")}
                    >
                      금일 조회순
                    </span>
                    <span
                      className={`${classes.subType} ${subType === "dist" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("dist")}
                    >
                      거리순
                    </span>
                    <span
                      className={`${classes.subType} ${subType === "openPhar" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("openPhar")}
                    >
                        영업중 약국
                    </span>
                    <span
                      className={`${classes.subType} ${subType === "openHos" ? classes.activeSubType : ""}`}
                      onClick={() => handleSubTypeChange("openHos")}
                    >
                        진료중 병원
                    </span>
                  </div>
                  {subType !== "filter" && <VetContentList type={subType} onSelect={setDetailContents} location={location}/>}
                </motion.div>
              )}
          </AnimatePresence>
        </div>
        <div className={classes.mapContainer}>
          <AnimatePresence mode="wait">
            {isVisible && (
              <motion.div key="detail" variants={panelVariants} initial="hidden" animate="show" exit="hidden" className={classes.modalContainer}>
                  <header className={classes.modalHeader}>
                    <div className={classes.exitIcon}>  
                        <IoIosArrowBack size={20} onClick={handleVisible}/>
                    </div>
                    <p className={classes.headerText}>{title}</p>
                  </header>
                  {placeId && placeType === "vet" && <VetPlaceDetailPage location={location} id={placeId}/>}
                  {placeId && placeType === "tour" && <TourPlaceDetailPage location={location} id={placeId}/>}
              </motion.div>
            )}
          </AnimatePresence>
          <KakaoMap location={mapLocation} onSelect={setDetailContents} id={placeId}/>
        </div>
      </main>
      <Footer/>
    </>
  );
};

export default TourPage;
