import { useEffect, useMemo, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import classes from "./FilterForm.module.css";
import {
  getTourPlacesByFilterService,
  getTourCategoryService,
} from "../../api/TourService";
import { decorateTour } from "../TourPageComponents/ConvertEntity";
import { ConvertCategory, ConvertSigungu } from "../TourPageComponents/ConvertEntity";
import NiceSelect from "../LayoutComponents/NiceSelect";
import { toast } from "react-toastify";
import TourContentList from "./TourContentList";
import { RiResetLeftLine } from "react-icons/ri";

const FilterForm = ({ onSelect }) => {

  const [cat1, setCat1] = useState("");
  const [cat2, setCat2] = useState("");
  const [cat3, setCat3] = useState("");
  const [sigunguCode, setSigunguCode] = useState("0");

  const [cat2List, setCat2List] = useState([]);
  const [cat3List, setCat3List] = useState([]);
  const [loading2, setLoading2] = useState(false);
  const [loading3, setLoading3] = useState(false);

  const [results, setResults] = useState([]);
  const [showResults, setShowResults] = useState(false);

  const resetFilter = () => {
    setShowResults(false);
    setResults([]);
    setCat1('');
    setCat2('');
    setCat3('');
    setSigunguCode("0");
  };
  const cat1List = ["A01", "A02", "A03", "A04", "A05", "B02"];

  const sigunguOptions = useMemo(() => {
    const entries = [
      ["0", "전체"],
      ["1", ConvertSigungu(1)],
      ["2", ConvertSigungu(2)],
      ["3", ConvertSigungu(3)],
      ["4", ConvertSigungu(4)],
      ["5", ConvertSigungu(5)],
      ["6", ConvertSigungu(6)],
      ["7", ConvertSigungu(7)],
      ["8", ConvertSigungu(8)],
      ["9", ConvertSigungu(9)],
      ["10", ConvertSigungu(10)],
    ];
    return entries.map(([value, label]) => ({ value, label }));
  }, []);

  useEffect(() => {
    if (!cat1) {
      setCat2("");
      setCat3("");
      setCat2List([]);
      setCat3List([]);
      return;
    }
    setCat2("");
    setCat3("");
    setCat3List([]);
    setLoading2(true);
    getTourCategoryService(1, cat1, "")
      .then((res) => {
        const list = Array.isArray(res?.data) ? res.data : res;
        const arr = (list || [])
          .map((code) => ({ value: code, label: ConvertCategory(code) }))
          .filter((o) => o.label !== "알 수 없음");
        setCat2List(arr);
      })
      .finally(() => setLoading2(false));
  }, [cat1]);

  useEffect(() => {
    if (!cat1 || !cat2) {
      setCat3("");
      setCat3List([]);
      return;
    }
    setCat3("");
    setLoading3(true);
    getTourCategoryService(2, cat1, cat2)
      .then((res) => {
        const list = Array.isArray(res?.data) ? res.data : res;
        const arr = (list || [])
          .map((code) => ({ value: code, label: ConvertCategory(code) }))
          .filter((o) => o.label !== "알 수 없음");
        setCat3List(arr);
      })
      .finally(() => setLoading3(false));
  }, [cat1, cat2]);

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!cat1){
      toast.warning("대분류는 필수 항목입니다.", { position: "top-center", autoClose: 800 });
      return;
    }
    const resp = await getTourPlacesByFilterService(
      cat1,
      cat2 || "",
      cat3 || "",
      Number(sigunguCode)
    );
    const payload = resp?.data ?? resp;
    const converted = decorateTour(payload);
    setResults(Array.isArray(converted) ? converted : []);
    setShowResults(true);
  };

  const container = {
    hidden: { opacity: 0, y: 10 },
    show: { opacity: 1, y: 0, transition: { staggerChildren: 0.05 } },
    exit: { opacity: 0, y: -10 },
  };
  const item = { hidden: { opacity: 0, y: 8 }, show: { opacity: 1, y: 0 } };

  return (
    <div className={classes.panelRoot}>
      <AnimatePresence mode="wait">
        {!showResults && (
          <motion.form
            key="filter"
            className={classes.form}
            onSubmit={onSubmit}
            initial="hidden"
            animate="show"
            exit="exit"
            variants={container}
          >
            <motion.div className={classes.field} variants={item}>
              <label className={classes.label}>대분류</label>
              <div className={`${classes.selectWrap} ${!cat1 ? classes.required : ""}`}>
                 <NiceSelect
                    ariaLabel="대분류"
                    options={[{value:"", label:"선택"}, ...cat1List.map(code => ({value: code, label: ConvertCategory(code)}))]}
                    value={cat1}
                    onChange={setCat1}
                    placeholder="선택"
                  />
              </div>
            </motion.div>

            <motion.div className={classes.field} variants={item}>
              <label className={classes.label}>중분류</label>
              <div className={classes.selectWrap} data-loading={loading2 ? "true" : "false"}>
                <NiceSelect
                  ariaLabel="중분류"
                  options={[
                    { value: "", label: loading2 ? "불러오는 중..." : "전체" },
                    ...cat2List, 
                  ]}
                  value={cat2}
                  onChange={setCat2}
                  disabled={!cat1 || loading2}
                  loading={loading2}
                  placeholder={loading2 ? "불러오는 중..." : "전체"}
                />
              </div>
            </motion.div>

            <motion.div className={classes.field} variants={item}>
              <label className={classes.label}>소분류</label>
              <div className={classes.selectWrap} data-loading={loading3 ? "true" : "false"}>
                <NiceSelect
                  ariaLabel="소분류"
                  options={[
                    { value: "", label: loading3 ? "불러오는 중..." : "전체" },
                    ...cat3List,
                  ]}
                  value={cat3}
                  onChange={setCat3}
                  disabled={!cat1 || !cat2 || loading3}
                  loading={loading3}
                  placeholder={loading3 ? "불러오는 중..." : "전체"}
                />
              </div>
            </motion.div>
            <motion.div className={classes.field} variants={item}>
              <label className={classes.label}>시군구</label>
              <div className={classes.selectWrap}>
                <NiceSelect
                  ariaLabel="시군구"
                  options={sigunguOptions}
                  value={sigunguCode}
                  onChange={setSigunguCode}
                  placeholder="전체"
                />
              </div>
            </motion.div>


            <motion.div className={classes.actions} variants={item}>
              <motion.button
                type="submit"
                className={classes.submit}
                whileHover={{ scale : 1.03 }}
              >
                검색하기
              </motion.button>
            </motion.div>
          </motion.form>
        )}
      </AnimatePresence>
      <AnimatePresence mode="wait">
        {showResults && (
          <motion.div
            key="results"
            initial={{ opacity: 0, y: 8 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 8 }}
          >
            <div className={classes.resultsHeader}>
              <div className={classes.resultCount}>
                총 {results.length.toLocaleString()}건
              </div>
              <button
                className={classes.backBtn}
                onClick={resetFilter}
              >
                 <span>다시 분류하기</span> <RiResetLeftLine/>
              </button>
            </div>
            <TourContentList filterPlaces={results} onSelect={onSelect} type="filter"/>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default FilterForm;
