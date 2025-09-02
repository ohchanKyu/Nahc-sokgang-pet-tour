import React ,{ useEffect, useState } from "react";
import classes from "./AllChatRoomList.module.css";
import { getAllChatRoomService,
    getMyChatRoomService } from '../../api/ChatRoomService';
import { motion, AnimatePresence } from "framer-motion";
import { toast } from "react-toastify";
import Loading from "../LayoutComponents/Loading";
import KeywordRoomForm from "./KeywordRoomForm";
import ChatCreateModal from "./ChatCreateModal";
import ChatRoom from "./ChatRoom";
import MyChatRoomList from "./MyChatRoomList";

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

const AllChatRoomList = ({ onChatConnect, chatRoom, onFormatChatTime  }) => {

    const [chatRooms,setChatRooms] = useState([]);
    const [isCreate,setIsCreate] = useState(false);
    const [isSubmit, setIsSubmit] = useState(false);
    const [type,setType] = useState(1);
    const [signal, setSignal] = useState(false);
    
    const typeChangeHandler = (type) => {
        setType(type);
    };

    const fetchConnectChatHandler = (chatRoom) => {
        onChatConnect(chatRoom);
        fetchAllChatRoomsHandler();
    }

    const fetchAllChatRoomsHandler = async () => {
        setIsSubmit(true);
        const chatRoomsResponse = await getAllChatRoomService();
        if (chatRoomsResponse.success){
            const chatRoomsResponseData = await chatRoomsResponse.data;
            const myChatRoomsResponse = await getMyChatRoomService();
            if (myChatRoomsResponse.success){
                const myChatRoomsResponseData = await myChatRoomsResponse.data;
                const fetchAllRoomsData = chatRoomsResponseData.map(item => {
                    const isParticipate = myChatRoomsResponseData.some(myChatRoom => myChatRoom.roomId === item.roomId);
                    if (isParticipate) {
                        return { ...item, isParticipate : true };
                    }else{
                        return {... item, isParticipate : false }
                    };
                });
                setChatRooms(fetchAllRoomsData);
                setTimeout(() => {
                    setIsSubmit(false);
                }, 500);
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
        }else{
            const errorMessage = chatRoomsResponse.message;
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

    const createRoomHandler = async () => {
        setType(2);
        setSignal(!signal);
    };

    const joinChatRoomHandler = async (chatRoom) => {
       fetchConnectChatHandler(chatRoom);
    };

    useEffect(() => {
        fetchAllChatRoomsHandler();
    }, [type]);

    return (
        <>
            {isSubmit && <Loading/>}
            {isCreate && <ChatCreateModal onCreated={createRoomHandler} onClose={() => setIsCreate(false)}/>}
            <div className={classes.page}>
            <div className={classes.all_chatRoom_container}>
                <h2 className={classes.chatRoom_header_text}>그룹 채팅방</h2>

                <div className={classes.chatRoom_description}>
                    여행 중 발생할 수 있는 유용한 정보나 경험을 공유할 수 있습니다. <br/>
                    이를 통해 반려동물 동반 여행 팁, 장소 추천 등을 실시간으로 <br/>
                    주고받으며, 여행 전반에 대한 유용한 정보를 쉽게 얻어보세요.
                </div>

                <div className={classes.buttonContainer}>
                <motion.button
                    whileHover={{ scale: 1.03 }}
                    className={`${classes.button} ${type===1 ? classes.active : ""}`}
                    onClick={() => typeChangeHandler(1)}
                >
                    전체 채팅방
                </motion.button>
                <motion.button
                    whileHover={{ scale: 1.03 }}
                    className={`${classes.button} ${type===2 ? classes.active : ""}`}
                    onClick={() => typeChangeHandler(2)}
                >
                    나의 채팅방
                </motion.button>
                <motion.button
                    whileHover={{ scale: 1.03 }}
                    className={`${classes.button} ${type===3 ? classes.active : ""}`}
                    onClick={() => typeChangeHandler(3)}
                >
                    키워드 검색
                </motion.button>
                <motion.button
                    whileHover={{ scale: 1.03 }}
                    className={classes.button}
                    onClick={() => setIsCreate(true)}
                >
                    채팅방 생성
                </motion.button>
                </div>

                {type === 1 && (
                <>
                    {chatRooms.length > 0 && (
                    <div className={classes.chatRoom_list}>
                        <AnimatePresence>
                        {chatRooms.map(item => (
                            <ChatRoom key={item.roomId} item={item} onJoin={joinChatRoomHandler}/>
                        ))}
                        </AnimatePresence>
                    </div>
                    )}
                    {chatRooms.length === 0 && <NoChatRoomMessage/>}
                </>
                )}

                {type === 2 && (
                    <MyChatRoomList
                        signal={signal}
                        chatRoom={chatRoom}
                        onFormatChatTime={onFormatChatTime}
                        onChatConnect={fetchConnectChatHandler}
                    />
                )}

                {type === 3 && (
                    <KeywordRoomForm
                        onType={() => typeChangeHandler(2)}
                        onChatConnect={fetchConnectChatHandler}
                        onJoin={joinChatRoomHandler}
                    />
                )}
            </div>
            </div>
        </>
    );
};


export default AllChatRoomList;