import React, { useContext } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { toast } from "react-toastify";
import loginContext from "../../store/login-context";
import { logoutService } from "../../api/MemberService";
import classes from "./Header.module.css";
import { MdPersonOutline } from "react-icons/md";
import { FiLogOut } from "react-icons/fi";
import { LuTickets } from "react-icons/lu";
import { motion } from "framer-motion";

const Header = () => {

  const loginCtx = useContext(loginContext);
  const navigate = useNavigate();
  const location = useLocation();
  const role = sessionStorage.getItem("USER_ROLE");
  const token = sessionStorage.getItem("accessToken");

  const logoutHandler = async () => {
    const logoutResponseData = await logoutService();
    if (logoutResponseData.success) {
      toast.success("로그아웃에 성공하셨습니다.", { position: "top-center", autoClose: 1000 });
    } else {
      toast.error("로그아웃에 실패하셨습니다. \n 강제로 로그아웃합니다.", { position: "top-center", autoClose: 1000 });
    }
    loginCtx.logoutUser();
    navigate("/auth");
  };

  const isActive = (path) =>
    location.pathname === path || location.pathname.startsWith(path);

  return (
    <>
      <header className={classes.header}>
        <div className={classes.topbar}>
          <div className={`${classes.topInner} container`}>
            <Link to="/" className={classes.logo}>
              <span className={classes.logoText}>PetGang</span>
            </Link>
          </div>
        </div>

        <nav className={classes.navbar}>
            <Link to="/" className={`${classes.tab} ${window.location.pathname === "/" ? classes.active : ""}`}>
              홈
            </Link>
            <Link to="/tour" className={`${classes.tab} ${isActive("/tour") ? classes.active : ""}`}>
              투어
            </Link>
            <Link to="/chat" className={`${classes.tab} ${isActive("/chat") ? classes.active : ""}`}>
              채팅
            </Link>
            <Link to="/ai" className={`${classes.tab} ${isActive("/ai") ? classes.active : ""}`}>
              AI 챗봇 및 코스 추천
            </Link>
            {role === "ADMIN" && (
              <Link to="/admin" className={`${classes.tab} ${isActive("/admin") ? classes.active : ""}`}>
                서비스 관리
              </Link>
            )}
        </nav>
        <div className={classes.buttonContainer}>
            {!token ? (
                <>
                  <motion.div whileHover={{ scale : 1.03 }} >
                    <Link to="/auth" className={classes.actionLink}>
                      <MdPersonOutline className={classes.icon} />
                      <span>로그인</span>
                    </Link>
                  </motion.div>
                  <motion.div whileHover={{ scale : 1.03 }} >
                    <Link to="/my" className={classes.actionLink}>
                      <LuTickets className={classes.icon} />
                      <span>마이페이지</span>
                    </Link>
                  </motion.div>
                </>
              ) : (
                <>
                  <motion.div whileHover={{ scale : 1.03 }} >
                    <Link to="/my" className={classes.actionLink}>
                      <LuTickets className={classes.icon} />
                      <span>마이페이지</span>
                    </Link>
                  </motion.div>
                  <motion.div whileHover={{ scale : 1.03 }} >
                    <Link className={classes.actionLink} onClick={logoutHandler}>
                      <FiLogOut className={classes.icon} />
                      <span>로그아웃</span>
                    </Link>
                  </motion.div>
                </>
            )}
        </div>
      </header>
      <div aria-hidden className={classes.headerSpacer} />
    </>
  );
};

export default Header;
