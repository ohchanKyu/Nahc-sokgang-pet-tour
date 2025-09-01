import classes from "./ChatCreateModal.module.css";
import { useState } from "react";
import Modal from "../LayoutComponents/Modal";
import { toast } from "react-toastify";
import { createChatRoomService } from "../../api/ChatRoomService";
import { motion } from "framer-motion";
import Loading from "../LayoutComponents/Loading";
const ChatCreateModal = ({ onClose, onCreated }) => {

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [maxParticipants, setMaxParticipants] = useState(50);
    const [nickname, setNickname] = useState("");
    const [loading, setLoading] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        if (name.trim().length < 2 || name.trim().length > 50) {
          toast.warn("채팅방 이름은 2~50자입니다.", { position: "top-center", autoClose: 500 });
          return;
        }
        if (description.trim().length < 5 || description.trim().length > 20) {
          toast.warn("설명은 5~20자입니다.", { position: "top-center", autoClose: 500 });
          return;
        }
        if (!nickname || nickname.trim().length < 2 || nickname.trim().length > 50) {
          toast.warn("닉네임은 2~8자입니다.", { position: "top-center", autoClose: 500 });
          return;
        }
        setLoading(true);
        const req = { name, description, maxParticipants: Number(maxParticipants), nickname };
        const res = await createChatRoomService(req);
        if (res?.success) {
             setLoading(false);
            toast.success("채팅방이 생성되었습니다.", { position: "top-center", autoClose: 500 });
            onCreated(res.data);
            onClose();
        } else {
            toast.error(res?.message || "채팅방 생성에 실패했습니다.", { position: "top-center", autoClose: 500});
        }
    };

    return (
        <Modal onClose={onClose}>
            {loading && <Loading />}
            <div className={classes.createContainer}>
                <form className={classes.form} onSubmit={submit}>
                    <label>채팅방 이름</label>
                    <input value={name} onChange={(e) => setName(e.target.value)} placeholder="예) 반려여행 논의방" />
        
                    <label>설명</label>
                    <input value={description} onChange={(e) => setDescription(e.target.value)} placeholder="방 소개를 적어주세요" />
        
                    <div className={classes.grid}>
                        <div className={classes.element1}>
                            <label>최대 인원</label>
                            <input type="number" min={2} max={1000} value={maxParticipants} onChange={(e) => setMaxParticipants(e.target.value)} />
                        </div>
                        <div className={classes.element2}>
                            <label>사용 닉네임</label>
                            <input value={nickname} onChange={(e) => setNickname(e.target.value)} placeholder="예) 홍길동" />
                        </div>
                    </div>
                    <div className={classes.modalActions}>
                        <motion.button whileHover={{ scale:1.03 }} type="button" className={classes.btnGhost} onClick={onClose}>취소</motion.button>
                        <motion.button whileHover={{ scale:1.03 }} type="submit" className={classes.btnPrimary}>생성</motion.button>
                    </div>
                </form>
            </div>
        </Modal>
    )
};

export default ChatCreateModal;