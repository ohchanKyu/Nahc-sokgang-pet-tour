import axios from "axios";
import { toast } from "react-toastify";
import { reIssueTokenService } from "./AuthService";

const apiClient = axios.create({
    baseURL: window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api/v1/tour' 
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/api/v1/tour`,
    withCredentials: true,
});

apiClient.interceptors.request.use(
    (config) => {
      const accessToken = sessionStorage.getItem('accessToken');
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }else{
        toast.warning("세션이 만료되었습니다. \n 다시 로그인해주세요.", {
            position: "top-center",
            autoClose: 1000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: false,
            draggable: true,
            progress: undefined,
        });
        setTimeout(() => {
            window.location.href = '/auth';
        },2000)
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
);
  
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.data['code']) {
      const tokenErrorCode = error.response.data['code'];
      if (tokenErrorCode !== 'A003') return Promise.reject(error);
      
      if (tokenErrorCode === 'A003') {
        const refreshToken = sessionStorage.getItem('refreshToken');
        const reissueTokenResponseData = await reIssueTokenService(refreshToken);
        if (reissueTokenResponseData.success){
            const newAccessToken = reissueTokenResponseData.data.accessToken;
            const newRefreshToken = reissueTokenResponseData.data.refreshToken;
            sessionStorage.setItem("accessToken", newAccessToken);
            sessionStorage.setItem("refreshToken", newRefreshToken);
            error.config.headers.Authorization = `Bearer ${newAccessToken}`;
            return axios(error.config);
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
            setTimeout(() => {
                window.location.href = '/auth';
            },2000)
        }
      } else {
        toast.error("인증에 문제가 생겼습니다. \n 다시 로그인해주세요.", {
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
        setTimeout(() => {
            window.location.href = '/auth';
        },2000)
      }
    }
    return Promise.reject(error);
  }
);

export const getTourPlacesService = async () => {

    try{
        const placeResponse = await apiClient.get('/places');
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlaceDetailService = async (placeId) => {

    try{
        const placeResponse = await apiClient.get(`/place/${placeId}`);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlacesWithKeywordService = async (keyword) => {

    try{
        const placeResponse = await apiClient.get(`/place/search?keyword=${keyword}`);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlacesByDistService = async (latitude,longitude) => {

    try{
        const placeResponse = await apiClient.get(`/place/dist?latitude=${latitude}&longitude=${longitude}`);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlacesByFilterService = async (cat1,cat2,cat3,sigunguCode) => {

    try{
        const placeResponse = await apiClient.get(`/place/filter?cat1=${cat1}&cat2=${cat2}&cat3=${cat3}&sigunguCode=${sigunguCode}`);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourCategoryService = async (type,cat1,cat2) => {

    try{
        const placeResponse = await apiClient.get(`/category/${type}?cat1=${cat1}&cat2=${cat2}`);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlacesByTodayCount = async () => {

    try{
        const placeResponse = await apiClient.get('/place/count');
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};

export const getTourPlacesByRecommend = async (itineraryRequest) => {

    try{
        const placeResponse = await apiClient.post('/place/recommend',itineraryRequest);
        return await placeResponse.data;
    }catch(error){
        if (error.response){
            return error.response.data;
        }
        toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
        return { success : false }
    }
};