import React from "react";
import classes from "./TourDetailPet.module.css";
import { motion } from "framer-motion";

const html = (s) => ({ __html: String(s || "") });

const variants = {
  container: { opacity: 0, y: 8 },
  animate: { opacity: 1, y: 0, transition: { duration: 0.35 } },
  list: { transition: { staggerChildren: 0.06 } },
  item: { opacity: 0, y: 6, transition: { duration: 0.25 } },
  itemShow: { opacity: 1, y: 0, transition: { duration: 0.25 } },
};

const Row = ({ label, value }) => {
  if (!isFilled(value)) return null;
  return (
    <div className={classes.row}>
      <div className={classes.label}>{label}</div>
      <div className={classes.value} dangerouslySetInnerHTML={html(value)} />
    </div>
  );
};

const isFilled = (v) => {
  if (v === null || v === undefined) return false;
  const s = String(v).trim();
  if (s === "" || s === "0") return false;
  return true;
};


const SectionCard = ({ title, children }) => {
  return (
    <section className={classes.card}>
      <div className={classes.cardBar} />
      <h3 className={classes.cardTitle}>{title}</h3>
      <div className={classes.body}>{children}</div>
    </section>
  );
};

const TourDetailPet = ({ tourDetail }) => {

  const { petsInfo } = tourDetail || {};

  return (
    <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
      <SectionCard title="반려동물 동반 안내">
          {petsInfo.length === 0 && (
            <div key='반려동물 동반 안내 추가 정보가 없습니다.' className={classes.card}>
              <div className={classes.row}>
                <div className={classes.label}>반려동물 동반 안내 추가 정보가 없습니다.</div>
              </div>
            </div>
          )}
          {petsInfo.map((pet, index) => (
            <div key={`tourDetailPet ${index}`} className={classes.card}>
              {Object.entries({
                "동반 구역": pet.acmpyTypeCd,
                "동반 가능 견종": pet.acmpyPsblCpam,
                "필수 준비물": pet.acmpyNeedMtr,
                "관련 사고 대비사항": pet.relaAcdntRiskMtr,
                "관련 구비 시설": pet.relaPosesFclty,
                "관련 비치 품목": pet.relaFrnshPrdlst,
                "기타 동반 정보": pet.etcAcmpyInfo,
                "대여 가능 품목": pet.relaRntlPrdlst,
              }).map(([label, value], i) =>
                value && value.trim() !== "" ? (
                  <Row key={label} label={label} value={value} />
                ) : null
              )}
            </div>
          ))}
      </SectionCard>
    </motion.div>
  );
};

export default TourDetailPet;
