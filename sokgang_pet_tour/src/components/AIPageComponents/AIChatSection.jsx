import React, { useState, useRef, useEffect } from "react";
import classes from "./AIChatSection.module.css";
import { motion } from "framer-motion";
const AIChatSection = ({ section, onDelete, onEdit, onSetCurrent }) => {
    
    const [editTitle, setEditTitle] = useState(section.title);
    const [isEditing, setIsEditing] = useState(false);
    const inputRef = useRef(null);

    const editTitleChangeHandler = (event) => setEditTitle(event.target.value);

    const editTitleHandler = (currentTitle) => {
        setIsEditing(true);
        setEditTitle(currentTitle);
    };

    const onEditHandler = (id) => {
        onEdit(id, editTitle);
        setIsEditing(false);
    };

    const handleKeyDown = (e, sectionId) => {
        if (e.key === "Enter") {
            onEdit(sectionId, editTitle);
            setIsEditing(false);
        }
    };

    useEffect(() => {
        if (isEditing && inputRef.current) {
            inputRef.current.focus();
        }
    }, [isEditing]);

    return (
        <motion.div 
            whileHover={{ scale : 1.02 }}
            key={section.id} onClick={() => onSetCurrent(section)} className={classes.chatSection}>
            {isEditing ? (
                <input
                    ref={inputRef}
                    value={editTitle}
                    onChange={editTitleChangeHandler}
                    onBlur={() => onEditHandler(section.id)}
                    onKeyDown={(e) => handleKeyDown(e, section.id)}
                />
            ) : (
                <h2>{section.title}</h2>
            )}
            <p>{section.time.split("T")[0].replaceAll('-','.')} 생성</p>
            <div className={classes.buttonContainer}>
                <button onClick={() => editTitleHandler(section.title)}>제목 변경</button>
                <button onClick={() => onDelete(section.id)}>삭제</button>
            </div>
        </motion.div>
    )
};

export default AIChatSection;