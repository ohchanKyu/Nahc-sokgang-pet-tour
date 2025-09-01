import classes from "./JoinChatRoomModal.module.css";
import { useState } from "react";
import Modal from "../LayoutComponents/Modal";
import { joinChatRoomService } from "../../api/ChatRoomService";
import { toast } from "react-toastify";
import { motion } from "framer-motion";
import Loading from "../LayoutComponents/Loading";

const JoinChatRoomModal = ({ onClose, onJoin , room }) => {

    const [nickname,setNickname] = useState('');
    const [loading, setLoading] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        if (!nickname || nickname.trim().length < 2 || nickname.trim().length > 50) {
            toast.warn("닉네임은 2~8자입니다.", { position: "top-center", autoClose: 500 });
            return;
        }
        setLoading(true);
        const res = await joinChatRoomService(room.roomId,nickname);
        if (res?.success) {
            toast.success("채팅방에 참여하였습니다..", { position: "top-center", autoClose: 500 });
            setLoading(false);
            onJoin(res.data);
            onClose();
        } else {
            toast.error(res?.message || "채팅방 참여에 실패했습니다.", { position: "top-center", autoClose: 500});
        }
    };

    return (
        <>
            {loading && <Loading />}
            <Modal>
                <div className={classes.joinContainer}>
                    <form className={classes.form} onSubmit={submit}>
                        <div className={classes.header}>
                            <h2 className={classes.roomTitle}>{room.name}</h2>
                            <div className={classes.titleUnderline} />
                        </div>
                        <p className={`${classes.description} ${classes.descCard}`}>{room.description}</p>
                        <div className={classes.metaRow}>
                            <div className={classes.badge}>
                            <span className={classes.metaLabel}>현재 인원</span>
                            <span className={classes.metaValue}>
                                {room.currentParticipants}/{room.maxParticipants}
                            </span>
                            </div>
                            <div className={classes.badge}>
                            <span className={classes.metaLabel}>개설일</span>
                            <span className={classes.metaValue}>
                                {room.createdAt.split("T")[0]}
                            </span>
                            </div>
                        </div>
                        <div className={classes.element}>
                            <label>사용할 닉네임</label>
                            <input value={nickname} onChange={(e) => setNickname(e.target.value)} placeholder="예) 홍길동" />
                        </div>
                        <div className={classes.modalActions}>
                            <motion.button whileHover={{ scale:1.03 }} type="button" className={classes.btnGhost} onClick={onClose}>취소하기</motion.button>
                            <motion.button whileHover={{ scale:1.03 }} type="submit" className={classes.btnPrimary}>참여하기</motion.button>
                        </div>
                    </form>
                </div>
            </Modal>
        </>
    )
};

export default JoinChatRoomModal;