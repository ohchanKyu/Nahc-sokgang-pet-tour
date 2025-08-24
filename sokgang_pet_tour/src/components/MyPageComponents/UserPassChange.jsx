import classes from "./UserPassChange.module.css";
import { motion, AnimatePresence } from "framer-motion";
import { toast } from "react-toastify";
import { CheckCircle } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import { editPasswordService } from "../../api/MemberService";

const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

const UserPassChange = ({ onType }) => {

    const [formData, setFormData] = useState({
        password: { value: "", placeholder: "기존 비밀번호를 입력하세요" },
        newPassword: { value: "", placeholder: "새로운 비밀번호를 입력하세요" },
        newConfirmPassword : { value: "", placeholder: "새로운 비밀번호를 다시 입력하세요" },
    });

    const [errors, setErrors] =  useState({});
    const [touched, setTouched] =  useState({});
    const [checking, setChecking] =  useState({});

    const validateField = (name, value) => {
        switch (name) {
            case "password" :
                return passwordRegex.test(value) ? "" : "기존 비밀번호를 정확히 입력해주세요.";
            case "newPassword":
                return passwordRegex.test(value) ? "" : "비밀번호는 최소 8자, 숫자 1개, \n 특수문자 1개를 포함해야 합니다.";
            case "newConfirmPassword":
                return value === formData.newPassword.value && value.trim().length > 0 ? "" : "비밀번호가 일치하지 않습니다.";
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
        setTouched({ password: true, newPassword: true, newConfirmPassword: true  });

        if (Object.values(newErrors).every((err) => err === "")) {
            setChecking((prev) => ({ ...prev, ["formData"]: true }));
            const passwordChangeRequest = {
                originalPassword : formData.password.value,
                newPassword : formData.newPassword.value,
            }
            const passwordChangeResponse = await editPasswordService(passwordChangeRequest);
            if (passwordChangeResponse.success){
                if (passwordChangeResponse.data){
                    toast.success("비밀번호 변경에 성공하셨습니다.", {
                        position: "top-center",
                        autoClose: 2000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                    });
                    onType();
                }else{
                    toast.error("기존 비밀번호와 일치하지 않습니다. 다시 시도해주세요.", {
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
            key='petGang-my-userpass-change'          
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.3 }}
            className={classes.container}>
             <h1 className={classes.title}>비밀번호 변경</h1>
             <p className={classes.desc}>현재 비밀번호와 일치하는 경우 비밀번호를 변경하실 수 있습니다.</p>
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
                            <input
                                type={/password/i.test(key) ? "password" : "text"}
                                name={key}
                                value={formData[key].value}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`${classes.input} ${errors[key] ? classes.input_error : ""}`}
                                placeholder={formData[key].placeholder}
                            />
                            {!errors[key] && touched[key] && formData[key] && 
                                ["password", "newPassword","newConfirmPassword"].includes(key) && 
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
                    {checking['formData'] ? '제출중...' : '비밀번호 변경'}
                </motion.button>
            </motion.form>
            <p className={classes.desc2}>
                현재 비밀번호가 기억나지 않으신다면 아래 링크에서 변경해주세요.
            </p>
            <Link to='/auth?type=4' className={classes.link}>바로가기</Link>
        </motion.section>
    )
};

export default UserPassChange;