import classes from "./UserInfo.module.css";
import { useEffect, useMemo, useState } from "react";
import { getMyChatBotService } from "../../api/ChatBotService";
import { getMyChatRoomService, getMyManagerChatRoomService } from "../../api/ChatRoomService";
import { toast } from "react-toastify";
import { motion } from "framer-motion";
import Loading from "../LayoutComponents/Loading";
const UserInfo = ({ loginCtx }) => {

    const { createTime, email, name, userId, role } = loginCtx || {};
    const roleTitle = role === "USER" ? "일반 사용자 등급" : "관리자 등급";

    const [data,setData] = useState({});
    const [isLoading, setIsLoading] = useState(false);
    const joinedAt = useMemo(() => {
        if (!createTime) return '가입일 정보 없음';
        try {
        const d = new Date(createTime);
        const y = d.getFullYear();
        const m = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${y}.${m}.${day} 가입`;
        } catch {
        return '가입일 정보 없음';
        }
    }, [createTime]);

    useEffect(() => {
        const fetchDatas = async () => {
            if (loginCtx.userId === '') return;
            setIsLoading(true);
            const chatBotResponse = await getMyChatBotService();
            const chatRoomResponse = await getMyChatRoomService();
            const managerChatRoomResponse = await getMyManagerChatRoomService();

            if (!chatBotResponse.success || !chatRoomResponse.success || !managerChatRoomResponse.success){
              toast.warning("사용자 정보를 받아오지 못했습니다. \n 잠시만 기다려주세요.", {
                  position: "top-center",
                  autoClose: 2000,
                  hideProgressBar: false,
                  closeOnClick: true,
                  pauseOnHover: true,
                  draggable: true,
                  progress: undefined,
              });
              return;
            }
            const chatBotData = chatBotResponse.data;
            const chatRoomData = chatRoomResponse.data;
            const managerData = managerChatRoomResponse.data;
            let count = 0;
            for(let i=0;i<chatRoomData.length;i++){
               count += chatRoomData[i].lastMessageNumber - chatRoomData[i].currentReadNumber;
            }
            setData({
              managerChatRoom : managerData.length,
              joinChatRoom : chatRoomData.length,
              aiChatRoom : chatBotData.length,
              unreadCount : count
            })
            setIsLoading(false);
        };
        fetchDatas();
    },[loginCtx])

    return (
        <>
          {isLoading && <Loading />}
          <motion.section 
            key='petGang-my-userinfo'          
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.3 }}
            className={classes.container}>
          <div className={classes.welcomeCard}>
            <div className={classes.welcomeWrapper}>
              <h2 className={classes.title}>PetGang Tour</h2>
              <h3 className={classes.hello}><strong>{name || '고객'}</strong>님 반갑습니다!</h3>
              <p className={classes.joinedAt}>{joinedAt}</p>
              <p className={classes.text}>{roleTitle}</p>
              <p className={classes.text}>{email || '이메일 정보 없음'}</p>
              <p className={classes.text}>{userId || '아이디 정보 없음'}</p>
            </div>
            <div className={classes.stats}>
              <h2 className={classes.statTitle}>내 활동 요약</h2>
              <div className={classes.statBoxWrapper}>
                  <div className={classes.statBox}>
                    <span className={classes.statLabel}>생성한 채팅방</span>
                    <span className={classes.statValue}>{data.managerChatRoom}개</span>
                  </div>
                  <div className={classes.statBox}>
                    <span className={classes.statLabel}>현재 참여 채팅방</span>
                    <span className={classes.statValue}>{data.joinChatRoom}개</span>
                  </div>
                  <div className={classes.statBox}>
                    <span className={classes.statLabel}>AI 챗봇</span>
                    <span className={classes.statValue}>{data.aiChatRoom}개</span>
                  </div>
                  <div className={classes.statBox}>
                    <span className={classes.statLabel}>읽지 않은 메시지</span>
                    <span className={classes.statValue}>{data.unreadCount}개</span>
                  </div>
              </div>
            </div>
          </div>
        </motion.section>
        </>
    )
};

export default UserInfo;