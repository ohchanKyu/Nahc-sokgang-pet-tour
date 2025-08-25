import classes from "./DeleteModal.module.css";
import { motion } from "framer-motion";
const DeleteModal = ({ onConfirm, onClose, loginCtx }) => {

    return (
         <div className={classes.modal_content}>
            <h2 className={classes.modal_title}>회원 탈퇴 확인</h2>
            <p className={classes.text}>
                {loginCtx.name}님 정말로 탈퇴하시겠습니까? <br/>
                탈퇴하시면 이용중인 서비스가 폐쇄되며 <br/>
                모든 데이터는 복구 불가입니다.
            </p>
            <ul className={classes.check_list}>
                <li className={classes.check_one}>
                    ✔ 채팅방, 채팅 기록, AI 챗봇, 프로필 등 <br/> 모든 정보가 삭제됩니다.
                </li>
                <li className={classes.check_two}>
                    ✔ 이전 정보는 모두 삭제되며 <br/> 필요한 데이터는 미리 백업을 해주세요.
                </li>
            </ul>
            <div className={classes.modal_btns}>
                <motion.button whileHover={{ scale : 1.02 }} className={classes.confirm_btn} onClick={onConfirm}>탈퇴하기</motion.button>
                <motion.button whileHover={{ scale : 1.02 }} className={classes.cancel_btn} onClick={onClose}>취소하기</motion.button>
            </div>
        </div>
    )
};

export default DeleteModal;