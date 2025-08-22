import axios from "axios";
import { toast } from "react-toastify";
import { reIssueTokenService } from "./AuthService";

const apiClient = axios.create({
    baseURL: window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api/v1/tour/sync' 
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/api/v1/tour/sync`,
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

export const saveSyncDataService = async (lists) => {

    try{
        const syncResponse = await apiClient.post('',lists);
        return await syncResponse.data;
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

export const saveOverviewDataService = async (contentId, overview) => {

    try{
        const syncResponse = await apiClient.post(`/common/${contentId}`,{
            overview : overview
        });
        return await syncResponse.data;
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

export const savePetDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/pet/${contentId}`,list);
        return await syncResponse.data;
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

export const saveImageDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/image/${contentId}`,list);
        return await syncResponse.data;
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

export const saveRepeatCommonDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/repeat/common/${contentId}`,list);
        return await syncResponse.data;
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

export const saveRepeatRoomDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/repeat/room/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroCultureDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/culture/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroFoodDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/food/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroLeportsDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/leports/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroLodgingDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/lodging/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroShoppingDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/shopping/${contentId}`,list);
        return await syncResponse.data;
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

export const saveIntroTouristDataService = async (contentId, list) => {

    try{
        const syncResponse = await apiClient.post(`/intro/tourist/${contentId}`,list);
        return await syncResponse.data;
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