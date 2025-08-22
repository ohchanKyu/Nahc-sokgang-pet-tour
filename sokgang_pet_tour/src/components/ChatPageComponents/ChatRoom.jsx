import classes from "./ChatRoom.module.css";
import { motion } from "framer-motion";
import { useState } from "react";
import JoinChatRoomModal from "./JoinChatRoomModal";
import { FiUsers, FiClock, FiChevronRight } from "react-icons/fi";
import ChatImg from "../../assets/chat.png";

const ChatRoom = ({ item, onJoin }) => {
  const [isJoin, setIsJoin] = useState(false);

  return (
    <div className={classes.chatRoom_box_container}>
      {isJoin && (
        <JoinChatRoomModal
          room={item}
          onJoin={onJoin}
          onClose={() => setIsJoin(false)}
        />
      )}

      <motion.div
        className={classes.chatRoom_box}
        initial={{ opacity: 0, x: -60 }}
        animate={{ opacity: 1, x: 0 }}
        exit={{ opacity: 0, x: 60 }}
        transition={{ duration: 0.35, ease: "easeOut" }}
      >
        <div className={classes.chatRoom_card}>
          <div className={classes.chatRoom_header}>
            <div className={classes.chatRoom_title_container}>
              <div className={classes.chatRoom_avatar}>
                <img className={classes.chatRoom_img} src={ChatImg} alt='chat-img'/>
              </div>
              <div className={classes.chatRoom_text_header}>
                <h3 className={classes.chatRoom_title}>{item.name}</h3>
                <p className={classes.chatRoom_desc}>{item.description}</p>
              </div>
            </div>
            <span
              className={`${classes.badge} ${
                item.isParticipate ? classes.badgeOn : ""
              }`}
            >
              {item.isParticipate ? "참여중" : "참여 가능"}
            </span>
          </div>
          <div className={classes.metaRow}>
            <span className={classes.metaItem}>
              <FiUsers /> {item.currentParticipants}/{item.maxParticipants}
            </span>
            <span className={classes.metaDot} />
            <span className={classes.metaItem}>
              <FiClock /> {item.createdAt.split("T")[0]}
            </span>
          </div>
          {!item.isParticipate && (
            <div className={classes.chatRoom_buttons}>
              <button
                onClick={() => setIsJoin(true)}
                className={classes.chatRoom_joinButton}
              >
                참여하기 <FiChevronRight />
              </button>
            </div>
          )}
        </div>
      </motion.div>
    </div>
  );
};

export default ChatRoom;
