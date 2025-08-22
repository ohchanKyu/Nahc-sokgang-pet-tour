import { useState, useEffect } from "react";

export const useDebounce = (v, delay = 350) => {
  const [val, setVal] = useState(v);
  useEffect(() => {
    const t = setTimeout(() => setVal(v), delay);
    return () => clearTimeout(t);
  }, [v, delay]);
  return val;
};
