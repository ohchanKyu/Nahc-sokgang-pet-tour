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
} 
const groupMessagesByDate = (messages) => {
    return messages.reduce((acc, message) => {
        const date = new Date(message.time);
        const formattedDate = date.toLocaleDateString().split('T')[0];
        const dayOfWeek = date.toLocaleDateString('ko-KR', { weekday: 'long' });
        const dateKey = `${formattedDate} ${dayOfWeek}`; 

        if (!acc[dateKey]) {
            acc[dateKey] = [];
        }
        acc[dateKey].push(message);

        return acc;
    }, {});
};

const Chat = ({ memberId, chatRoom, onChatDisconnect }) => {
    
    const [messages, setMessages] = useState([]);
    const stompClient = useRef(null);
    const [currentRoom, setCurrentRoom] = useState(null); 
    const [inputMessage,setInputMessage] = useState('');
    const messagesRef = useRef(null);
    const inputRef = useRef(null);
    const [isLoading,setIsLoading] = useState(false);
    const [isOpen, setIsOpen] = useState(true);

    const isReadOnly = chatRoom?.status === "READ_ONLY";
    const inputMessageHandler = (event) => { 
        if (event.target.value.length > 5000){
            toast.error("최대 5000자까지만 전달가능합니다.", {
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
        setInputMessage(event.target.value);
        requestAnimationFrame(adjustTextareaHeight);
    }

    const adjustTextareaHeight = () => {
        const ta = inputRef.current;
        if (!ta) return;
        ta.style.height = "auto";                  
        const sH = ta.scrollHeight;
        ta.style.height = Math.min(sH, MAX_H) + "px";
        ta.style.overflowY = sH > MAX_H ? "auto" : "hidden";
    };

    const exitChatHandler = () => {
        setIsOpen(false);
    };

    const sendMessageHandler = (event) => {
        if (event.isComposing) return;
        if (!stompClient.current || !stompClient.current.connected) {
            console.error("STOMP Client is not connected. Cannot send message.");
            return;
        }
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
        }
        if (event.key === 'Enter' || event.type === 'click') {
            if (event.shiftKey) {
                event.preventDefault();
                setInputMessage(prev => {
                    const newMessage = prev + "\n";
                    requestAnimationFrame(() => adjustTextareaHeight());
                    return newMessage;
                });
                return;
            }
            if (inputMessage.trim().length === 0) {
                event.preventDefault();
                return;
            }
            if (inputMessage.trim().length > 5000){
                toast.error("최대 5000자까지만 전달가능합니다.", {
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
            const body = {
                type : "TALK",
                roomId: currentRoom.roomId,
                message: inputMessage,
                memberId,
                nickname : currentRoom.nickname
            };
            try {
                stompClient.current.publish({
                    destination: "/pub/chat/message",
                    body: JSON.stringify(body),
                });
                if (inputRef.current){
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

    const connect = (roomId) => {

        const socket = new WebSocket(baseURL);
        stompClient.current = new Client({
            webSocketFactory: () => socket,
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                connectHeaders: {
                    token: `${import.meta.env.VITE_X_NAHC_TOKEN}`
                },
            onConnect: () => {
                console.log(`[+] Connected to room ${roomId}`);
                stompClient.current.subscribe(`/sub/chat/room/${roomId}`, (message) => {
                    const newMessageBody = JSON.parse(message.body);
                    
                    setMessages((prevMessages) => {
                        const date = new Date(newMessageBody.time);
                        const formattedDate = date.toLocaleDateString().split('T')[0];
                        const dayOfWeek = date.toLocaleDateString("ko-KR", { weekday: "long" });
                        const newMessageDate = `${formattedDate} ${dayOfWeek}`;
                        const updatedMessages = prevMessages.map((group) => {
                            if (group.date === newMessageDate) {
                                return {
                                    ...group,
                                    messages: [...group.messages, { 
                                        ...newMessageBody, 
                                        time: parseTime(newMessageBody.time) 
                                    }]
                                };
                            }
                            return group;
                        });
                    
                        const dateExists = updatedMessages.some(group => group.date === newMessageDate);
                        if (!dateExists) {
                            updatedMessages.push({
                                date: newMessageDate,
                                messages: [{ 
                                    ...newMessageBody, 
                                    time: parseTime(newMessageBody.time) 
                                }]
                            });
                        }
                    
                        return updatedMessages;
                    });
                    
                });
            },
            onWebSocketClose: () => {
                console.log("[-] WebSocket connection closed.");
            },
            onWebSocketError: (error) => {
                console.error("[-] WebSocket error:", error);
            },
            onStompError: (frame) => {
                console.error("Broker reported error:", frame.headers['message']);
                console.error("Additional details:", frame.body);
            },
        });
        stompClient.current.activate();
    };
    
    const disconnect = () => {
        if (stompClient.current) {
            stompClient.current.deactivate();
            stompClient.current = null;
        }
    };

    const fetchMessages = async (roomId) => {
        try {
            setIsLoading(true);
            const messageResponse = await getChatRoomMessageService(roomId);
            const messageResponseData = messageResponse.data;
            const groupedMessages = groupMessagesByDate(messageResponseData);
            const fetchMessageResponseData = Object.entries(groupedMessages).map(([date, messages]) => ({
                date,
                messages: messages.map(message => ({
                    ...message,
                    time: parseTime(message.time)
                }))
            }));
            setMessages(fetchMessageResponseData);
            setIsLoading(false);
        } catch (error) {
            console.error("Failed to fetch messages:", error);
        }
    };
    
    useEffect(() => {
        if (!chatRoom.roomId) return;
        if (stompClient.current && stompClient.current.connected) {
            if (currentRoom.roomId === chatRoom.roomId){
                console.log("WebSocket already connected, skipping reconnect.");
                return;
            }
        }
        connect(chatRoom.roomId);
        fetchMessages(chatRoom.roomId);
        setCurrentRoom(chatRoom);
        return async () => {
            await clearUnreadMessageService(chatRoom.roomId);
            disconnect();
        };
    }, [chatRoom]);

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
            <AnimatePresence
                    mode="wait"
                    initial={false}
                    onExitComplete={async () => {
                        await clearUnreadMessageService(currentRoom.roomId);
                        disconnect();                    
                        onChatDisconnect(currentRoom);    
                    }}
                >
                {isOpen && currentRoom && (
                <motion.div 
                    key={currentRoom.roomId}              
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -10 }}
                    transition={{ duration: 0.3 }}
                    className={classes.chat_container}>
                    <div className={classes.chat_header}>
                        <h2 className={classes.chat_room_title}>{currentRoom && currentRoom.name}</h2>
                        <div className={classes.exit_chat} onClick={() => exitChatHandler(currentRoom)}>
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
                                    className={classes.no_chat_message}>
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
                                    className={classes.no_chat_message}>
                                        대화를 불러오고 있습니다.
                                </motion.p>
                            )}
                        </AnimatePresence>
                        {messages.map((group, index) => (
                            <div key={index} className={classes.chat_messages_container}>
                                <div className={classes.chat_date_container}>
                                <div className={classes.chat_date}>
                                    <FaCalendarAlt/> {group.date}
                                </div>
                                </div>
                                {group.messages.map((message, msgIndex) => {

                                    if (message.type !== "TALK") {
                                        return (
                                        <div key={`sys-${msgIndex}`} className={classes.chat_meta_container}>
                                            <div className={classes.chat_meta}>
                                                {message.message}
                                            </div>
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