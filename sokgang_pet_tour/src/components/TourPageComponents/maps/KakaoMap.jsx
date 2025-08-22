import React,{ useEffect, useState, useRef } from "react";
import classes from "./KakaoMap.module.css";
import { Map } from "react-kakao-maps-sdk";
import { toast } from "react-toastify";
import { getTourPlacesService } from "../../../api/TourService";
import { getVetPlacesService } from "../../../api/VetService";
import { findNearbyMarkers, clusterMarkers } from "./Clustering";
import ClusterMarker from "./ClusterMarker";
import { AnimatePresence } from "framer-motion";
import { decorateTour } from "../ConvertEntity";
import EventMarkerContainer from "./EventMarkerContainer";

const DEFAULT_CENTER_GANGWON = { lat: 38.1904539080119, lng: 128.60441659054428 };
const defaultLevel = 5;
const maxLevel = 6;

const { kakao } = window;
const CATEGORY_LABEL = { HOSPITAL: "동물병원", PHARMACY: "동물약국" };

const getDistance = (level) => {
  if (level <= 3) return 1;
  if (level <= 5) return 2;
  if (level <= 6) return 4;
  if (level <= 7) return 7;
  if (level <= 8) return 14;
  if (level <= 9) return 21;
  if (level <= 10) return 30;
  if (level <= 11) return 40;
  return 120;
};

const getCellSize = (level) => {
  if (level === 6) return 0.02;
  if (level === 7) return 0.04;
  if (level === 8) return 0.08;
  if (level === 9) return 0.2;
  if (level === 10) return 0.5;
  if (level === 11) return 0.8;
  return 1.6;
};

const KakaoMap = (props) => {

  const mapRef = useRef(null);
  const [level, setLevel] = useState(defaultLevel);    
  const [center, setCenter] = useState(DEFAULT_CENTER_GANGWON);
  const [places, setPlaces] = useState([]);
  const [clusterGroup, setClusterGroup] = useState([]);
  const [visiblePlaces, setVisiblePlaces] = useState([]);

  const handleDatas = async () => {
    const savedData = sessionStorage.getItem("mapData");
    if (savedData){
        const parsedData = JSON.parse(savedData);
        setTimeout(() => {
          setPlaces(parsedData);
        }, 500)
        
    }else{
      const tourResponse = await getTourPlacesService();
      const vetResponse = await getVetPlacesService();
      if (tourResponse.success && vetResponse.success) {
        let fetchData = [];
        const converted = decorateTour(tourResponse.data);
        const vetResponseData = vetResponse.data;
        converted.forEach(item => {
            const id = item.contentId;
            const cat = item.typeName;
            const address = item.address;
            const contentType = "Tour";
            const name = item.title;
            const latitude = item.latitude;
            const longitude = item.longitude;
            fetchData.push({
                id,cat,address,contentType,name,latitude,longitude
            });
        })
        vetResponseData.forEach(item => {
            const id = item.id;
            const cat = CATEGORY_LABEL[item.category]
            const address = item.address;
            const contentType = "Vet"
            const name = item.placeName
            const latitude = item.latitude;
            const longitude = item.longitude;
            fetchData.push({
                id,cat,address,contentType,name,latitude,longitude
              });
        });
        setPlaces(fetchData);
        sessionStorage.setItem("mapData", JSON.stringify(fetchData));
      } else {
        toast.error(`일시적 오류입니다.\n${tourResponse.message}`, { position: "top-center", autoClose: 2000 });
      }
    }
  }

  useEffect(() => {
        if (mapRef.current && places.length > 0) {
            reloadMarkers();
        }
  }, [places]);

  useEffect(() => {
      const map = mapRef.current;
      if (!map) return;
      const onIdle = () => {
          setLevel(map.getLevel());
          reloadMarkers();
      };
      kakao.maps.event.addListener(map, 'idle', onIdle);
      return () => {
          kakao.maps.event.removeListener(map, 'idle', onIdle);
      };
  }, [level]);

  const reloadMarkers = () => {
    const map = mapRef.current;
    if (!map) return;
    const position = map.getCenter();
    const lv = map.getLevel();
    const distance = getDistance(lv);

    const nearbyMarker = findNearbyMarkers({
      markers: places,
      latitude: position.getLat(),
      longitude: position.getLng(),
      maxDistance: distance,
    });

    if (lv >= maxLevel) {
      const clustered = clusterMarkers(nearbyMarker, getCellSize(lv));
      setClusterGroup(clustered);
      setVisiblePlaces([]);
    } else {
      setClusterGroup([]);
      setVisiblePlaces(nearbyMarker);
    }
  };

  useEffect(() => {
    const fetchLocation = async () => {
      const lat = Number(props?.location?.latitude);
      const lng = Number(props?.location?.longitude);
      if (Number.isNaN(lat) && Number.isNaN(lng)) setLevel(defaultLevel);
      else setLevel(5);
      setCenter(Number.isFinite(lat) || Number.isFinite(lng) ? { lat, lng } : DEFAULT_CENTER_GANGWON);
      await handleDatas();
    }
    fetchLocation();
  }, [props.location]);

  return (
    <>
      <Map
        id="map"
        className={classes.container}
        center={center}       
        level={level}      
        isPanto={true}      
        onZoomChanged={(map) => setLevel(map.getLevel())}
        zoomable={true}
        ref={mapRef}
      >
        <AnimatePresence>
            {visiblePlaces.map((placeData, index) => (
                <EventMarkerContainer
                    key={`${placeData.id}-${props.id === placeData.id}`}
                    onSelect={props.onSelect}
                    flag={props.id === placeData.id}
                    position={{ lat: placeData.latitude, lng: placeData.longitude }}
                    placeData={placeData}/>
            ))}
        </AnimatePresence>
        <AnimatePresence>
            {clusterGroup.map((cluster, index) => (
                <ClusterMarker 
                    onClick={() => {
                        const map = mapRef.current;
                        if (map.getLevel() > 3) map.setLevel(map.getLevel() - 1, { animate: true });
                        map.setCenter(new kakao.maps.LatLng(cluster.centerLatitude, cluster.centerLongitude));
                    }}
                    key={`cluster-${index}`} cluster={cluster} />
            ))}
        </AnimatePresence>
      </Map>
    </>
  );
};

export default KakaoMap;
