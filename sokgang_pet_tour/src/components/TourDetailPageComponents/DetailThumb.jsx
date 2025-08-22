import { useState, useEffect } from "react";
import classes from "./DetailThumb.module.css";

const DetailThumb = ({ primary, backup, title }) => {
  const [src, setSrc] = useState(primary || backup || "");
  const [failed, setFailed] = useState(!primary && !backup);

  useEffect(() => {
    setSrc(primary || backup || "");
    setFailed(!primary && !backup);
  }, [primary, backup]);

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

export default DetailThumb;
