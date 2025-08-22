import React, { useEffect, useMemo, useRef, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import styles from "./NiceSelect.module.css"; 

const NiceSelect = ({
  options,          
  value,
  onChange,
  placeholder = "선택",
  disabled = false,
  loading = false,
  className = "",    
  ariaLabel,
}) => {
  const [open, setOpen] = useState(false);
  const btnRef = useRef(null);
  const listRef = useRef(null);
  const idxMap = useMemo(() => {
    const m = new Map();
    (options || []).forEach((o, i) => m.set(o.value, i));
    return m;
  }, [options]);

  const activeIndex = idxMap.has(value) ? idxMap.get(value) : -1;
  const label = activeIndex >= 0 ? options[activeIndex].label : placeholder;

  useEffect(() => {
    if (!open) return;
    const onDoc = (e) => {
      if (!btnRef.current || !listRef.current) return;
      if (btnRef.current.contains(e.target) || listRef.current.contains(e.target)) return;
      setOpen(false);
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, [open]);

  const openList = () => !disabled && setOpen(true);
  const closeList = () => setOpen(false);

  const move = (delta) => {
    if (!options?.length) return;
    let i = activeIndex;
    i = i < 0 ? 0 : i + delta;
    if (i < 0) i = options.length - 1;
    if (i >= options.length) i = 0;
    onChange?.(options[i].value);
  };

  const onButtonKey = (e) => {
    if (disabled) return;
    switch (e.key) {
      case "ArrowDown":
      case "ArrowUp":
        e.preventDefault();
        if (!open) setOpen(true);
        move(e.key === "ArrowDown" ? +1 : -1);
        break;
      case "Enter":
      case " ":
        e.preventDefault();
        setOpen((v) => !v);
        break;
      default:
        break;
    }
  };

  const onListKey = (e) => {
    switch (e.key) {
      case "ArrowDown":
        e.preventDefault(); move(+1); break;
      case "ArrowUp":
        e.preventDefault(); move(-1); break;
      case "Enter":
        e.preventDefault(); closeList(); btnRef.current?.focus(); break;
      case "Escape":
        e.preventDefault(); closeList(); btnRef.current?.focus(); break;
      default:
        break;
    }
  };

  return (
    <div className={`${styles.wrap} ${className}`} data-disabled={disabled ? "1" : "0"}>
      <button
        type="button"
        ref={btnRef}
        className={styles.button}
        aria-haspopup="listbox"
        aria-expanded={open}
        aria-label={ariaLabel}
        disabled={disabled}
        onClick={() => setOpen((v) => !v)}
        onKeyDown={onButtonKey}
      >
        <span className={`${styles.label} ${activeIndex < 0 ? styles.placeholder : ""}`}>
          {label}
        </span>
        <span className={styles.chev} aria-hidden />
        {loading && <span className={styles.spin} aria-hidden />}
      </button>

      <AnimatePresence>
        {open && (
          <motion.ul
            ref={listRef}
            className={styles.list}
            role="listbox"
            tabIndex={-1}
            initial={{ opacity: 0, y: 6 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 6 }}
            onKeyDown={onListKey}
          >
            {(options || []).map((o, i) => (
              <li
                key={o.value}
                role="option"
                aria-selected={value === o.value}
                className={`${styles.option} ${value === o.value ? styles.active : ""}`}
                onMouseDown={(e) => e.preventDefault()}
                onClick={() => { onChange?.(o.value); closeList(); btnRef.current?.focus(); }}
              >
                {o.label}
              </li>
            ))}
          </motion.ul>
        )}
      </AnimatePresence>
    </div>
  );
};

export default NiceSelect;
