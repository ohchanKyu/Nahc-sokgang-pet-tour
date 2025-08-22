import React, { useContext } from 'react';
import Header from '../components/LayoutComponents/Header';
import Footer from '../components/LayoutComponents/Footer';
import loginContext from '../store/login-context';
import classes from "./AdminPage.module.css";
import DataSync from '../components/AdminPageComponents/DataSync';
import { motion } from 'framer-motion';

const AdminPage = () => {

    const loginCtx = useContext(loginContext);
    const role = loginCtx.role;
    if (!role || role !== 'ADMIN') {
        return (
            <div>
                <Header />
                <div className={classes.notFoundContainer}>
                    <h1>403 - Forbidden</h1>
                    <p>접근 권한이 존재하지 않는 페이지입니다.</p>
                    <button className={classes.notFoundButton} onClick={() => window.location.href = '/'}>
                        홈으로 돌아가기
                    </button>
                </div>
                <Footer />
            </div>
        );
    }
    
    return (
        <>
            <Header/>
            <motion.div 
                key='petGang-admin'          
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -10 }}
                transition={{ duration: 0.3 }}
                className={classes.wrapper}>
                <DataSync/>
            </motion.div>
            <Footer/>
        </>
    )
};


export default AdminPage;