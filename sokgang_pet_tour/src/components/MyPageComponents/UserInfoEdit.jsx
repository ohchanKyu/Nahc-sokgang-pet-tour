import classes from "./UserInfoEdit.module.css";
import { motion, AnimatePresence } from "framer-motion";
import { useState } from "react";
import { toast } from "react-toastify";
import { CheckCircle } from "lucide-react";
import { editMemberInfoService } from "../../api/MemberService";


const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const UserInfoEdit = ({ loginCtx, onType }) => {

    const { createTime, memberId, email, name, userId, role } = loginCtx || {};
    
    const roleTitle = role === "USER" ? "일반 사용자 등급" : "관리자 등급";
    const [formData, setFormData] = useState({
        name : { value: name, placeholder: name },
        email : { value: email, placeholder: email },
        userId : { value: userId, placeholder: userId },
        role : { value: roleTitle, placeholder: roleTitle },
    });

    const [errors, setErrors] =  useState({});
    const [touched, setTouched] =  useState({});
    const [checking, setChecking] =  useState({});
    
    const validateField = (name, value) => {
        switch (name) {
            case "name":
                return value.length >= 2 && value.length <= 50 ? "" : "이름은 2자 이상 50자 이하로 입력해주세요.";
            case "email":
                return emailRegex.test(value) ? "" : "올바른 이메일 형식을 입력해주세요.";
            default:
                return "";
        }
    };
    
    const handleFocus = (e) => {
        const { name } = e.target;
        setTouched({ ...touched, [name]: true });
        const error = validateField(name, formData[name].value);
        setErrors({ ...errors, [name]: error });
    };
        
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: { ...formData[name], value } });
        if (touched[name]) {
            const error = validateField(name, value);
            setErrors({ ...errors, [name]: error });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        let newErrors = {};
        Object.keys(formData).forEach((key) => {
            newErrors[key] = validateField(key, formData[key].value);
        });
        setErrors(newErrors);
        setTouched({ name: true, email: true  });

        if (Object.values(newErrors).every((err) => err === "")) {
            setChecking((prev) => ({ ...prev, ["formData"]: true }));
            const request = {
                name : formData.name.value,
                email : formData.email.value
            }
            const editMemberResponse = await editMemberInfoService(request);
            if (editMemberResponse.success){
                if (editMemberResponse.message){
                    toast.success(`${editMemberResponse.message}`, {
                        position: "top-center",
                        autoClose: 2000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                    });
                    loginCtx.loginUser({
                        name : request.name,
                        email : request.email,
                        userId : userId,
                        role : role,
                        createTime : createTime,
                        memberId: memberId
                    })
                    onType();
                }else{
                    toast.error("회원정보 변경을 실패하였습니다. 다시 시도해주세요.", {
                        position: "top-center",
                        autoClose: 2000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                    });
                }
            }else{
                const errorMessage = passwordChangeResponseData.message;
                toast.error(`일시적 오류입니다. \n ${errorMessage}`, {
                    position: "top-center",
                    autoClose: 2000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                });
            }
            setChecking((prev) => ({ ...prev, ["formData"]: false }));
        }else {
            toast.error("입력값을 정확히 입력해주세요.", {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        }
    };

    return (
        
        <motion.section
            key='petGang-my-userinfo-edit'          
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.3 }}
            className={classes.container}
            >
                <h1 className={classes.title}>회원정보 변경</h1>
                <p className={classes.desc}>원하시는 회원정보를 변경할 수 있습니다. <br/> 아이디와 사용자 권한은 변경이 불가합니다.</p>
                <motion.form
                    onSubmit={handleSubmit}
                    className={classes.auth_form}
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.3 }}
                >
                {Object.keys(formData).map((key) => (
                    <div key={key} className={classes.input_group}>
                        <div className={classes.input_wrapper}>
                            {(key === "name" || key === "email") && (
                                <input
                                    type="text"
                                    name={key}
                                    value={formData[key].value}
                                    onChange={handleChange}
                                    onFocus={handleFocus}
                                    className={`${classes.input} ${errors[key] ? classes.input_error : ""}`}
                                    placeholder={formData[key].placeholder}
                                />
                            )}
                            {(key === "role" || key === "userId") && (
                                <input
                                    type="text"
                                    name={key}
                                    disabled={true}
                                    value={formData[key].value}
                                    className={classes.inputDisabled}
                                    placeholder={formData[key].placeholder}
                                />
                            )}
                            {!errors[key] && touched[key] && formData[key] && 
                                ["name", "email"].includes(key) && 
                                (
                                    <CheckCircle className={classes.check_icon} color="green" size={20} />
                                )
                            }
                        </div>
                        <AnimatePresence>
                            {touched[key] && errors[key] && (
                                <motion.p
                                    className={classes.error}
                                    initial={{ opacity: 0, y: -5 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    exit={{ opacity: 0, y: -5 }}
                                >
                                    {errors[key]}
                                </motion.p>
                            )}
                        </AnimatePresence>
                    </div>
                ))}
                <motion.button
                    type="submit"
                    className={classes.submit_button}
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.95 }}
                    disabled={checking['formData']}
                >
                    {checking['formData'] ? '제출중...' : '회원정보 변경'}
                </motion.button>
            </motion.form>
            <p className={classes.desc2}>
                회원탈퇴를 원하시면 아래 버튼을 클릭해주세요.
            </p>
            <motion.button 
                whileHover={{ scale : 1.02 }}
                className={classes.deleteButton}>
                회원탈퇴
            </motion.button>
        </motion.section>
                 
    )
};

export default UserInfoEdit;