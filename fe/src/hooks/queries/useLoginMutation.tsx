import { loginAPI } from "@/services"
import { useMutation } from "@tanstack/react-query"
import {message} from "antd"
import { ERROR_MESSAGE } from "@/constants"
interface LoginForm {
    id: string;
    password: string;
}

const fetchLogin = async (body: LoginForm) => {
    const response = await loginAPI.post(body, "")
    
    if (response.status !== 200) {
        message.error(ERROR_MESSAGE.LOGIN_ERROR);
    }
    const data = await response.json()
    return data
}

const useLoginMutation = () => {
    return useMutation({
        mutationFn: (body: LoginForm) => fetchLogin(body),
    });
}

export default useLoginMutation