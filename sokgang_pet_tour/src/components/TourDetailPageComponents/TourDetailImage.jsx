import React, { useEffect, useMemo, useRef, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import classes from "./TourDetailImage.module.css";
import { RiArrowLeftSLine, RiArrowRightSLine } from "react-icons/ri";

const valid = (v) => v && String(v).trim().length > 0;

function ImageWithFallback({ src, alt, fallbackTitle, className, imgClassName }) {
  const [failed, setFailed] = useState(!valid(src));
  useEffect(() => setFailed(!valid(src)), [src]);

  if (failed) {
    return (
      <div className={`${className} ${classes.fallback}`}>
        <span className={classes.fallbackTitle}>{fallbackTitle || alt || "이미지"}</span>
      </div>
    );
  }
  return (
    <img
      src={src}
      alt={alt}
      className={imgClassName}
      loading="lazy"
      decoding="async"
      onError={() => setFailed(true)}
      referrerPolicy="no-referrer"
    />
  );
}

const TourDetailImage = ({ tourDetail, title }) => {
  
  const images = useMemo(
    () => (Array.isArray(tourDetail) ? tourDetail.filter(i => valid(i?.originImgUrl) || valid(i?.smallImgUrl)) : []),
    [tourDetail]
  );
  const [idx, setIdx] = useState(0);
  const wrap = (i) => (i + images.length) % images.length;

  const thumbsWrapRef = useRef(null);
  const thumbBtnRefs = useRef([]);

  const ensureThumbVisible = (i) => {
    const wrapEl = thumbsWrapRef.current;
    const btn = thumbBtnRefs.current[i];
    if (!wrapEl || !btn) return;
    const { left: wl, right: wr } = wrapEl.getBoundingClientRect();
    const { left: bl, right: br } = btn.getBoundingClientRect();
    if (bl < wl) wrapEl.scrollBy({ left: bl - wl - 12, behavior: "smooth" });
    else if (br > wr) wrapEl.scrollBy({ left: br - wr + 12, behavior: "smooth" });
  };

  const go = (d) => setIdx((i) => {
    const ni = wrap(i + d);
    ensureThumbVisible(ni);
    return ni;
  });
  const goTo = (i) => { setIdx(wrap(i)); ensureThumbVisible(i); };

  useEffect(() => {
    const onKey = (e) => {
      if (e.key === "ArrowLeft") go(-1);
      if (e.key === "ArrowRight") go(1);
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [images.length]);

  if (!images.length) {
    // 전체 이미지가 없을 때: 타이틀 폴백 카드
    return (
      <section className={classes.section}>
        <div className={classes.gallery}>
          <div className={`${classes.main} ${classes.fallback}`}>
            <span className={classes.fallbackTitle}>{title}</span>
          </div>
        </div>
      </section>
    );
  }

  const main = images[idx];
  const mainSrc = main?.originImgUrl || main?.smallImgUrl;
  const mainTitle = main?.imgName || title || `사진 ${idx + 1}`;

  return (
    <section className={classes.section}>
      <div className={classes.gallery}>
        <div className={classes.main}>
          <button className={`${classes.navBtn} ${classes.left}`} onClick={() => go(-1)} aria-label="이전">
            <RiArrowLeftSLine />
          </button>

          <AnimatePresence mode="wait">
            <motion.div
              key={mainSrc || mainTitle}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              transition={{ duration: 0.22 }}
              className={classes.mainInner}
            >
              <ImageWithFallback
                src={mainSrc}
                alt={mainTitle}
                fallbackTitle={main?.imgName || title}
                className={classes.mainFallback}
                imgClassName={classes.mainImg}
              />
            </motion.div>
          </AnimatePresence>

          <button className={`${classes.navBtn} ${classes.right}`} onClick={() => go(1)} aria-label="다음">
            <RiArrowRightSLine />
          </button>
        </div>
        <div className={classes.captionRow}>
          <div className={classes.dots}>
            {images.map((_, i) => (
              <button
                key={i}
                className={`${classes.dot} ${i === idx ? classes.dotActive : ""}`}
                onClick={() => goTo(i)}
                aria-label={`${i + 1}번 이미지로 이동`}
                title={`${i + 1}/${images.length}`}
              />
            ))}
          </div>
        </div>
        <div className={classes.thumbRow} ref={thumbsWrapRef} role="listbox" aria-label="썸네일 목록">
          {images.map((img, i) => {
            const tSrc = img.smallImgUrl || img.originImgUrl;
            const tTitle = img.imgName || `썸네일 ${i + 1}`;
            return (
              <button
                key={tSrc || tTitle || i}
                ref={(el) => (thumbBtnRefs.current[i] = el)}
                onClick={() => goTo(i)}
                className={`${classes.thumbBtn} ${i === idx ? classes.thumbActive : ""}`}
                aria-label={`이미지 ${i + 1} 선택`}
                title={tTitle}
              >
                <ImageWithFallback
                  src={tSrc}
                  alt={tTitle}
                  fallbackTitle={img.imgName}
                  className={classes.thumbFallback}
                  imgClassName={classes.thumbImg}
                />
              </button>
            );
          })}
        </div>
      </div>
    </section>
  );
};

export default TourDetailImage;
