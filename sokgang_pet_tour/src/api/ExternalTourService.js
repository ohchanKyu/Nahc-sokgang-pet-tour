import axios from "axios";


const baseURL = `${import.meta.env.VITE_TOUR_BASE_URL}`;
const serverApiClient = axios.create({
    baseURL: window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api/v1/tour/sync' 
    : `${import.meta.env.VITE_DEPLOY_BACKEND_URL}/api/v1/tour/sync`,
    withCredentials: true,
});

const APP_KEY = `${import.meta.env.VITE_TOUR_APP_KEY}`

const BASE_PARAMS = {
  MobileOS: 'ETC',
  MobileApp: 'MyApp',
  _type: 'json',
};

serverApiClient.interceptors.request.use(
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
  
serverApiClient.interceptors.response.use(
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

export const getPetTourSyncList = async () => {

    try{
       
        let uri = baseURL + `/petTourSyncList?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
            ...BASE_PARAMS,
            numOfRows: 300,
            pageNo: 1,
            arrange: 'C',
            listYN: 'Y',
            areaCode: 32,
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
};

export const getPetTourCommonInfo = async (contentId) => {

    try{
        let uri = baseURL + `/detailCommon?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
            ...BASE_PARAMS,
            contentId: contentId,
            defaultYN : 'Y',
            firstImageYN : 'Y',
            areacodeYN : 'Y',
            catcodeYN : 'Y',
            addrinfoYN : 'Y',
            mapinfoYN : 'Y',
            overviewYN : 'Y',
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourAccompanyInfo = async (contentId) => {
 try{
        let uri = baseURL + `/detailPetTour?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
             ...BASE_PARAMS,
            contentId: contentId,
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourImageInfo = async (contentId) => {
 try{
        let uri = baseURL + `/detailImage?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
            ...BASE_PARAMS,
            contentId: contentId,
            imageYN : 'Y',
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourRepeatInfo = async (contentId,contentTypeId) => {
 try{
        let uri = baseURL + `/detailInfo?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
            ...BASE_PARAMS,
            contentId: contentId,
            contentTypeId : contentTypeId,
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourIntroInfo = async (contentId,contentTypeId) => {
 try{
        let uri = baseURL + `/detailIntro?ServiceKey=${APP_KEY}&`;
        const params = new URLSearchParams({
            ...BASE_PARAMS,
            contentId: contentId,
            contentTypeId : contentTypeId,
        }).toString();
        uri = uri + params;
        const response = await serverApiClient.post('/data',{
            uri : uri
        });
        return await response.data;
    }catch(error){
        return error;
    }
}