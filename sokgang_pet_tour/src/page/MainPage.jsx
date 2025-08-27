import React from "react";
import Footer from "../components/LayoutComponents/Footer";
import Header from "../components/LayoutComponents/Header";
import classes from "./MainPage.module.css";
import mainImg from "../assets/mainImg.png";
import tourImg from "../assets/tour.png";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import tourRecommendImg from "../assets/tourRecommend.png";

import tourRecommendResultImg from "../assets/tourRecommendResult.png";
import chatBotImg from "../assets/chatBot.png";
import chatImg from "../assets/chatImg.png";

const MockIcon = ({ type }) => {
  const common = {
    width: 22,
    height: 22,
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: 1.6,
    strokeLinecap: "round",
    strokeLinejoin: "round",
    "aria-hidden": true,
  };
  switch (type) {
    case "chat":
      return (
        <svg {...common}>
          <path d="M21 15a4 4 0 0 1-4 4H9l-4 3v-3H5a4 4 0 0 1-4-4V7a4 4 0 0 1 4-4h12a4 4 0 0 1 4 4v8Z" />
          <path d="M7 9h8M7 13h5" />
        </svg>
      );
    case "route":
      return (
        <svg {...common}>
          <path d="M4 20c2 0 4-1.5 4-3.5S6 13 4 13s-4-1.5-4-3.5S2 6 4 6" transform="translate(4 1)"/>
          <path d="M6 3h8M6 21h8" />
          <circle cx="6" cy="3" r="1.5" />
          <circle cx="14" cy="21" r="1.5" />
        </svg>
      );
    case "pet":
      return (
        <svg {...common}>
          <circle cx="6.5" cy="8" r="2.2" />
          <circle cx="10.5" cy="6.5" r="2" />
          <circle cx="13.5" cy="8.2" r="2" />
          <circle cx="8.5" cy="11" r="2.3" />
          <path d="M6 14c2.2-1.2 5.8-1.2 8 0 1.7 1 2.5 2.4 2.5 3.6S15.7 20 14 20H6c-1.7 0-2.5-1-2.5-2.4S4.3 15 6 14Z" />
        </svg>
      );
    case "pin":
      return (
        <svg {...common}>
          <path d="M12 21s-7-6.4-7-11A7 7 0 0 1 19 10c0 4.6-7 11-7 11Z" />
          <circle cx="12" cy="10" r="2.5" />
        </svg>
      );
    case "map":
      return (
        <svg {...common}>
          <path d="M3 6l6-3 6 3 6-3v15l-6 3-6-3-6 3V6z" />
          <circle cx="12" cy="10" r="2.25" />
          <path d="M12 12.5v5" />
        </svg>
      );
    default:
      return (
        <svg {...common}>
          <rect x="3.5" y="5" width="17" height="14" rx="2" />
          <path d="M8 3.5h8" />
        </svg>
      );
  }
};

const Feature = ({ icon, title, text, href }) => (
  <a href={href} className={classes.feature}>
    <div className={classes.featureIcon}><MockIcon type={icon} /></div>
    <div>
      <h3 className={classes.featureTitle}>{title}</h3>
        <p className={classes.featureText}>
            {String(text).split("\n").map((line, i) => (
                <React.Fragment key={i}>
                {line}
                {i < String(text).split("\n").length - 1 && <br />}
                </React.Fragment>
            ))}
        </p>
    </div>
  </a>
);

const Module = ({ id, icon, title, children, figure }) => (
  <section id={id} className={classes.module}>
    <div className={classes.moduleHeader}>
      <div className={classes.moduleIcon}><MockIcon type={icon} /></div>
      <h3 className={classes.moduleTitle}>{title}</h3>
    </div>
    <div className={classes.moduleBody}>
      <div className={classes.moduleCopy}>{children}</div>
      <div className={classes.moduleFigure}>{figure}</div>
    </div>
  </section>
);

const BrowserMock = ({ children }) => (
  <div className={classes.browserWrap} aria-hidden>
    <div className={classes.browserTop}>
      <span className={classes.dot} />
      <span className={classes.dot} />
      <span className={classes.dot} />
    </div>
    <div className={classes.browserScreen}>{children}</div>
  </div>
);

