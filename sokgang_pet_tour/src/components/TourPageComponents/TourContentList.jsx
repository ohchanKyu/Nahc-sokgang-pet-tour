import React, { useEffect, useState } from "react";
import classes from "./TourContentList.module.css";
import { getTourPlacesByTodayCount, getTourPlacesByDistService } from "../../api/TourService";
import { decorateTour } from "./ConvertEntity";
import Thumb from "./Thumb";
import { FaLocationDot } from "react-icons/fa6";

const TourContentList = ({ type = "count", onSelect, location, filterPlaces }) => {

  const [places, setPlaces] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let mounted = true;
    const fetchData = async () => {
        setLoading(true);
        let res;
        if (type === "count") {
            res = await getTourPlacesByTodayCount();
            if (mounted && res.success) {
            setPlaces(decorateTour(res.data.slice(0,15)) || []);
        }
        } else if (type === "dist") {
            res = await getTourPlacesByDistService(location.latitude, location.longitude);
            if (mounted && res.success) {
                const mapped = (res.data.slice(0,15) || []).map((item) => {
                const base = decorateTour([item.tourContentResponse])[0];
                return {
                    ...base,
                    distance: item.distance,
                    distanceStringFormat: item.distanceStringFormat,
                };
                });
                setPlaces(mapped);
            }
        }else{
           setPlaces(decorateTour(filterPlaces) || []);
        }
        setTimeout(() => {
          setLoading(false);
        },500);
        
    };
    fetchData();
    return () => { mounted = false; };
  }, [type]);

  if (loading) {
    return (
      <div className={classes.list}>
        {Array.from({ length: 6 }).map((_, i) => (
          <div key={i} className={`${classes.item} ${classes.skel}`}>
            <div className={classes.thumb} />
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

  if (!places.length) {
    return <div className={classes.empty}>결과가 없습니다.</div>;
  }

  return (
    <div className={classes.list}>
      {places.map((p) => {
        const primary = p.thumbnailImageUrl || "";
        const backup  = p.originalImageUrl || "";
        return (
          <div
            key={p.contentId}
            className={classes.item}
            onClick={() => onSelect?.(p,"tour")}
            role="button"
            tabIndex={0}
          >
            <Thumb primary={primary} backup={backup} title={p.title} />
            <div className={classes.body}>
              <div className={classes.header}>
                <div className={classes.title} title={p.title}>{p.title}</div>
                <div className={classes.type}>
                    {p.typeName}
                    {type === "dist" && (
                      <div className={classes.distance}>{p.distanceStringFormat}</div>
                    )}
                </div>
              </div>

              {p.overview && (
                <div className={classes.desc} dangerouslySetInnerHTML={{__html: p.overview}}></div>
              )}

             {p.address && (
                <div className={classes.addr} title={p.address}>
                  <FaLocationDot className={classes.icon} size={12} />
                  <span className={classes.addrText}>{p.address}</span>
                </div>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default TourContentList;
