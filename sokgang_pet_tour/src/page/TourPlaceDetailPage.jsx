import { useState, useEffect, useMemo } from "react";
import { getTourPlaceDetailService } from "../api/TourService";
import { decorateTour } from "../components/TourPageComponents/ConvertEntity";
import { toast } from "react-toastify";
import classes from "./TourPlaceDetailPage.module.css";
import TourDetailCommon from "../components/TourDetailPageComponents/TourDetailCommon";
import TourDetailPet from "../components/TourDetailPageComponents/TourDetailPet";
import TourDetailRepeat from "../components/TourDetailPageComponents/TourDetailRepeat";
import TourDetailIntro from "../components/TourDetailPageComponents/TourDetailIntro";
import DetailTabBar from "../components/TourDetailPageComponents/DetailTabBar";
import TourDetailImage from "../components/TourDetailPageComponents/TourDetailImage";
import DetailThumb from "../components/TourDetailPageComponents/DetailThumb";
import { motion } from "framer-motion";
import {
  MdContentCopy,
  MdDirections
} from "react-icons/md";
import Loading from "../components/LayoutComponents/Loading";

const has = (v) => {
  if (v === null || v === undefined) return false;
  if (typeof v === "string") return v.trim().length > 0;
  if (Array.isArray(v)) return v.length > 0;
  if (typeof v === "object") return Object.keys(v).length > 0;
  return true;
};

const naverMapURL = 'http://map.naver.com/index.nhn?';

const TourPlaceDetailPage = ({ id, location, isIncludeRoute = true }) => {

  const [tourDetail, setTourDetail] = useState(null);
  const [active, setActive] = useState("home"); 
  const [loading, setLoading] = useState(false);

  const copyAddress = async () => {
    try {
      await navigator.clipboard.writeText(tourDetail.tourContentResponse.address || "");
      toast.success("주소를 복사하였습니다.", { position: "top-center", autoClose: 500 });
    } catch {
      toast.error("주소 복사에 실패하였습니다.\n다시 시도해주세요.", { position: "top-center", autoClose: 800 });
    }
  };

  const openNaverDirections = () => {
        
        if (!tourDetail) return;
        
        const startName = "현재 위치";
        const startLat = location.latitude;
        const startLng = location.longitude;

        const destName = tourDetail.tourContentResponse.title || "목적지";
        const destLat = tourDetail.tourContentResponse.latitude;
        const destLng = tourDetail.tourContentResponse.longitude
        let url = `${naverMapURL}slng=${startLng}&slat=${startLat}&stext=${startName}&elng=${destLng}&elat=${destLat}&pathType=0&showMap=true&etext=${destName}&menu=route`;
        window.open(url, "_blank", "noopener,noreferrer");
  };

  useEffect(() => {
    
    const fetchTourDetail = async () => {
      setLoading(true);
      setTourDetail(null);
      const placeResponse = await getTourPlaceDetailService(id);
      if (placeResponse.success){
        const decorated = decorateTour(placeResponse.data.tourContentResponse);
        const imagesInfo = placeResponse.data.imagesInfo || [];
        imagesInfo.unshift({
          imgName : "대표 이미지",
          originImgUrl: decorated.originalImageUrl,
          smallImgUrl: decorated.thumbnailImageUrl,
        });
        decorated.imagesInfo = imagesInfo;
        setTimeout(() => {
          setLoading(false);  
          setTourDetail({ ...placeResponse.data, tourContentResponse: decorated });
        },500);
      } else {
        const errorMessage = placeResponse?.message ?? "알 수 없는 오류";
        toast.error(`일시적 오류입니다.\n${errorMessage}`, { position: "top-center", autoClose: 2000 });
        setLoading(false); 
      }
      setActive('home'); 
    };
    if (id) fetchTourDetail();
  }, [id]);

  const sections = useMemo(() => {
    if (!tourDetail) return [];
    const typeId = tourDetail.tourContentResponse.contentTypeId;

    const base = [
      { id: "home",  label: "홈" },
      { id: "image", label: "사진" },
      { id: "info",  label: "정보" },
    ];
    const extra = [];
    return [...base, ...extra];
  }, [tourDetail]);

  return (
    <div className={classes.wrapper}>
      {loading && (
          <div className={`${classes.item} ${classes.skel}`}>
            <div className={classes.thumb} />
            <div className={classes.body}>
              <div className={classes.line} />
              <div className={classes.lineShort} />
              <div className={classes.line} />
            </div>
          </div>
      )}
      {tourDetail && (
        <header className={classes.header}>
          <div className={classes.badges}>
            {has(tourDetail.tourContentResponse.typeName) && <span className={classes.badgeBlue}>{tourDetail.tourContentResponse.typeName}</span>}
            {has(tourDetail.tourContentResponse.cat3Name) && <span className={classes.badgeGray}>{tourDetail.tourContentResponse.cat3Name}</span>}
            {has(tourDetail.tourContentResponse.region) && <span className={classes.badgeGreen}>{tourDetail.tourContentResponse.region}</span>}
          </div>
          <h1 className={classes.title}>{tourDetail.tourContentResponse.title}</h1>
          <p className={classes.subtitle}>{tourDetail.tourContentResponse.cat1Name} • {tourDetail.tourContentResponse.cat2Name} • {tourDetail.tourContentResponse.cat3Name}</p>
          {isIncludeRoute && (
            <div className={classes.actionRow}>
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
          )}
          <DetailThumb
            key={tourDetail.tourContentResponse.contentId}
            primary={tourDetail.imagesInfo[0]?.originImgUrl}
            backup={tourDetail.imagesInfo[1]?.originImgUrl}
            title={tourDetail.tourContentResponse.title}
          />
        </header>
      )}
      {tourDetail && <DetailTabBar key={`Detailbar-${tourDetail.tourContentResponse.contentId}`} sections={sections} active={active} onChange={setActive} />}
      {tourDetail && (
        <div className={classes.panelArea}>
          {active === "home"   && (
            <section className={classes.section}>
                <TourDetailCommon key={`common-${tourDetail.tourContentResponse.contentId}`} tourDetail={tourDetail.tourContentResponse} />
            </section>
          )}
          {active === "image"  && (
            <section className={classes.section}>
              <TourDetailImage
                key={`image-${tourDetail.tourContentResponse.contentId}`}
                tourDetail={tourDetail.imagesInfo}
                title={tourDetail.tourContentResponse.title}
              />
            </section>
          )}

          {active === "info"   && (
            <section className={classes.section}>
              <TourDetailPet   key={`pet-${tourDetail.tourContentResponse.contentId}`}   tourDetail={tourDetail} />
              <TourDetailRepeat key={`repeat-${tourDetail.tourContentResponse.contentId}`} tourDetail={tourDetail} typeId={tourDetail.tourContentResponse.contentTypeId} />
              <TourDetailIntro key={`intro-${tourDetail.tourContentResponse.contentId}`}  tourDetail={tourDetail} typeId={tourDetail.tourContentResponse.contentTypeId} />
            </section>
          )}
          </div>
      )}
    </div>
  );
};

export default TourPlaceDetailPage;
