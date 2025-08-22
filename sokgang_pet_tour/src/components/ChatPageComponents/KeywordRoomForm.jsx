import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { getChatRoomByKeywordService, getisJoinChatRoomService
 } from '../../api/ChatRoomService';
import classes from './KeywordRoomForm.module.css';
import JoinChatRoomModal from './JoinChatRoomModal';
import { FiUsers } from "react-icons/fi";

const useDebounce = (value, delay) => {
    const [debouncedValue, setDebouncedValue] = useState(value);
    useEffect(() => {
        const handler = setTimeout(() => {
        setDebouncedValue(value);
        }, delay);

        return () => {
        clearTimeout(handler);
        };
    }, [value, delay]);
    return debouncedValue;
};

const KeywordRoomForm = ({ onJoin, onChatConnect }) => {

    const [query, setQuery] = useState('');
    const debouncedQuery = useDebounce(query, 400);
    const [results, setResults] = useState([]);
    const [displayedResults, setDisplayedResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [noResultsMessageVisible, setNoResultsMessageVisible] = useState(false);
    const [initialMessageVisible, setInitialMessageVisible] = useState(true);
    
    const [joinModal, setJoinModal] = useState(false);
    const [selectedChatRoom,setSelectedChatRoom] = useState(null);
    const observerRef = useRef(null);

    const participateChatHandler = async (chatRoom) => {

        const isParticipateResponse = await getisJoinChatRoomService(chatRoom.roomId);
        if (isParticipateResponse.success) {
            if (isParticipateResponse.data){
                onChatConnect(chatRoom);
            }else{
                setJoinModal(true);
                setSelectedChatRoom(chatRoom);
            }
        }else{
            const errorMessage = createRoomResponse.message;
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

    useEffect(() => {
        const fetchData = async () => {
            if (debouncedQuery.trim() === '') {
                setResults([]); 
                setDisplayedResults([]); 
                setNoResultsMessageVisible(false);
                setInitialMessageVisible(true);
                return;
            }
            setLoading(true);
            setError(null);
            setInitialMessageVisible(false);
            try {
            const chatRoomResponse = await getChatRoomByKeywordService(debouncedQuery);
            if (chatRoomResponse.success) {
                setResults(chatRoomResponse.data);
                setDisplayedResults(chatRoomResponse.data.slice(0, 20));
                setNoResultsMessageVisible(chatRoomResponse.data.length === 0); 
            } else {
                setError('검색 결과를 불러오는데 실패했습니다.');
            }
            } catch (err) {
            setError('검색 중 오류가 발생했습니다.');
            }
            setLoading(false);
        };
        fetchData();
    }, [debouncedQuery]);
    
    useEffect(() => {
        if (results.length === 0) return;
        const observer = new IntersectionObserver(
            (entries) => {
            if (entries[0].isIntersecting && displayedResults.length < results.length) {
                const nextPage = page + 1;
                setPage(nextPage);
                setDisplayedResults(results.slice(0, nextPage * 20));
            }
            },
            { threshold: 1.0 }
        );
        if (observerRef.current) observer.observe(observerRef.current);
        return () => observer.disconnect();
    }, [results, displayedResults, page]);
    
      return (
        <AnimatePresence>
            {joinModal && <JoinChatRoomModal 
                onClose={() => setJoinModal(false)} 
                room={selectedChatRoom}
                onJoin={onJoin}/>}
            <motion.div 
                key="searchContainer"
                className={classes.searchContainer}
                initial={{ opacity: 0, x: -100 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: 100 }}
                transition={{ duration: 0.5, ease: "easeOut" }}>
            <input
                type="text"
                value={query}
                onChange={e => setQuery(e.target.value)}
                placeholder="검색어를 입력해주세요."
                className={classes.searchInput}
            />
            <div className={classes.resultsContainer}>
                {loading && <p className={classes.loadingText}>검색 중입니다...</p>}
                {error && <p className={classes.errorText}>{error}</p>}
                <AnimatePresence>
                    {initialMessageVisible && (
                        <motion.p
                            key='noInput'
                            className={classes.noResultsText}
                            initial={{ opacity: 0, y: -10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: 10 }}
                            transition={{ duration: 0.3 }}
                        >
                            검색어를 입력해보세요!
                        </motion.p>
                    )}
                </AnimatePresence>
                <AnimatePresence>
                    {!loading && !error && noResultsMessageVisible && (
                        <motion.p
                            key='noResult'
                            className={classes.noResultsText}
                            initial={{ opacity: 0, y: -10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: 10 }}
                            transition={{ duration: 0.3 }}
                        >
                            검색 결과가 존재하지 않습니다.
                        </motion.p>
                    )}
                </AnimatePresence>
                <AnimatePresence>
                    {!loading && !error && displayedResults.map((result, index) => (
                        <motion.div
                            onClick={() => participateChatHandler(result)}
                            key={result.roomId}
                            initial={{ opacity: 0, y: -10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: 10 }}
                            transition={{ duration: 0.3, delay: index * 0.05 }}
                            className={classes.resultItem}
                        >
                            <div className={classes.mainResult}>
                                <p className={classes.title}>{result.name}</p>
                                <p className={classes.desc}>{result.description}</p>
                            </div>
                            <span className={classes.metaItem}>
                                <FiUsers style={{ marginTop:'3px'}}/> {result.currentParticipants}/{result.maxParticipants}
                            </span>
                        </motion.div>
                    ))}
                </AnimatePresence>
                <div ref={observerRef} />
            </div>
            </motion.div>
        </AnimatePresence>
    );
};

export default KeywordRoomForm;