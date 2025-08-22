import React, { useMemo, useState } from "react";
import { getTourPlacesByRecommend } from "../../api/TourService";
import classes from "./TourRecommend.module.css";
import { ConvertContentType } from "../TourPageComponents/ConvertEntity";
import NiceSelect from "../LayoutComponents/NiceSelect";
import { getCoordinateService } from "../../api/KakaoLocation";
import Post from "./Post";
import { toast } from "react-toastify";
import Modal from "../LayoutComponents/Modal";
import TourRecommandResult from "./TourRecommandResult";
import { motion, AnimatePresence } from "framer-motion";
import { RiResetLeftLine } from "react-icons/ri";

const CONTENT_TYPE_IDS = ["12", "14", "15", "28", "32", "38", "39"];

const DEFAULT_FORM = {
  startLatitude: 38.189651793,
  startLongitude: 128.604238011,
  address : '강원도 지역의 주소를 입력해주세요.',
  days: 2,
  travelMode: "DRIVE",
  dayStartHour: 9,
  dailyTimeLimitMin: 480,
  maxRadiusKm: 30,
  maxStopsPerDay: 7,
  keywords: "",
  excludeContentTypeIds: [],
};

const TourRecommand = () => {

  const [form, setForm] = useState(DEFAULT_FORM);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [postPopup,setPostPopup] = useState(false);
  const [results, setResults] = useState([]);
  const [showResults, setShowResults] = useState(false);

  const resetFilter = () => {
    setForm(DEFAULT_FORM);
    setResults([]);
    setError("");
    setShowResults(false);
  };

    const popupOverlay = () => {
        setPostPopup(true);
    };

    const popupDown = () => {
        setPostPopup(false);
    };

    const onComplete = async (data) =>{
      let fullAddress = data.address;
      let extraAddress = '';

      if (data.addressType === 'R') {
          if (data.bname !== '') {
              extraAddress += data.bname;
          }
          if (data.buildingName !== '') {
              extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
          }
          fullAddress += (extraAddress !== '' ? ` (${extraAddress})` : '');
      }
      popupDown();
      const coordinateResponse = await getCoordinateService(fullAddress);
      if (coordinateResponse.success){
          const latitude = coordinateResponse.data.latitude;
          const longitude = coordinateResponse.data.longitude;
          setForm((f) => ({
            ...f,
            startLatitude : latitude,
            startLongitude : longitude,
            address : fullAddress
          }));
      }else{
          const errorMessage = coordinateResponse.message
          toast.error(`일시적 오류입니다. \n ${errorMessage}`, {
              position: "top-center",
              autoClose: 2000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
          });
      }
  }
  const onChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((f) => ({
      ...f,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const toggleExclude = (id) => {
    setForm((f) => {
      const set = new Set(f.excludeContentTypeIds || []);
      if (set.has(id)) set.delete(id);
      else set.add(id);
      return { ...f, excludeContentTypeIds: Array.from(set) };
    });
  };

  const payload = useMemo(() => {
    const p = {
      startLatitude: Number(form.startLatitude),
      startLongitude: Number(form.startLongitude),
      days: Number(form.days),
      travelMode: form.travelMode,
      includeFoodStops: !(form.excludeContentTypeIds || []).includes("39"),
      dayStartHour: Number(form.dayStartHour),
      dailyTimeLimitMin: Number(form.dailyTimeLimitMin),
      maxRadiusKm: Number(form.maxRadiusKm),
      keywords: form.keywords
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean),
    };
    if (form.maxStopsPerDay) p.maxStopsPerDay = Number(form.maxStopsPerDay);
    if (form.excludeContentTypeIds?.length) {
      p.excludeContentTypeIds = form.excludeContentTypeIds;
    }
    return p;
  }, [form]);

  const onSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setResults(null);
    const recommandResponse = await getTourPlacesByRecommend(payload);
    if (recommandResponse.success){
      setResults(recommandResponse.data);
    }else{
      setError(recommandResponse.message || "추천을 불러오지 못했습니다.");
    }
    setShowResults(true);
    setLoading(false);
  };

  const container = {
    hidden: { opacity: 0, y: 10 },
    show: { opacity: 1, y: 0, transition: { staggerChildren: 0.05 } },
    exit: { opacity: 0, y: -10 },
  };

  return (
    <div className={classes.container}>
      {postPopup && (
            <Modal 
                onClose={popupDown}>
                <Post onComplete={onComplete}/>
            </Modal>
        )
      }
      <AnimatePresence mode="wait">
         {!showResults && (
            <motion.form
                key="recommand"
                className={classes.form}
                onSubmit={onSubmit}
                initial="hidden"
                animate="show"
                exit="exit"
                variants={container}
              >
                    <div className={classes.flexSection}>
                        <input
                          className={classes.addressInput}
                          type="text"
                          name="address"
                          value={form.address}
                          onChange={onChange}
                          disabled
                          required
                        />
                        <motion.button 
                          whileHover={{ scale : 1.03 }}
                          className={classes.postButton} type='button' onClick={popupOverlay}>주소 검색</motion.button>
                    </div>
                    <div className={classes.flexSection}>
                      <input
                        className={classes.keywordInput}
                        type="text"
                        name="keywords"
                        placeholder="키워드를 입력해주세요. 예시) 시장, 해변"
                        value={form.keywords}
                        onChange={onChange}
                      />
                    </div>
                    <div className={classes.flexSection}>
                      <div className={classes.formRow}>
                        <label className={classes.label}>여행 일수</label>
                        <input
                          className={classes.input}
                          type="number"
                          min="2"
                          max="5"
                          name="days"
                          value={form.days}
                          onChange={onChange}
                        />
                      </div>
                      <div className={classes.formRow}>
                        <label className={classes.label}>이동 수단</label>
                        <NiceSelect
                          ariaLabel="이동 수단"
                          options={[
                            { value: "",        label: "선택" },
                            { value: "WALK",    label: "도보" },
                            { value: "TRANSIT", label: "대중교통" },
                            { value: "DRIVE",   label: "자동차" },
                          ]}
                          value={form.travelMode ?? ""}
                          onChange={(v) =>
                            onChange({ target: { name: "travelMode", value: typeof v === "string" ? v : v?.value ?? "" } })
                          }
                          placeholder="선택"
                        />
                      </div>
                      <div className={classes.formRow}>
                        <label className={classes.label}>최대 반경 (km)</label>
                        <input
                          className={classes.input}
                          type="number"
                          min="0.1"
                          step="0.1"
                          name="maxRadiusKm"
                          value={form.maxRadiusKm}
                          onChange={onChange}
                        />
                      </div>
                    </div>
                    <div className={classes.flexSection}>
                      <div className={classes.formRow}>
                        <label className={classes.label}>일일 시간(분)</label>
                        <input
                          className={classes.input}
                          type="number"
                          min="60"
                          max="1440"
                          name="dailyTimeLimitMin"
                          value={form.dailyTimeLimitMin}
                          onChange={onChange}
                        />
                      </div>
                      <div className={classes.formRow}>
                        <label className={classes.label}>시작 시각(시)</label>
                        <input
                          className={classes.input}
                          type="number"
                          min="0"
                          max="23"
                          name="dayStartHour"
                          value={form.dayStartHour}
                          onChange={onChange}
                        />
                      </div>
                      <div className={classes.formRow}>
                        <label className={classes.label}>일자당 최대 방문지</label>
                        <input
                          className={classes.input}
                          type="number"
                          min="1"
                          max="30"
                          name="maxStopsPerDay"
                          value={form.maxStopsPerDay}
                          onChange={onChange}
                        />
                      </div>
                    </div>
                    <div className={classes.formRowWide}>
                      <div className={classes.label}>제외할 유형 선택</div>
                      <div className={classes.checkboxGroup}>
                        {CONTENT_TYPE_IDS.map((id) => (
                          <label key={id} className={classes.checkboxItem}>
                            <input
                              type="checkbox"
                              checked={form.excludeContentTypeIds?.includes(id)}
                              onChange={() => toggleExclude(id)}
                            />
                            <span className={classes.cbLabel}>{ConvertContentType(Number(id))}</span>
                          </label>
                        ))}
                      </div>
                    </div>
                    <motion.button 
                      whileHover={{ scale : 1.03 }}
                      className={classes.button} type="submit" disabled={loading}>
                      {loading ? "추천 생성 중..." : "추천 받기"}
                    </motion.button>
            </motion.form>
         )}
      </AnimatePresence>
      <AnimatePresence mode="wait">
          {showResults && (
              <motion.div
                key="recommandResults"
              >
                <div className={classes.results}>
                  {error && <div className={classes.error}>{error}</div>}
                  {loading && (
                    <div className={classes.loadingWrap}>
                      <div className={classes.spinner} />
                      <div>경로를 계산하고 있습니다...</div>
                    </div>
                  )}
                  {!loading && !error && results?.days?.length === 0 && (
                     <div className={classes.empty}>
                      주변에서 추천할 코스를 찾지 못했어요.<br/>
                      반경(km)을 늘리거나 출발지를 다시 선택해 주세요.<br/>
                      제외한 유형을 줄이거나 키워드를 비워두면 결과가 늘어납니다.
                    </div>
                  )}
                  {!loading && results?.days?.length > 0 && <TourRecommandResult days={form.days} result={results} />}
                  <div className={classes.buttonContainer}>
                    <button
                      className={classes.backBtn}
                      onClick={resetFilter}
                    >
                        <span>다시 추천받기</span> <RiResetLeftLine/>
                    </button>
                  </div>
                </div>
              </motion.div>
          )}
      </AnimatePresence>
    </div>
  );
};

export default TourRecommand;