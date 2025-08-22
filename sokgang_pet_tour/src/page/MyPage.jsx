import React, { useContext } from 'react';
import classes from './MyPage.module.css';
import Header from '../components/LayoutComponents/Header';
import Footer from '../components/LayoutComponents/Footer';
import loginContext from '../store/login-context';

const MyPage = () => {
  // loginCtx에서 사용자 정보 가져오기
  const loginCtx = useContext(loginContext);

  if (!loginCtx) {
    return (
      <div>
        <Header />
        <h1>로딩 중...</h1>
        <Footer />
      </div>
    );
  }

  const { createTime, email, name, role, userId } = loginCtx;

  return (
    <div className={classes.myPageContainer}>
      <Header />
      <div className={classes.userInfo}>
        <h1>My Page</h1>
        <div className={classes.infoCard}>
          <div className={classes.infoItem}>
            <strong>이름:</strong> <span>{name}</span>
          </div>
          <div className={classes.infoItem}>
            <strong>이메일:</strong> <span>{email}</span>
          </div>
          <div className={classes.infoItem}>
            <strong>사용자 ID:</strong> <span>{userId}</span>
          </div>
          <div className={classes.infoItem}>
            <strong>가입일:</strong> <span>{new Date(createTime).toLocaleString()}</span>
          </div>
          <div className={classes.infoItem}>
            <strong>역할:</strong> <span>{role}</span>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MyPage;
