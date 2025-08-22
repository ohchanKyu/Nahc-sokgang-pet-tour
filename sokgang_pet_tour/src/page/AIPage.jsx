import classes from "./AIPage.module.css";
import Header from "../components/LayoutComponents/Header";
import Footer from "../components/LayoutComponents/Footer";
import TourRecommand from "../components/AIPageComponents/TourRecommand";
import AIChatList from "../components/AIPageComponents/AIChatList";

const AIPage = () => {

    return (
        <div>
            <Header/>
            <div className={classes.wrapper}>
                <div className={classes.recommendWrapper}>
                    <h2 className={classes.title}>여행 코스 추천</h2>
                    <p className={classes.description}>
                        출발지와 키워드를 입력하고 이동수단·반경·방문 수를 정해 보세요. <br/>
                        일자별 일정, 동선, 예상 소요시간을 기준으로 최적 코스를 추천합니다. <br/>
                        필요 없는 유형은 아래에서 제외할 수 있어요. <br/>
                        기본 주소는 속초 해수욕장으로 설정되어 있어요.
                    </p>
                    <TourRecommand/>
                </div>
                <div className={classes.AIWrapper}>
                    <h2 className={classes.title}>반려동물 건강 상담</h2>
                    <p className={classes.description}>
                        반려동물의 건강과 케어에 대해 무엇이든 물어보세요. <br/>
                        답변은 공공 정책과 가이드라인을 바탕으로 제공합니다. <br/>
                        대화는 자동 저장되며, 좌측에서 제목을 바꾸거나 새 채팅을 만들 수 있어요.
                    </p>
                    <AIChatList/>
                </div>
            </div>
            <Footer/>
        </div>
    );
}

export default AIPage;