import React, { useState, useEffect } from "react";
import classes from "./AIChatList.module.css";
import {
  getMyChatBotService,
  saveNewChatBotRoomService,
  deleteChatBotService,
  editChatBotService,
} from "../../api/ChatBotService";
import { toast } from "react-toastify";
import { motion, AnimatePresence } from "framer-motion";
import AIChat from "./AIChat";
import AIChatSection from "./AIChatSection";
import Loading from "../LayoutComponents/Loading";

const TITLE_MAX = 50;

const AIChatList = () => {

    const [chatSections, setChatSections] = useState([]);
    const [currentSection, setCurrentSection] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [title,setTitle] = useState('');

    const titleChangeHandler = (e) => setTitle(e.target.value);

    const fetchChatSections = async () => {        
        const chatSectionsResponse = await getMyChatBotService();
        if (chatSectionsResponse.success) {
            const reversedSections = [...chatSectionsResponse.data].reverse();
            setChatSections(reversedSections);
            if (reversedSections.length > 0) {
                setCurrentSection(reversedSections[0]);
            }
        } else {
            toast.error("AI 채팅방을 불러오는데 실패하였습니다.", {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        }
    };

    useEffect(() => {
        fetchChatSections();
    },[])

    const createChatSectionHandler = async () => {
        if (title.trim().length === 0) {
            toast.warning("채팅방 제목 설정은 필수입니다.", {
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
        if (title.trim().length > TITLE_MAX) {
            toast.warning(`채팅방 제목은 ${TITLE_MAX}자 이내로 작성해주세요.`, {
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
        const createSectionsResponse = await saveNewChatBotRoomService(title);
        if (createSectionsResponse.success) {
            setCurrentSection(createSectionsResponse.data);
            setChatSections((prevSections) => [createSectionsResponse.data,...prevSections, ]);
            setTitle('');
        } else {
            toast.error("AI 채팅방을 생성하는데 실패하였습니다.", {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        }
    };  

    const setCurrentSectionHandler = (section) => setCurrentSection(section);

    const deleteSectionHandler = async (sectionId) => {
        if (currentSection && sectionId === currentSection.id) {
            setCurrentSection(null);
        }
        const deleteSectionResponse = await deleteChatBotService(sectionId);
        if (deleteSectionResponse.success && deleteSectionResponse.data){
            setChatSections((prevSections) => prevSections.filter((section) => section.id !== sectionId));
        }
        fetchChatSections();
    };

    const saveEditedTitle = async (sectionId, editTitle) => {
        if (editTitle.trim().length === 0) {
            toast.warning("채팅방 제목 설정은 필수입니다.", {
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
        if (editTitle.trim().length > TITLE_MAX) {
            toast.warning(`채팅방 제목은 ${TITLE_MAX}자 이내로 작성해주세요.`, {
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
        setChatSections(prev =>
            prev.map(sec => sec.id === sectionId ? { ...sec, title: editTitle } : sec)
        );
        if (currentSection && currentSection.id === sectionId) {
            setCurrentSection(prev => prev.id === sectionId ? { ...prev, title: editTitle } : prev);
        }
        const editSectionResponse = await editChatBotService(sectionId, editTitle);
        if (!editSectionResponse.success || !editSectionResponse.data) {  
            toast.error("제목 수정에 실패하였습니다.", {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        }
    };

  return (
    <>
      {isLoading && <Loading />}
        <div className={classes.chat_wrapper}>
            <motion.div
                className={classes.chat_section_wrapper}
            >
                <div className={classes.chat_section_list}>
                    <motion.input
                        type="text"
                        value={title}
                        className={classes.input}
                        placeholder="제목을 입력해주세요."
                        onChange={titleChangeHandler}
                    />
                    <motion.button
                        onClick={createChatSectionHandler}
                    >
                        + 새 채팅 생성
                    </motion.button>
                <AnimatePresence>
                    {chatSections.length !== 0 ? (
                        chatSections.map((section) => (
                            <motion.div
                                key={section.id}
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                exit={{ opacity: 0 }}
                                transition={{ duration: 0.3 }}
                            >
                            <AIChatSection
                                onSetCurrent={setCurrentSectionHandler}
                                onEdit={saveEditedTitle}
                                onDelete={deleteSectionHandler}
                                section={section}
                            />
                            </motion.div>
                        ))
                    ) : (
                    <motion.p
                        className={classes.no_chat_room_message}
                    >
                        아직 등록된 채팅이 존재하지 않습니다. <br/>
                        새로운 채팅을 생성해보세요!
                    </motion.p>
                    )}
                </AnimatePresence>
                </div>
            </motion.div>
            <AnimatePresence>
                {currentSection && chatSections.length > 0 &&  (
                    <motion.div
                        className={classes.chat_content}
                        initial={{ opacity: 0 }}
                        exit={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ delay: 0.4, duration: 0.6 }}
                    >
                        <AIChat section={currentSection} />
                    </motion.div>
                )}
            </AnimatePresence>
            {chatSections.length === 0 && (
                <div
                    className={classes.new_chat_description}>
                        새로운 채팅방을 생성하여 궁금한 점을 물어보세요! <br/>
                        반려동물 건강 정보 및 케어에 대한 다양한 정보를 얻을 수 있어요. <br/>
                        <span style={{ color: '#10a37f', fontWeight: 'bold' }}>
                            본 답변은 반려동물 건강 정책 자료를 기반으로 생성됩니다.
                        </span>
                </div>
            )}
        </div>
    </>
  );
};

export default AIChatList;
