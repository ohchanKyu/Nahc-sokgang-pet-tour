import React, { useEffect, useState, useRef } from "react";
import { Client } from '@stomp/stompjs';
import classes from "./Chat.module.css";
import { getChatRoomMessageService, clearUnreadMessageService } from "../../api/ChatService";
import { AnimatePresence, motion } from 'framer-motion';
import { Send } from 'lucide-react';
import { ImExit } from "react-icons/im";
import { FaCalendarAlt } from "react-icons/fa";
import { toast } from "react-toastify";

const baseURL = window.location.hostname === 'localhost' 
  ? 'ws://localhost:8080/ws'
  : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/ws`;

const MAX_H = 180;

const parseTime = (timestamp) => {
  const chatTime = new Date(timestamp);
  return chatTime.toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit", hour12: false });
};

const groupMessagesByDate = (messages) => {
  return messages.reduce((acc, message) => {
    const date = new Date(message.time);
    const formattedDate = date.toLocaleDateString().split('T')[0];
    const dayOfWeek = date.toLocaleDateString('ko-KR', { weekday: 'long' });
    const dateKey = `${formattedDate} ${dayOfWeek}`;
    if (!acc[dateKey]) acc[dateKey] = [];
    acc[dateKey].push(message);
    return acc;
  }, {});
};

const Chat = ({ memberId, chatRoom, onChatDisconnect }) => {

  const [messages, setMessages] = useState([]);
  const [currentRoom, setCurrentRoom] = useState(null);
  const [inputMessage, setInputMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isOpen, setIsOpen] = useState(true);
  const isReadOnly = chatRoom?.status === "READ_ONLY";


  const clientRef = useRef(null);
  const subRef = useRef(null);  
  const messagesRef = useRef(null);
  const inputRef = useRef(null);
  const activeRoomIdRef = useRef(null); 

  const [isConnected, setIsConnected] = useState(false);

  const adjustTextareaHeight = () => {
    const ta = inputRef.current;
    if (!ta) return;
    ta.style.height = "auto";
    const sH = ta.scrollHeight;
    ta.style.height = Math.min(sH, MAX_H) + "px";
    ta.style.overflowY = sH > MAX_H ? "auto" : "hidden";
  };

  const inputMessageHandler = (event) => {
    if (event.target.value.length > 5000) {
      toast.error("최대 5000자까지만 전달가능합니다.", {
        position: "top-center",
        autoClose: 2000,
      });
      return;
    }
    setInputMessage(event.target.value);
    requestAnimationFrame(adjustTextareaHeight);
  };

  const exitChatHandler = () => {
    setIsOpen(false);
    if (currentRoom?.roomId) {
      clearUnreadMessageService(currentRoom.roomId).finally(() => {
        try { subRef.current?.unsubscribe(); } catch (_) {}
        subRef.current = null;
        onChatDisconnect && onChatDisconnect(currentRoom);
      });
    }
  };

  const sendMessageHandler = (event) => {
    if (event.isComposing) return;
    const client = clientRef.current;

    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
    }

    if (event.key === 'Enter' || event.type === 'click') {
      if (event.shiftKey) {
        event.preventDefault();
        setInputMessage(prev => {
          const next = prev + "\n";
          requestAnimationFrame(adjustTextareaHeight);
          return next;
        });
        return;
      }

      if (inputMessage.trim().length === 0) {
        event.preventDefault();
        return;
      }

      if (inputMessage.trim().length > 5000) {
        toast.error("최대 5000자까지만 전달가능합니다.", {
          position: "top-center",
          autoClose: 2000,
        });
        return;
      }

      if (!client || !client.connected) {
        console.error("STOMP Client is not connected. Cannot send message.");
        return;
      }

      const body = {
        type: "TALK",
        roomId: currentRoom.roomId,
        message: inputMessage,
        memberId,
        nickname: currentRoom.nickname,
      };

      try {
        client.publish({
          destination: "/pub/chat/message",
          body: JSON.stringify(body),
        });
        if (inputRef.current) {
          setInputMessage('');
          requestAnimationFrame(() => {
            const ta = inputRef.current;
            ta.style.height = "50px";
            ta.focus();
            ta.setSelectionRange(0, 0);
          });
        }
      } catch (error) {
        console.error("Failed to send message:", error);
      }
    }
  };

  const onMessageFrame = (frame) => {
    const newMessageBody = JSON.parse(frame.body);
    setMessages((prevGroups) => {
      const date = new Date(newMessageBody.time);
      const formattedDate = date.toLocaleDateString().split('T')[0];
      const dayOfWeek = date.toLocaleDateString("ko-KR", { weekday: "long" });
      const newMessageDate = `${formattedDate} ${dayOfWeek}`;

      let found = false;
      const updated = prevGroups.map((g) => {
        if (g.date === newMessageDate) {
          found = true;
          return {
            ...g,
            messages: [
              ...g.messages,
              { ...newMessageBody, time: parseTime(newMessageBody.time) },
            ],
          };
        }
        return g;
      });

      if (!found) {
        updated.push({
          date: newMessageDate,
          messages: [{ ...newMessageBody, time: parseTime(newMessageBody.time) }],
        });
      }
      return updated;
    });
  };

  const fetchMessages = async (roomId) => {
    try {
      setIsLoading(true);
      const messageResponse = await getChatRoomMessageService(roomId);
      const messageResponseData = messageResponse.data;
      const grouped = groupMessagesByDate(messageResponseData);
      const normalized = Object.entries(grouped).map(([date, msgs]) => ({
        date,
        messages: msgs.map((m) => ({ ...m, time: parseTime(m.time) })),
      }));
      setMessages(normalized);
    } catch (error) {
      console.error("Failed to fetch messages:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const socket = new WebSocket(baseURL);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      connectHeaders: { token: `${import.meta.env.VITE_X_NAHC_TOKEN}` },
      onConnect: () => {
        console.log('[+] STOMP connected');
        setIsConnected(true);
      },
      onWebSocketClose: () => {
        console.log('[-] WebSocket connection closed');
        setIsConnected(false);
      },
      onWebSocketError: (error) => {
        console.error('[-] WebSocket error:', error);
      },
      onStompError: (frame) => {
        console.error('Broker reported error:', frame.headers?.['message']);
        console.error('Additional details:', frame.body);
      },
    });

    clientRef.current = client;
    client.activate();

    return () => {
      try { subRef.current?.unsubscribe(); } catch (_) {}
      subRef.current = null;
      client.deactivate();
      clientRef.current = null;
      setIsConnected(false);
    };
  }, []);

  useEffect(() => {
    if (!chatRoom?.roomId || !isConnected || !clientRef.current) return;

    const client = clientRef.current;
    const roomId = chatRoom.roomId;
    activeRoomIdRef.current = roomId;
    try { subRef.current?.unsubscribe(); } catch (_) {}

    console.log(`[room-switch] subscribe -> ${roomId}`);
    const mySub = client.subscribe(`/sub/chat/room/${roomId}`, onMessageFrame);
    subRef.current = mySub;

    (async () => {
      await fetchMessages(roomId);
      setCurrentRoom(chatRoom);
    })();

    return () => {
      clearUnreadMessageService(roomId).catch(() => {});
      try { mySub?.unsubscribe(); } catch (_) {}
      if (subRef.current === mySub) subRef.current = null;
    };
  }, [chatRoom, isConnected]);

  useEffect(() => {
    if (messagesRef.current) {
      messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
    }
  }, [messages]);

  useEffect(() => { adjustTextareaHeight(); }, []);

  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.scrollTop = inputRef.current.scrollHeight;
    }
  }, [inputMessage]);

  return (
    <>
      <AnimatePresence mode="wait" initial={false}>
        {isOpen && currentRoom && (
          <motion.div
            key={currentRoom.roomId}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.3 }}
            className={classes.chat_container}
          >
            <div className={classes.chat_header}>
              <h2 className={classes.chat_room_title}>{currentRoom && currentRoom.name}</h2>
              <div className={classes.exit_chat} onClick={exitChatHandler}>
                <ImExit />
              </div>
            </div>

            <div className={classes.chat_messages} ref={messagesRef}>
              <AnimatePresence>
                {messages.length === 0 && !isLoading && (
                  <motion.p
                    key='nochatmessages'
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3 }}
                    className={classes.no_chat_message}
                  >
                    등록된 채팅이 존재하지 않습니다.
                  </motion.p>
                )}
              </AnimatePresence>

              <AnimatePresence>
                {isLoading && (
                  <motion.p
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3 }}
                    className={classes.no_chat_message}
                  >
                    대화를 불러오고 있습니다.
                  </motion.p>
                )}
              </AnimatePresence>

              {messages.map((group, index) => (
                <div key={index} className={classes.chat_messages_container}>
                  <div className={classes.chat_date_container}>
                    <div className={classes.chat_date}>
                      <FaCalendarAlt /> {group.date}
                    </div>
                  </div>

                  {group.messages.map((message, msgIndex) => {
                    if (message.type !== "TALK") {
                      return (
                        <div key={`sys-${msgIndex}`} className={classes.chat_meta_container}>
                          <div className={classes.chat_meta}>{message.message}</div>
                        </div>
                      );
                    }

                    const isMe = message.memberId === memberId;
                    return (
                      <motion.div
                        key={`msg-${msgIndex}`}
                        className={isMe ? classes.chat_my_message_container : classes.chat_message_container}
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}
                      >
                        {isMe ? (
                          <>
                            <div className={classes.nickname}>{message.nickname}</div>
                            <div className={classes.chat_display}>
                              <div className={classes.chat_time}>{message.time}</div>
                              <div className={`${classes.chat_message} ${classes.chat_message_self}`}>
                                {message.message}
                              </div>
                            </div>
                          </>
                        ) : (
                          <>
                            <div className={classes.nicknameRight}>{message.nickname}</div>
                            <div className={classes.chat_display}>
                              <div className={`${classes.chat_message} ${classes.chat_message_other}`}>
                                {message.message}
                              </div>
                              <div className={classes.chat_time}>{message.time}</div>
                            </div>
                          </>
                        )}
                      </motion.div>
                    );
                  })}
                </div>
              ))}
            </div>

            <div className={classes.chat_input_container}>
              <textarea
                ref={inputRef}
                type="text"
                className={classes.chat_input}
                placeholder={isReadOnly ? "읽기 전용 채팅방입니다." : "메시지를 입력해주세요."}
                value={inputMessage}
                rows={1}
                onChange={isReadOnly ? undefined : inputMessageHandler}
                onKeyDown={isReadOnly ? undefined : sendMessageHandler}
                disabled={isReadOnly}
                aria-disabled={isReadOnly}
                style={{ minHeight: "50px", height: inputMessage ? "auto" : "50px" }}
              />
              <button
                onClick={isReadOnly ? undefined : sendMessageHandler}
                onMouseDown={(e) => e.preventDefault()}
                className={classes.chat_send_button}
                disabled={isReadOnly || inputMessage.trim().length === 0}
                aria-disabled={isReadOnly}
              >
                <Send size={25} />
              </button>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
};

export default Chat;
