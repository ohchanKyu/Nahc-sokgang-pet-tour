import React, { useContext, useMemo, useState } from 'react';
import classes from './MyPage.module.css';
import Header from '../components/LayoutComponents/Header';
import Footer from '../components/LayoutComponents/Footer';
import loginContext from '../store/login-context';
import { motion } from 'framer-motion';
import UserInfo from '../components/MyPageComponents/UserInfo';
import UserInfoEdit from '../components/MyPageComponents/UserInfoEdit';
import UserPassChange from '../components/MyPageComponents/UserPassChange';

const MyPage = () => {

  const loginCtx = useContext(loginContext);
  const [type,setType] = useState(1);
  
  return (
    <>
    <Header />
    <div className={classes.myPageContainer}>
      <motion.main 
        key='petGang-my'          
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -10 }}
        transition={{ duration: 0.3 }}
        className={classes.main}>
        <aside className={classes.sidebar}>
          <h2 className={classes.sidebarTitle}>MY PAGE</h2>
          <section className={classes.navSection}>
            <h3 className={classes.navHeading}>회원정보</h3>
            <ul className={classes.navList}>
              <li onClick={() => setType(1)}>회원정보 조회</li>
              <li onClick={() => setType(2)}>회원정보 수정</li>
              <li onClick={() => setType(3)}>비밀번호 변경</li>
            </ul>
          </section>
        </aside>
        <div className={classes.sectionContainer}>
            {type === 1 && <UserInfo loginCtx={loginCtx}/>}
            {type === 2 && <UserInfoEdit onType={() => setType(1)} loginCtx={loginCtx}/>}
            {type === 3 && <UserPassChange onType={() => setType(1)}/>}
        </div>
      </motion.main>
    </div>
    <Footer />
    </>
  );
};

export default MyPage;
