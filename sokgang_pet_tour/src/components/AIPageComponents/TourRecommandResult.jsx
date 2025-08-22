import classes from "./TourRecommandResult.module.css";
import { useState } from "react";
import { decorateTour, ConvertContentType } from "../TourPageComponents/ConvertEntity";
import TourPlaceDetailPage from "../../page/TourPlaceDetailPage";
import { motion, AnimatePresence } from "framer-motion";
import { IoIosArrowBack } from "react-icons/io";

const formatDistance = (km) => {
  if (km == null || Number.isNaN(Number(km))) return "-";
  const v = Number(km);
  return v >= 1 ? `${v.toFixed(1)}km` : `${Math.round(v * 1000)}m`;
};

const formatMinutes = (m) => {
  if (m == null || Number.isNaN(Number(m))) return "-";
  const h = Math.floor(m / 60);
  const mm = Math.round(m % 60);
  return h ? `${h}시간 ${mm}분` : `${mm}분`;
};

const TourRecommandResult = ({ days, result }) => {

    if (!result?.days?.length) {
        return null;
    }

    const [placeId,setPlaceId] = useState({});
    const [title,setTitle] = useState('');
    const [isVisible,setIsVisible] = useState(false);
    const panelVariants = { hidden:{opacity:0, y:6}, show:{opacity:1, y:0} };

    const handleVisible = () => {
        setIsVisible(false);
    };


    const setDetailContents = async (id,title) => {
        
        setPlaceId(id);
        setTitle(title);
        setIsVisible(true);
    };

  return (
    <>
        <AnimatePresence mode="wait">
            {isVisible && (
                <motion.div key="detail" variants={panelVariants} initial="hidden" animate="show" exit="hidden" className={classes.modalContainer}>
                    <header className={classes.modalHeader}>
                        <div className={classes.exitIcon}>  
                            <IoIosArrowBack size={20} onClick={handleVisible}/>
                        </div>
                        <p className={classes.headerText}>{title}</p>
                    </header>
                    {placeId && <TourPlaceDetailPage isIncludeRoute={false} id={placeId}/>}
                </motion.div>
            )}
        </AnimatePresence>
    <div className={classes.dayGrid}>
      <h2 className={classes.dayTitle}>{days}일동안의 <span className={classes.dayTitleHighlight}>추천 일정</span>입니다.</h2>
      {result.days.map((day) => {
        const stops = day?.stops || [];
        const rows = [];
        stops.forEach((raw, idx) => {
          const s = raw;
          const content = decorateTour(raw.tourContentResponse);
          rows.push(
            <li
              
              key={`stop-${day.dayIndex}-${idx}`}
              className={classes.stopRow}
              aria-label={`Day ${day.dayIndex} - ${idx + 1}번째 장소`}
            >
              <div className={classes.timelineCol}>
                <div className={classes.node}>{idx + 1}</div>
              </div>
              <motion.article 
                whileHover={{ scale : 1.02 }}
                className={classes.stopCard} onClick={() => setDetailContents(content.contentId,content.title)}>
                <div className={classes.stopBody}>
                  <div className={classes.stopHeader}>
                    <h4 className={classes.stopTitle}>
                      {content.title || "-"}
                    </h4>
                    <div className={classes.chips}>
                      <span className={`${classes.chip} ${classes.chipBlue}`}>
                        {content.typeName ||
                          ConvertContentType(Number(s.contentTypeId)) ||
                          "유형"}
                      </span>
                    </div>
                  </div>

                  <p className={classes.addr}>
                    {content.address || "-"}{" "}
                    {content.region ? `· ${content.region}` : ""}
                  </p>

                  <div className={classes.metrics}>
                    <div className={classes.metricBox}>
                      <div className={classes.metricLabel}>체류</div>
                      <div className={classes.metricValue}>
                        {formatMinutes(s.stayMin)}
                      </div>
                    </div>
                    <div className={classes.metricBox}>
                      <div className={classes.metricLabel}>도착 · 출발</div>
                      <div className={classes.metricValue}>
                        {(s.arrival || "-")} → {(s.depart || "-")}
                      </div>
                    </div>
                    <div className={classes.metricBox}>
                      <div className={classes.metricLabel}>관광 종류</div>
                      <div className={classes.metricValue}>
                        {content.cat3Name}
                      </div>
                    </div>
                  </div>
                </div>
              </motion.article>
            </li>
          );
          const next = stops[idx + 1];
          if (next) {
            rows.push(
              <li
                key={`travel-${day.dayIndex}-${idx}`}
                className={classes.travelRow}
                aria-label={`다음 장소로 이동`}
              >
                 <div className={classes.timelineCol}>
                    <div className={classes.connector}>
                        <span className={classes.travelBadge}>
                            {formatDistance(next.distFromPrevKm)} · {formatMinutes(next.travelMin)}
                        </span>
                    </div>
                </div>
                <div className={classes.travelSpacer} />
              </li>
            );
          }
        });

        return (
          <section key={day.dayIndex} className={classes.dayCard}>
            <header className={classes.dayHeader}>
              <h3>Day {day.dayIndex}</h3>
              <div className={classes.dayMeta}>
                <span>이동 {formatMinutes(day.totalTravelMin)}</span>
                <span>·</span>
                <span>체류 {formatMinutes(day.totalStayMin)}</span>
              </div>
            </header>

            <ol className={classes.stopList}>
              {rows}
            </ol>
          </section>
        );
      })}
    </div>
    </>
  );
};

export default TourRecommandResult;
