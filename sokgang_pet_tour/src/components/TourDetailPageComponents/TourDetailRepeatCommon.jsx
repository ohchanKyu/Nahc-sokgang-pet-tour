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

const TourDetailRepeatCommon = ({ items }) => {
    
  return (
     <motion.div 
        variants={variants}
        initial="container"
        animate="animate"
        className={classes.wrapper}>
            <SectionCard title="이용 안내">
                {items.length === 0 && (
                <div key='이용 안내 추가 정보가 없습니다.' className={classes.card}>
                    <div className={classes.row}>
                    <div className={classes.label}>이용 안내 추가 정보가 없습니다.</div>
                    </div>
                </div>
                )}
                {items.map((it, i) => (
                <div key={`${it.infoName}-${i}`} className={classes.card}>
                    <Row label={it.infoName} value={it.infoText} />
                </div>
                ))}
            </SectionCard>
    </motion.div>
  );
};

export default TourDetailRepeatCommon;