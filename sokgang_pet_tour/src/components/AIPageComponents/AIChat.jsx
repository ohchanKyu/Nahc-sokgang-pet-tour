import React, { useState, useRef, useEffect } from "react";
import classes from "./AIChat.module.css";
import { getAllChatHistoryService } from "../../api/ChatBotService";
import { motion, AnimatePresence } from "framer-motion";
import ReactMarkdown from 'react-markdown';

const baseURL = `${import.meta.env.VITE_LLM_BACKEND_URL}/api/v1/llm/chat`

const AIChat = ({ section }) => {

    const [isLoading,setIsLoading] = useState(false);
    const [previousMessages, setPreviousMessages] = useState([]);
    const [question,setQuestion] = useState('');
    const textareaRef = useRef(null);
    const chatContainerRef = useRef(null);
    const latestAnswerRef = useRef(null);
    const composingRef = useRef(false);    

    useEffect(() => {
        if (latestAnswerRef.current) {
            latestAnswerRef.current.scrollIntoView({ behavior: "smooth", block: "nearest" });
        }
    }, [previousMessages]);


    const questionChangeHandler = (event) => {
        setQuestion(event.target.value);
        adjustTextareaHeight();
    };
    
    const questionSubmitHandler = async (event) => {
        if (event.key === 'Enter') {
            if (composingRef.current) return;
            if (event.shiftKey) {
                event.preventDefault();
                setQuestion(prev => prev + "\n");
                return;
            }
            if (question.trim().length === 0) {
                event.preventDefault();
                return;
            }
            event.preventDefault();
            if (textareaRef.current){
                textareaRef.current.style.height = "auto";
                textareaRef.current.blur();
            }
    
            setIsLoading(true);
            const currentQuestion = question;
            setQuestion('');
            setPreviousMessages((prev) => [...prev, { question: currentQuestion, answer: "" }]);
            const res = await fetch(baseURL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-NAHC-TOKEN": `${import.meta.env.VITE_X_NAHC_TOKEN}`,
                },
                body: JSON.stringify({
                    message : currentQuestion,
                    session_id : section.id,
                }),
            });
    
            const reader = res.body?.getReader();
            const decoder = new TextDecoder();
            let done = false;
            let fullAnswer = "";
            let receivedFirstChunk = false;

            while (!done && reader) {
                const { value, done: doneReading } = await reader.read();
                done = doneReading;
                const chunk = decoder.decode(value);
                fullAnswer += chunk;

                if (!receivedFirstChunk && chunk.trim() !== "") {
                    setIsLoading(false);
                    receivedFirstChunk = true;
                }

                setPreviousMessages((prevMessages) => {
                    const updatedMessages = [...prevMessages];
                    const lastIndex = updatedMessages.length - 1;
                    updatedMessages[lastIndex] = {
                        ...updatedMessages[lastIndex],
                        answer: updatedMessages[lastIndex].answer + chunk,
                    };
                    return updatedMessages;
                });
            }
        }
    };

    const adjustTextareaHeight = () => {
        const el = textareaRef.current;
        if (!el) return;

        el.style.height = 'auto';

        const css = getComputedStyle(el);
        const minH = parseInt(css.minHeight, 10); 
        const maxH = parseInt(css.maxHeight, 10);  

        const nextH = Math.min(el.scrollHeight, maxH); 
        if (el.scrollHeight > minH) {
            el.style.height = `${nextH}px`;
        }
        el.style.overflowY = el.scrollHeight > maxH ? 'auto' : 'hidden';
    };

     const toQAPairs = (items) => {
        const sorted = [...items].sort((a,b) => new Date(a.time) - new Date(b.time));
        const out = [];
        for (let i = 0; i < sorted.length; i += 2) {
            out.push({
                question: sorted[i]?.content ?? "",
                answer:   sorted[i + 1]?.content ?? ""
            });
        }
        return out;
    };

    useEffect(() =>  {
        const fetchChatHistory = async () => {
            setQuestion("");
            const chatHistoryResponse = await getAllChatHistoryService(section.id);
            if (chatHistoryResponse.success){
                const qaList = toQAPairs(chatHistoryResponse.data);
                setPreviousMessages(qaList);
            }
        };
        fetchChatHistory();
    },[section.id])

    useEffect(() => {
        adjustTextareaHeight();
    }, [question]);

    useEffect(() => {
        const el = textareaRef.current;
        if (!el) return;
        el.style.height = '80px';
    },[]);

    const onCompositionStart = () => { composingRef.current = true; };
    const onCompositionEnd   = () => { composingRef.current = false; };
    
    return (
        <div className={classes.wrapper}>
            <h1>{section.title}</h1>
            <div className={classes.chat_wrapper}>
                <div className={classes.chat_container} ref={chatContainerRef}>
                    <div className={classes.chat_header_container}>
                        <p className={classes.chat_main_text}>
                            반려동물 건강 정보 및 케어에 대해 원하는것을 질문해보세요!
                            해당 답변은 반려동물 건강 정책 자료를 기반으로 생성됩니다.
                        </p>
                    </div>
                    <div className={classes.chat_response_container}>
                        {previousMessages.length === 0 && (
                            <p className={classes.no_chat_message}>아직 질문하신 내역이 없습니다.</p>   
                        )}
                        <AnimatePresence>
                            {previousMessages.map((item, index) => {
                                const isLast = index === previousMessages.length - 1;
                                return (
                                    <motion.div
                                        key={index}
                                        ref={isLast ? latestAnswerRef : null}
                                        className={classes.chat_response_wrapper}
                                        initial={{ opacity: 0 }}
                                        animate={{ opacity: 1 }}
                                        exit={{ opacity: 0 }}
                                        transition={{ duration: 0.3 }}
                                    >
                                        <div className={classes.question_container}>
                                            <p className={classes.question_text}>{item.question}</p>
                                        </div>
                                        <div className={classes.answer_container}>
                                            <div className={classes.answer_text}>
                                                <ReactMarkdown>{item.answer}</ReactMarkdown>
                                            </div>
                                        </div>
                                        {isLast && isLoading && (
                                            <p className={classes.loading_text}>
                                                답변을 생성중입니다.
                                                잠시만 기다려주세요.
                                            </p>
                                        )}
                                    </motion.div>
                                );
                            })}
                        </AnimatePresence>
                    </div>
                    <div className={classes.input_container}>
                        <textarea
                            className={classes.gemini_input}
                            value={question}
                            ref={textareaRef}
                            onCompositionStart={onCompositionStart}
                            onCompositionEnd={onCompositionEnd}
                            onChange={questionChangeHandler}
                            onKeyDown={questionSubmitHandler}
                            placeholder="Message Prompt..."></textarea>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AIChat;