const MainPage = () => {
  return (
    <React.Fragment>
      <Header />

      <motion.main className={classes.main}
            key='petGang-Main'          
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.3 }}
                    >
        <section className={classes.hero}>
          <div className={classes.containerWide}>
            <div className={classes.heroGrid}>
              <div className={classes.heroCopy}>
                <span className={classes.pill}>여행 · 반려케어 · 지역 데이터</span>
                <h1 className={classes.heroTitle}>
                  강원도 반려동물 동반 여행, <br/>
                  웹에서 더 크게 · 더 편하게
                </h1>
                <p className={classes.heroSubtitle}>
                  실시간 그룹 채팅, 코스 추천, 반려 건강 상담, <br/>
                  지역 특화 데이터를 한 곳에서 제공합니다. <br/>
                  웹 화면에 최적화된 인터페이스로 지도를 넓게 보고, 정보를 빠르게 결정하세요.
                </p>

                <div className={classes.quickNav}>
                  <a href="#local">지역 특화 서비스</a>
                  <a href="#course">여행 코스 추천</a>
                  <a href="#chat">그룹 채팅방</a>
                  <a href="#pet">반려동물 건강 상담</a>
                </div>
              </div>

              <div className={classes.heroVisual}>
                <BrowserMock>
                  <div className={classes.heroShot}>
                    <div className={classes.heroLeftList}>
                      <img className={classes.mainImg} src={mainImg} alt='main-img'/>
                    </div>
                  </div>
                </BrowserMock>
              </div>
            </div>
          </div>
        </section>
        <section className={classes.featuresSection}>
          <div className={classes.container}>
            <h2 className={classes.sectionTitle}>무엇을 할 수 있나요?</h2>
            <div className={classes.featuresGrid}>
                <Feature
                    icon="pin"
                    title="지역 특화 서비스"
                    href="#local"
                    text={`강원도 내 동반 가능지, \n 숙소·식당, 병원·약국을 한눈에.`}
                />
                <Feature
                    icon="route"
                    title="여행 코스 추천"
                    href="#course"
                    text={`출발지·키워드·이동수단을 입력하면 \n 최적 코스를 제안합니다.`}
                />
                <Feature
                    icon="chat"
                    title="그룹 채팅방"
                    href="#chat"
                    text={`여행 중 유용한 정보와 경험을 \n 실시간으로 주고받아요.`}
                />
                <Feature
                    icon="pet"
                    title="반려동물 건강 상담"
                    href="#pet"
                    text={`공공 가이드에 기반한 케어 답변. \n 대화를 저장하고 이어서 볼 수 있습니다.`}
                />
            </div>
          </div>
        </section>
        <div className={classes.descContainer}>
        <Module
            id="local"
            icon="pin"
            title="지역 특화 서비스"
            figure={
              <BrowserMock>
                <div className={classes.figLocal}>
                   <img src={tourImg} alt='tour-img' className={classes.tourImg}/>
                </div>
              </BrowserMock>
            }
          >
            서비스는 강원도 지역 내 반려동물 동반이 가능한 <br/>
            관광지·숙소·식당 정보를 통합 제공하며, 거리순 정렬과 <br/>
            키워드·필터 검색 등 다양한 기능으로 편리한 여행을 지원합니다. <br/>
            또한 근처 동물병원 및 약국 정보를 실시간으로 제공하여 <br/> 
            지도에서 인근 의료시설을 쉽게 찾고,<br/>
            반려동물의 상태에 맞는 장소를 신속하게 확인할 수 있습니다.
          </Module>
        <Module
            id="course"
            icon="route"
            title="여행 코스 추천"
            figure={
              <BrowserMock>
                <div className={classes.figLocal}>
                    <img src={tourRecommendImg} alt='tour-recommend-img' className={classes.tourRecommendImg}/>
                    <img src={tourRecommendResultImg} alt='tour-recommendResult-img' className={classes.tourRecommendImg}/>
                </div>
              </BrowserMock>
            }
          >
            출발지와 키워드를 입력하고 이동수단·반경·방문 수를 정해 보세요. <br/>
            일자별 일정, 동선, 예상 소요시간을 기준으로 최적 코스를 추천합니다. <br/>
            필요 없는 유형은 아래에서 제외할 수 있어요.
          </Module>
          <Module
            id="chat"
            icon="chat"
            title="그룹 채팅방"
            figure={
              <BrowserMock>
                 <div className={classes.figLocal}>
                    <img src={chatImg} alt='chat-img' className={classes.chatImg}/>
                </div>
              </BrowserMock>
            }
          >
            여행 중 발생할 수 있는 유용한 정보나 경험을 공유할 수 있습니다. <br/>
            이를 통해 반려동물 동반 여행 팁, 장소 추천 등을 실시간으로 <br/>
            주고받으며, 여행 전반에 대한 유용한 정보를 쉽게 얻어보세요.
          </Module>

        

          <Module
            id="pet"
            icon="pet"
            title="반려동물 건강 상담"
            figure={
              <BrowserMock>
                <div className={classes.figPet}>
                    <img className={classes.chatBotImg} src={chatBotImg} alt='chat-bot-img'/>
                </div>
              </BrowserMock>
            }
          >
            반려동물의 건강과 케어에 대해 무엇이든 물어보세요. <br/>
            답변은 공공 정책과 가이드라인을 바탕으로 제공합니다. <br/>
            대화는 자동 저장되며, 제목을 바꾸거나 새 채팅을 만들 수 있어요. <br/>
          </Module>


        </div>
        <section className={classes.ctaSection}>
          <div className={classes.catContainer}>
            <div className={classes.ctaCard}>
              <div>
                <h3 className={classes.ctaTitle}>지금, 브라우저에서 바로 시작하세요</h3>
                <p className={classes.ctaText}>
                  설치 없이 웹으로 접속해 큰 화면에서 지도를 보고 계획을 완성하세요.
                </p>
              </div>
              <div className={classes.ctaActions}>
                <Link className={classes.primaryBtn} to='/tour'>관광 장소 보기</Link>
                <Link className={classes.ghostBtn} to='/ai'>코스 추천 열기</Link>
              </div>
            </div>
          </div>
        </section>
      </motion.main>

      <Footer />
    </React.Fragment>
  );
};

export default MainPage;
