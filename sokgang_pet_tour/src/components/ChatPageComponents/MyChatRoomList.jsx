import React,{ useEffect, useState } from "react";
import classes from "./MyChatRoomList.module.css";
import { getMyChatRoomService, leaveChatRoomService } from '../../api/ChatRoomService';
import { motion, AnimatePresence } from "framer-motion";
import { toast } from "react-toastify";
import Loading from "../LayoutComponents/Loading";
import MyChatRoom from "./MyChatRoom";

const NoChatRoomMessage = () => {
    
    return (
        <motion.p 
            className={classes.no_room_message}
            initial={{ opacity: 0, x: -100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: 100 }}
            transition={{ duration: 0.5, ease: "easeOut" }}>
                아직 등록된 채팅방이 없습니다. <br/>
                채팅방을 등록해보세요!
        </motion.p>
    )
}

const MyChatRoomList = ({ chatRoom, onFormatChatTime, onChatConnect }) => {

    const [myChatRooms,setMyChatRooms] = new useState([]);
    const [isSubmit, setIsSubmit] = new useState(false);

    const leaveChatRoomHandler = async (roomId) => {
        
        setIsSubmit(true);
        const unregisterResponse = await leaveChatRoomService(roomId);
        if (unregisterResponse.success){
            setTimeout(() => {
                fetchMyChatRoomsHandler();
                setIsSubmit(false);
            },1500)
        }else{
            const errorMessage = unregisterResponse.message;
            toast.error(`일시적 오류입니다. \n ${errorMessage}`, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
            setIsSubmit(false);
        }
    };
    const fetchMyChatRoomsHandler = async () => {
        setIsSubmit(true);
        const myChatRoomsResponse = await getMyChatRoomService();
        if (myChatRoomsResponse.success){
            const myChatRoomsResponseData = await myChatRoomsResponse.data;
            const fetchMyChatRoomResponse = myChatRoomsResponseData.map(item => {
                return { ...item, lastMessageTime : onFormatChatTime(item.lastMessageTime)}
            });
            setMyChatRooms(fetchMyChatRoomResponse)
            setIsSubmit(false);
        }else{
            const errorMessage = myChatRoomsResponse.message;
            toast.error(`일시적 오류입니다. \n ${errorMessage}`, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
            setIsSubmit(false);
        }
    };

    const connectChatHandler = (room) => {
        onChatConnect(room);
        fetchMyChatRoomsHandler();
    };

    useEffect( () => {
        fetchMyChatRoomsHandler();
    }, [chatRoom]);

    return (
        <React.Fragment>
            {isSubmit && <Loading/>}
            {myChatRooms.length > 0 && (
                <ul className={classes.chatRoom_list}>
                    <AnimatePresence>
                    {myChatRooms.map((item) => {
                        return (
                            <MyChatRoom 
                                currentChatRoom={chatRoom}
                                onFormatChatTime={onFormatChatTime}
                                onParticipateChat={connectChatHandler}
                                onUnparticipateChat={leaveChatRoomHandler}
                                key={item.roomId} chatRoom={item}/>
                        )
                    })}
                    </AnimatePresence>
                </ul>
            )}
            {myChatRooms.length === 0 && <NoChatRoomMessage/>}
        </React.Fragment>
    )
};

export default MyChatRoomList;