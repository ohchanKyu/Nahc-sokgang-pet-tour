import axios from "axios";
import { toast } from "react-toastify";
import { reIssueTokenService } from "./AuthService";

const apiClient = axios.create({
    baseURL: window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api/v1/chatRooms' 
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/api/v1/chatRooms`,
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
            return apiClient.request(error.config);
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


export const createChatRoomService = async (chatRoomCreateRequest) => {

    try{
        const chatRoomResponse = await apiClient.post("",chatRoomCreateRequest);
        return await chatRoomResponse.data;
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

export const getAllChatRoomService = async () => {
  try {
    const chatRoomResponse = await apiClient.get("");
    return chatRoomResponse.data;
  } catch (error) {
    if (error.response) return error.response.data;
    toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, { position: "top-center", autoClose: 2000 });
    return { success: false };
  }
};

export const getMyChatRoomService = async () => {

    try{
        const chatRoomResponse = await apiClient.get("/me",);
        return await chatRoomResponse.data;
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

export const getMyManagerChatRoomService = async () => {

    try{
        const chatRoomResponse = await apiClient.get("/manager",);
        return await chatRoomResponse.data;
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

export const getChatRoomByKeywordService = async (keyword) => {
  try {
    const chatRoomResponse = await apiClient.get("/search", {
      params: { keyword }
    });
    return chatRoomResponse.data;
  } catch (error) {
    if (error.response) return error.response.data;
    toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, { position: "top-center", autoClose: 2000 });
    return { success: false };
  }
};

export const getisJoinChatRoomService = async (roomId) => {

    try{
        const chatRoomResponse = await apiClient.get(`/join/${roomId}`,);
        return await chatRoomResponse.data;
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

export const joinChatRoomService = async (roomId, nickname) => {
  try {
    const chatRoomResponse = await apiClient.post(`/join/${roomId}`, null, {
      params: { nickname }
    });
    return chatRoomResponse.data;
  } catch (error) {
    if (error.response) return error.response.data;
    toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, { position: "top-center", autoClose: 2000 });
    return { success: false };
  }
};

export const leaveChatRoomService = async (roomId) => {
  try {
    const chatRoomResponse = await apiClient.delete(`/join/${roomId}`);
    return chatRoomResponse.data;
  } catch (error) {
    if (error.response) return error.response.data;
    toast.error(`일시적 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.`, { position: "top-center", autoClose: 2000 });
    return { success: false };
  }
};
