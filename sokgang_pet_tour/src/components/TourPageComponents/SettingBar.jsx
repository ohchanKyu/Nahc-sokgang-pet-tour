import React from "react";
import { RiMapPinLine } from "react-icons/ri";
import { MdPets } from "react-icons/md";
import classes from "./SettingBar.module.css";
import { motion } from "framer-motion";
import { FaHome } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';

const SettingBar = ({ mode, onChangeMode }) => {

  const navigate = useNavigate();

  const goHome = () => {
    navigate('/');
  }

  return (
    <aside className={classes.sidebar} aria-label="설정 사이드바">
      <div className={classes.group}>
        <p className={classes.title}>Contents</p>
        <div className={classes.buttonContainer}>
          <motion.button
            whileHover={{ scale : 1.05 }}
            type="button"
            className={`${classes.modeBtn} ${mode === "tour" ? classes.active : ""}`}
            onClick={() => onChangeMode("tour")}
          >
            <RiMapPinLine/> 여행 장소
          </motion.button>
          <motion.button
            whileHover={{ scale : 1.05 }}
            type="button"
            className={`${classes.modeBtn} ${mode === "vet" ? classes.active : ""}`}
            onClick={() => onChangeMode("vet")}
          >
            <MdPets/> 반려 케어
          </motion.button>
          <motion.button
            whileHover={{ scale : 1.05 }}
            type="button"
            className={`${classes.modeBtn} ${mode === "home" ? classes.active : ""}`}
            onClick={() => goHome()}
          >
            <FaHome/> 메인 화면
          </motion.button>
        </div>
      </div>
    </aside>
  );
};

export default SettingBar;
