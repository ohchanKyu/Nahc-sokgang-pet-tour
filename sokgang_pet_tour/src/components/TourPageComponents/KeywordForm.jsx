import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { getTourPlacesWithKeywordService } from '../../api/TourService';
import classes from './KeywordForm.module.css';
import { decorateTour } from './ConvertEntity';
import { FaLocationDot } from "react-icons/fa6";

const useDebounce = (value, delay) => {
  const [debounced, setDebounced] = useState(value);
  useEffect(() => { const t = setTimeout(() => setDebounced(value), delay); return () => clearTimeout(t); }, [value, delay]);
  return debounced;
};

const KeywordForm = (props) => {

  const [query, setQuery] = useState('');
  const debouncedQuery = useDebounce(query, 300);
  const [results, setResults] = useState([]);
  const [displayed, setDisplayed] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [error, setError] = useState(null);
  const observerRef = useRef(null);
  const inputRef = useRef(null);
  const [activeIndex, setActiveIndex] = useState(-1);

  const goPlaceDetailHandler = (contentInfo) => {
      props.onDetail(contentInfo,"tour");
      clearQuery();
  };

  useEffect(() => {
    const fetchData = async () => {
      const q = debouncedQuery.trim();
      if (!q) {
        setResults([]); setDisplayed([]); setPage(1); setError(null); setActiveIndex(-1);
        return;
      }
      setLoading(true); setError(null); setActiveIndex(-1); setPage(1);
      try {
        const res = await getTourPlacesWithKeywordService(q);
        if (res?.success) {
          const conv = decorateTour(res.data || []);
          setResults(conv);
          setDisplayed(conv.slice(0, 20));
        } else {
          setResults([]); setDisplayed([]); setError('검색 실패');
        }
      } catch {
        setResults([]); setDisplayed([]); setError('네트워크 오류');
      }
      setLoading(false);
    };
    fetchData();
  }, [debouncedQuery]);

  useEffect(() => {
    if (results.length === 0) return;
    const obs = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && displayed.length < results.length) {
        const next = page + 1;
        setPage(next);
        setDisplayed(results.slice(0, next * 20));
      }
    }, { threshold: 1.0 });
    if (observerRef.current) obs.observe(observerRef.current);
    return () => obs.disconnect();
  }, [results, displayed, page]);

  const showDropdown = displayed.length > 0 && !error;
  const clearQuery = () => { setQuery(''); setResults([]); setDisplayed([]); setActiveIndex(-1); inputRef.current?.focus(); };

  const onKeyDown = (e) => {
    if (!showDropdown) return;
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setActiveIndex((i) => Math.min(i + 1, displayed.length - 1));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setActiveIndex((i) => Math.max(i - 1, 0));
    } else if (e.key === 'Enter') {
      if (activeIndex >= 0) {
        e.preventDefault();
        goPlaceDetailHandler(displayed[activeIndex]);
      }
    } else if (e.key === 'Escape') {
      clearQuery();
    }
  };

  return (
    <div className={classes.searchContainer}>
      <div className={classes.inputWrap} data-loading={loading ? 'true' : 'false'}>
        <span className={classes.iconSearch} aria-hidden />
        <input
          ref={inputRef}
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={onKeyDown}
          placeholder="장소, 상호명을 검색하세요"
          className={classes.searchInput}
          aria-autocomplete="list"
          aria-expanded={showDropdown}
        />
      </div>

      <AnimatePresence>
        {showDropdown && (
          <motion.div
            className={classes.dropdown}
            initial={{ opacity: 0, y: -4 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -4 }}
            transition={{ duration: 0.18 }}
          >
            <div className={classes.list} role="listbox">
              {displayed.map((r, idx) => (
                <div
                  key={r.contentId}
                  role="option"
                  aria-selected={activeIndex === idx}
                  className={`${classes.item} ${activeIndex === idx ? classes.active : ''}`}
                  onMouseDown={() => goPlaceDetailHandler(r)}
                  onMouseEnter={() => setActiveIndex(idx)}
                >
                  <div className={classes.itemIcon}><FaLocationDot/></div>
                  <div className={classes.itemBody}>
                    <div className={classes.itemTitle}>{r.title}</div>
                    <div className={classes.itemMeta}>
                      <span>{r.cat1Name}</span>
                      <span className={classes.dot}>·</span>
                      <span>{r.cat2Name}</span>
                    </div>
                    <div className={classes.itemAddr}>{r.address}</div>
                  </div>
                </div>
              ))}
              <div ref={observerRef} />
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default KeywordForm;
