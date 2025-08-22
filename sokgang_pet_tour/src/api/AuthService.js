import axios from "axios";
import { toast } from "react-toastify";

const apiClient = axios.create({
    baseURL: window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api/v1/auth' 
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/api/v1/auth`,
    withCredentials: true,
});

export const checkDuplicateUserIdService = async (userId) => {

    try{
        const checkDuplicateResponse = await apiClient.get(`duplicate?userId=${userId}`);
        return await checkDuplicateResponse.data;
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
}

export const signupService = async (signupRequest) => {
    try {
        const signupResponse = await apiClient.post('/signup', signupRequest);
        return await signupResponse.data;
    } catch (error) {
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

export const signinService = async (signinRequest) => {
    try {
        const signinResponse = await apiClient.post('/login', signinRequest);
        return signinResponse.data;
    } catch (error) {
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


export const findIdService = async (name,email) => {
    try{
        const findIdResponse = await apiClient.get(`/find-id?name=${name}&email=${email}`);
        return await findIdResponse.data;
    }catch (error){
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

export const sendVerifyCodeService = async (userId) => {
    try{
        const sendVerifyCodeResponse = await apiClient.post(`/find-password/certificate?userId=${userId}`);
        return await sendVerifyCodeResponse.data;
    }catch (error){
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

export const isVerifyCodeService = async (userId,code) => {
    try{
        const isVerifyResponse = await apiClient.get(`/find-password/verify?userId=${userId}&code=${code}`);
        return await isVerifyResponse.data;
    }catch (error){
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

export const changePasswordService = async (userId,passwordChangeRequest) => {
    try{
        const changePasswordResponse = await apiClient.patch(`/password?userId=${userId}`, passwordChangeRequest);
        return await changePasswordResponse.data;
    }catch (error){
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

export const reIssueTokenService = async (refreshToken) => {
    try{
        const reissueTokenResponse = await apiClient.post(`/reissue?refreshToken=${refreshToken}`);
        return await reissueTokenResponse.data;
    }catch (error){
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