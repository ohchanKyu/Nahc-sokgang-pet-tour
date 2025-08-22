import React from "react";

const loginContext = React.createContext({
    name : '',
    email : '',
    userId : '',
    role : '',
    createTime : '',
    loginUser : (authContext) => {},
    logoutUser : () => {},
});

export default loginContext;