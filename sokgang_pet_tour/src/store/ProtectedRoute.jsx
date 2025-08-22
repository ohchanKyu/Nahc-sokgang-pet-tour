import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect, useContext, useRef } from "react";
import { jwtDecode } from "jwt-decode";
import { reIssueTokenService } from "../api/AuthService";
import { toast } from "react-toastify";
import loginContext from "./login-context";
import { getMemberService } from "../api/MemberService";

const FIVE_MIN_MS = 5 * 60 * 1000;

function getExpMs(token) {
    if (!token) return null;
    try {
        const decoded = jwtDecode(token);
        if (!decoded?.exp) return null;
        return decoded.exp * 1000; 
    } catch {
        return null;
    }
}

const ProtectedRoute = ({ children }) => {

    const [isTokenValid, setIsTokenValid] = useState(null);
    const [isRefreshingToken, setIsRefreshingToken] = useState(false);

    const timerRef = useRef(null);
    const refreshingRef = useRef(false);

    const loginCtx = useContext(loginContext);
    const location = useLocation();
    const navigate = useNavigate();

    const accessToken = sessionStorage.getItem("accessToken");
    const refreshToken = sessionStorage.getItem("refreshToken");

    const saveLoginContext = async () => {
        const memberResponseData = await getMemberService();
        const jsonToken = jwtDecode(accessToken);
        if (memberResponseData.success){
            const memberData = memberResponseData.data;
            loginCtx.loginUser({
                name : memberData.name,
                email : memberData.email,
                userId : memberData.userId,
                role : memberData.role,
                createTime : memberData.createTime,
                memberId: jsonToken.key
            })
        };
    };

    const clearTimer = () => {
        if (timerRef.current) {
            clearTimeout(timerRef.current);
            timerRef.current = null;
        }
    };

    const doRefresh = async () => {
        if (refreshingRef.current) return;
        if (!refreshToken) {
            sessionStorage.removeItem("accessToken");
            setIsTokenValid(false);
            return;
        }
        refreshingRef.current = true;
        setIsRefreshingToken(true);
        
        const currentRefresh  = sessionStorage.getItem('refreshToken');
        const reissueTokenResponseData = await reIssueTokenService(currentRefresh);
        if (reissueTokenResponseData.success){
            const newAccessToken = reissueTokenResponseData.data.accessToken;
            const newRefreshToken = reissueTokenResponseData.data.refreshToken;
            sessionStorage.setItem("accessToken", newAccessToken);
            sessionStorage.setItem("refreshToken", newRefreshToken);
            saveLoginContext(newAccessToken);
            setIsTokenValid(true);
        }else{
            toast.warning("로그인 시간이 만료되었습니다. \n 다시 로그인해주세요.", {
                position: "top-center",
                autoClose: 1000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: false,
                draggable: true,
                progress: undefined,
            });
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken"); 
            setIsTokenValid(false);
            navigate('/auth');
        }
        refreshingRef.current = false;
        setIsRefreshingToken(false);    
    }
    
    useEffect(() => {
        clearTimer();
        if (!accessToken) {
            sessionStorage.removeItem("refreshToken");
            setIsTokenValid(false);
            return;
        }
        const expMs = getExpMs(accessToken);
        const now = Date.now();

        if (!expMs) {
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");
            setIsTokenValid(false);
            return;
        }
        saveLoginContext();
        if (expMs <= now) {
            doRefresh();
            return;
        }
        setIsTokenValid(true);
        const delay = expMs - now - FIVE_MIN_MS;
        if (delay <= 0) {
            doRefresh();
        } else {
            timerRef.current = setTimeout(() => {
                doRefresh();
            }, delay);
        }
        return () => clearTimer();
    }, [accessToken, refreshToken, location.pathname]);

    if (isTokenValid === null || isRefreshingToken) {
        return null;
    }

    if (!isTokenValid) {
        return (
            <Navigate
                to='/auth'
                replace
            />
        );
    }

    return children;
};

export default ProtectedRoute;
