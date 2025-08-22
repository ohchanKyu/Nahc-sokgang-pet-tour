import React, { useContext, useState } from "react";
import classes from "./ChatPage.module.css";
import Header from '../components/LayoutComponents/Header';
import Footer from "../components/LayoutComponents/Footer";
import AllChatRoomList from "../components/ChatPageComponents/AllChatRoomList";
import loginContext from "../store/login-context";
import { clearUnreadMessageService } from "../api/ChatService";
import Chat from "../components/ChatPageComponents/Chat";

const chatTimeFormatHandler = (timestamp) => {

    if (!timestamp){
        return "등록된 채팅이 존재하지 않습니다."
    }
    
    const now = new Date();
    const chatTime = new Date(timestamp);
    const diffMs = now - chatTime;
    const diffMinutes = Math.floor(diffMs / (1000 * 60));
    const isSameDay = now.toDateString() === chatTime.toDateString();
    
    if (diffMinutes < 60) {
        if (diffMinutes === 0){
            return '1분 전';
        }
        return `${diffMinutes}분 전`;
    } else if (isSameDay) {
        return chatTime.toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit" });
    } else {
        return chatTime.toLocaleDateString("ko-KR", { month: "numeric", day: "numeric" }) + "일";
    }
}

const ChatPage = () => {

    const loginCtx = useContext(loginContext);
    const [isChat,setIsChat] = useState(false);
    const [chatRoom,setChatRoom] = useState({});
    
    // 채팅 창 연결
    const connectChatHandler = async (chatRoom) => {
        await clearUnreadMessageService(chatRoom.roomId);
        setIsChat(true);
        setChatRoom(chatRoom);
    };

    // 채팅 창 연결 취소
    const disconnectChatHandler = async (chatRoom) => {
        await clearUnreadMessageService(chatRoom.roomId);
        setIsChat(false);
        setChatRoom({});
    };

    return (
        <>
            <Header/>
            <div className={classes.container}>
                <AllChatRoomList  
                    onChatConnect={connectChatHandler} 
                    onFormatChatTime={chatTimeFormatHandler}
                    chatRoom={chatRoom}/>
                {isChat && chatRoom && <Chat 
                    onChatDisconnect={disconnectChatHandler} 
                    memberId={loginCtx.memberId} 
                    chatRoom={chatRoom}/>}
            </div>
            <Footer/>
        </>
    )
};

export default ChatPage;