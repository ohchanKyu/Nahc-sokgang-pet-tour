import React, { useState } from "react";
import classes from "./TourContentList.module.css";

const Thumb = ({ primary, backup, title }) => {
    
  const [src, setSrc] = useState(primary || backup || "");
  const [failed, setFailed] = useState(!primary && !backup);

  const handleError = () => {
    if (backup && src !== backup) setSrc(backup);
    else setFailed(true); 
  };

  return (
    <div className={classes.thumb}>
      {failed ? (
        <div className={classes.fallback}>
          <span className={classes.fallbackTitle}>{title}</span>
        </div>
      ) : (
        <img
          src={src}
          alt={title}
          loading="lazy"
          decoding="async"
          onError={handleError}
          referrerPolicy="no-referrer"
        />
      )}
    </div>
  );
};

export default Thumb;
