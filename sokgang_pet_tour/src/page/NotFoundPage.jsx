import React from 'react';
import Header from '../components/LayoutComponents/Header';
import Footer from '../components/LayoutComponents/Footer';
import classes from "./NotFoundPage.module.css";

const NotFoundPage = () => {
  return (
    <>
        <Header />
        <div className={classes.notFoundContainer}>
            <h1>404 - Page Not Found</h1>
            <p>죄송합니다. 찾을 수 없는 페이지입니다.</p>
            <button className={classes.notFoundButton} onClick={() => window.location.href = '/'}>
                홈으로 돌아가기
            </button>
        </div>
        <Footer/>
    </>
  );
};

export default NotFoundPage;
