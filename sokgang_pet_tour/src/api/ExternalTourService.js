import axios from "axios";

const apiClient = axios.create({
    baseURL : `${import.meta.env.VITE_TOUR_BASE_URL}`,
});
const APP_KEY = `${import.meta.env.VITE_TOUR_APP_KEY}`

const BASE_PARAMS = {
  MobileOS: 'ETC',
  MobileApp: 'AppTest',
  _type: 'json',
};

export const getPetTourSyncList = async () => {

    try{
        const response = await apiClient.get(`/petTourSyncList?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                numOfRows: 300,
                pageNo: 1,
                arrange: 'C',
                listYN: 'Y',
                areaCode: 32,
            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
};

export const getPetTourCommonInfo = async (contentId) => {

    try{
        const response = await apiClient.get(`/detailCommon?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                contentId: contentId,
                defaultYN : 'Y',
                firstImageYN : 'Y',
                areacodeYN : 'Y',
                catcodeYN : 'Y',
                addrinfoYN : 'Y',
                mapinfoYN : 'Y',
                overviewYN : 'Y',
            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourAccompanyInfo = async (contentId) => {
 try{
        const response = await apiClient.get(`/detailPetTour?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                contentId: contentId,
            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourImageInfo = async (contentId) => {
 try{
        const response = await apiClient.get(`/detailImage?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                contentId: contentId,
                imageYN : 'Y',

            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourRepeatInfo = async (contentId,contentTypeId) => {
 try{
        const response = await apiClient.get(`/detailInfo?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                contentId: contentId,
                contentTypeId : contentTypeId,
            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
}

export const getPetTourIntroInfo = async (contentId,contentTypeId) => {
 try{
        const response = await apiClient.get(`/detailIntro?serviceKey=${APP_KEY}`, {
            params: {
                ...BASE_PARAMS,
                contentId: contentId,
                contentTypeId : contentTypeId,
            },
        });
        return await response.data;
    }catch(error){
        return error;
    }
}