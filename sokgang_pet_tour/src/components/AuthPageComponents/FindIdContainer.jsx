import React from "react";
import classes from "./AuthContainer.module.css";
import FindIdInput from "./input/FindIdInput";
import { findIdService } from "../../api/AuthService";

const FindIdContainer = () => {

    const findIdHandler = async (name,email) => {
        return await findIdService(name,email);
    }

    return (
        <React.Fragment>
            <div className={classes.auth_wrapper}>
                <FindIdInput findIdHandler={findIdHandler}/>
            </div>
        </React.Fragment>
    )

};

export default FindIdContainer;