import React, { useEffect, useRef, useState } from "react";
import classes from "./MyChatRoom.module.css";
import { motion } from "framer-motion";
import { Client } from "@stomp/stompjs";
import { FiUsers, FiClock, FiChevronRight, FiLogOut } from "react-icons/fi";
import ChatImg from "../../assets/chat.png";

const baseURL =
  window.location.hostname === "localhost"
    ? "ws://localhost:8080/ws"
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/ws`;

const MyChatRoom = ({
  chatRoom,
  currentChatRoom,
  onFormatChatTime,
  onUnparticipateChat,
  onParticipateChat,
}) => {
  const [lastMessageInfo, setLastMessageInfo] = useState({
    content: "",
    time: "",
    unreadCount: 0,
  });
  const stompClient = useRef(null);

  const connect = (roomId) => {
    const socket = new WebSocket(baseURL);
    stompClient.current = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      connectHeaders: { token: `${import.meta.env.VITE_X_NAHC_TOKEN}` },
      onConnect: () => {
        stompClient.current.subscribe(`/sub/chat/room/${roomId}`, (message) => {
          const body = JSON.parse(message.body);
          if (body.type === "TALK") {
            setLastMessageInfo((prev) => {
              const time = onFormatChatTime(body.time);
              const content = body.message;
              const isActive = currentChatRoom?.roomId === roomId;
              const prevCount = prev?.unreadCount || 0;
              return {
                ...prev,
                time,
                content,
                unreadCount: isActive ? 0 : prevCount + 1,
              };
            });
          }
        });
      },
      onWebSocketClose: () => {},
      onWebSocketError: () => {},
      onStompError: () => {},
    });
    stompClient.current.activate();
  };

  const disconnect = () => {
    try {
      stompClient.current?.deactivate();
    } finally {
      stompClient.current = null;
    }
  };

  useEffect(() => {
    if (!chatRoom?.roomId) return;
    setLastMessageInfo({
      content: chatRoom.lastMessage || "",
      time: chatRoom.lastMessageTime || "",
      unreadCount:
        (chatRoom.lastMessageNumber || 0) - (chatRoom.currentReadNumber || 0),
    });
    if (!stompClient.current || !stompClient.current.connected) {
      connect(chatRoom.roomId);
    }
    return () => disconnect();
  }, [chatRoom]);
  const isActive = currentChatRoom?.roomId === chatRoom.roomId;

  return (
    <div className={classes.chatRoom_box}>
      <motion.div
        className={classes.chatRoom_box}
        initial={{ opacity: 0, x: -60 }}
        animate={{ opacity: 1, x: 0 }}
        exit={{ opacity: 0, x: 60 }}
        transition={{ duration: 0.35, ease: "easeOut" }}
      >
        <div
          className={`${classes.chatRoom_card} ${
            isActive ? classes.isActive : classes.clickable
          }`}
          onClick={() => onParticipateChat(chatRoom)}
          role="button"
        >
          <div className={classes.chatRoom_header}>
            <div className={classes.chatRoom_title_container}>
                <div className={classes.chatRoom_avatar}>
                  <img className={classes.chatRoom_img} src={ChatImg} alt="chat" />
                </div>
                <div className={classes.chatRoom_text_header}>
                    <h3 className={classes.chatRoom_title}>{chatRoom.name}</h3>
                    <p className={classes.chatRoom_last_message}>
                      {lastMessageInfo.content.length > 0
                        ? lastMessageInfo.content
                        : '아직 등록된 채팅이 없습니다.'}
                    </p>
                </div>
                <div className={classes.lastMeta}>
                    <p className={classes.last_message_time}>
                      {lastMessageInfo.time !== '등록된 채팅이 존재하지 않습니다.' ? lastMessageInfo.time : ''}
                    </p>
                    {lastMessageInfo.unreadCount > 0 && (
                      <span className={classes.badgeUnread}>{lastMessageInfo.unreadCount}</span>
                    )}
                </div>
              </div>
          </div>

          <div className={classes.metaRow}>
            <span className={classes.metaItem}>
                <FiUsers /> {chatRoom.currentParticipants}/{chatRoom.maxParticipants}
            </span>
            <span className={classes.metaDot} />
            <span className={classes.metaItem}>
              <FiClock /> {chatRoom.createdAt.split("T")[0]} 생성
            </span>
          </div>
          <div className={classes.chatRoom_buttons}>
            <button
              className={classes.chatRoom_joinButton}
              onClick={(e) => {
                e.stopPropagation();
                onParticipateChat(chatRoom);
              }}
            >
              채팅창 켜기 <FiChevronRight className={classes.chevron} />
            </button>
            {currentChatRoom.roomId !== chatRoom.roomId && (
                <button
                    className={classes.chatRoom_exitButton}
                    onClick={(e) => {
                        e.stopPropagation();
                        onUnparticipateChat(chatRoom.roomId);
                    }}
                    >
                    <FiLogOut />
                </button>
            )}
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default MyChatRoom;
