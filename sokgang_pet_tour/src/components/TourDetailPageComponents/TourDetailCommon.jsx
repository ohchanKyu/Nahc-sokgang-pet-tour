import { memo, useMemo } from "react";
import { motion } from "framer-motion";
import classes from "./TourDetailCommon.module.css";
import { LuMapPin, LuPhone, LuMailbox, LuTag, LuLayers } from "react-icons/lu";

const has = (v) => {
  if (v === null || v === undefined) return false;
  if (typeof v === "string") return v.trim().length > 0;
  if (Array.isArray(v)) return v.length > 0;
  if (typeof v === "object") return Object.keys(v).length > 0;
  return true;
};

function cleanOverview(raw) {
  let s = String(raw || "");

  s = s.replace(/^\s*"+|"+\s*$/g, "");


  s = s.replace(/(\r\n|\n|\r|\\n|\\r)/g, " ");

  s = s.replace(/^\s*NAVER\s*$/gim, "");
  s = s.replace(/^\s*©\s*NAVER\s*Corp\.?\s*$/gim, "");

  return s.trim();
}


const variants = {
  container: { opacity: 0, y: 8 },
  animate: { opacity: 1, y: 0, transition: { duration: 0.35 } },
  list: { transition: { staggerChildren: 0.06 } },
  item: { opacity: 0, y: 6, transition: { duration: 0.25 } },
  itemShow: { opacity: 1, y: 0, transition: { duration: 0.25 } },
};

function TourDetailCommon({ tourDetail }) {
  if (!tourDetail) return null;

  const {
    title,
    typeName,
    cat1Name, cat2Name, cat3Name,
    region,
    address,
    detailAddress,
    postCode,
    telephone,
    overview,
    latitude,
    longitude,
  } = tourDetail;

  const fullAddress = [address, detailAddress].filter(has).join(" ");
  const cleanedOverview = useMemo(() => cleanOverview(overview), [overview]);

  const infoRows = useMemo(() => {
    const rows = [];

    if (has(fullAddress)) {
      const mapHref = has(latitude) && has(longitude)
        ? `https://map.naver.com/v5/search/${encodeURIComponent(fullAddress)}/place/${encodeURIComponent(
            title || ""
          )}?c=15,0,0,0,dh&isCorrectAnswer=true&lng=${longitude}&lat=${latitude}`
        : `https://map.naver.com/v5/search/${encodeURIComponent(fullAddress)}`;
      rows.push({
        id: "addr",
        icon: <LuMapPin />,
        label: "주소",
        value: fullAddress,
        href: mapHref,
      });
    }

    if (has(telephone)) {
      rows.push({
        id: "tel",
        icon: <LuPhone />,
        label: "문의",
        value: telephone,
        href: `tel:${telephone.replace(/-/g, "")}`,
      });
    }

    if (has(postCode)) {
      rows.push({ id: "post", icon: <LuMailbox />, label: "우편번호", value: postCode });
    }

    if (has(typeName)) {
      rows.push({ id: "type", icon: <LuLayers />, label: "관광 종류", value: typeName });
    }

    const cat = [cat1Name, cat2Name, cat3Name].filter(has).join(" • ");
    if (has(cat)) {
      rows.push({ id: "cat", icon: <LuTag />, label: "분류", value: cat });
    }

    if (has(region)) {
      rows.push({ id: "region", icon: <LuTag />, label: "지역", value: region });
    }

    return rows;
  }, [fullAddress, latitude, longitude, title, telephone, postCode, typeName, cat1Name, cat2Name, cat3Name, region]);
  const html = (s) => ({ __html: String(s || "") });

  return (
    <motion.div
      className={`${classes.theme} ${classes.container}`}
      variants={variants}
      initial="container"
      animate="animate"
    >
      {infoRows.length > 0 && (
        <section className={classes.section}>
          <h2 className={classes.sectionTitle}>기본 정보</h2>

          <motion.div className={classes.infoCard} variants={variants.list} initial={false} animate="animate">
            {infoRows.map((row) => (
              <motion.div key={row.id} className={classes.row} variants={{ hidden: variants.item, show: variants.itemShow }} initial="hidden" animate="show">
                <div className={classes.left}>
                  <span className={classes.icon}>{row.icon}</span>
                  <span className={classes.label}>{row.label}</span>
                </div>
                <div className={classes.right}>
                  <span className={classes.value}>{row.value}</span>
                </div>
              </motion.div>
            ))}
          </motion.div>
        </section>
      )}
      {has(cleanedOverview) && (
        <section className={classes.section}>
          <h2 className={classes.sectionTitle}>소개 정보</h2>
          <p className={classes.overview} dangerouslySetInnerHTML={html(cleanedOverview)}></p>
        </section>
      )}
    </motion.div>
  );
}

export default memo(TourDetailCommon);
