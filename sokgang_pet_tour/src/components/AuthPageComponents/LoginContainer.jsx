import React, { useContext } from "react";
import classes from "./AuthContainer.module.css";
import LoginInput from "./input/LoginInput";
import { signinService } from "../../api/AuthService";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { getMemberService } from "../../api/MemberService";
import loginContext from "../../store/login-context";

const LoginContainer = () => {

    const navigate = useNavigate();
    const loginCtx = useContext(loginContext);

    const loginSubmitHandler = async (formData) => {
        
        const signinResponseData = await signinService(formData);
        if (signinResponseData.success){
            const tokenData = signinResponseData.data;
            const accessToken = tokenData.accessToken;
            const refreshToken = tokenData.refreshToken;
            sessionStorage.setItem('accessToken',accessToken);
            sessionStorage.setItem('refreshToken',refreshToken);
            const memberResponseData = await getMemberService();
            if (memberResponseData.success){
                const memberData = memberResponseData.data;
                sessionStorage.setItem('USER_ROLE', memberData.role);
                loginCtx.loginUser({
                    name : memberData.name,
                    email : memberData.email,
                    userId : memberData.userId,
                    role : memberData.role,
                    createTime : memberData.createTime,
                })
            };
            navigate('/');
        }else{
            const code = signinResponseData.code;
            if (code === "A004"){
                toast.error("일치하는 회원정보가 존재하지 않습니다.", {
                    position: "top-center",
                    autoClose: 2000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                });
            }
        }
    };

    return (
        <React.Fragment>
            <div className={classes.auth_wrapper}>
                <LoginInput loginSubmitHandler={loginSubmitHandler}/>
            </div>
        </React.Fragment>
    )
};

export default LoginContainer;