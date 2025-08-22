import React, { useReducer } from "react";
import loginContext from './login-context';

const defaultLoginUser = {
    name : '',
    email : '',
    userId : '',
    role : '',
    createTime : '',
    memberId : '',
}

const loginReducer = (state,action) => {

    if (action.type === "LOGIN"){
        return {
            ...state,
            name : action.name,
            email : action.email,
            userId : action.userId,
            role : action.role,
            createTime : action.createTime,
            memberId : action.memberId
        }
    }
    if (action.type === "LOGOUT"){
        return defaultLoginUser;
    }
    return defaultLoginUser;
}

const LoginProvider = (props) => {

    const [userState, dispatchUserAction] = useReducer(loginReducer, defaultLoginUser);
  
    const loginHandler = (authContext) => { 
        dispatchUserAction({
            type : 'LOGIN',
            ...authContext
        })
    };

    const logoutHandler = () => {
        sessionStorage.removeItem("accessToken");
        sessionStorage.removeItem("refreshToken"); 
        dispatchUserAction({
            type : 'LOGOUT',
        })
    };

    const userContext = {
        name : userState.name,
        email : userState.email,
        userId : userState.userId,
        role : userState.role,
        createTime : userState.createTime,
        memberId : userState.memberId,
        loginUser : loginHandler,
        logoutUser : logoutHandler,
    }

    return (
        <loginContext.Provider value={userContext}>
            {props.children}
        </loginContext.Provider>
    );
}

export default LoginProvider;