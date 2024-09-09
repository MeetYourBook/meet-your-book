import * as S from "@/styles/AuthFormStyle";
import Logo from "@/components/Navigation/Logo/Logo";
import { FormEvent, useEffect, useState } from "react";
import useLoginMutation from "@/hooks/queries/useLoginMutation";
import { useNavigate } from "react-router-dom";
import { message, Spin } from "antd";
import { SUCCESS_MESSAGE } from "@/constants";
interface LoginForm {
    id: string;
    password: string;
}

const Login = () => {
    const [loginForm, setLoginForm] = useState<LoginForm>({ id: "", password: "" });
    const { data, mutate, isPending } = useLoginMutation();
    const navigate = useNavigate()

    const handleInputChange = (e: FormEvent<HTMLInputElement>) => {
        const { id, value } = e.currentTarget;
        setLoginForm((prev) => ({ ...prev, [id]: value }));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        mutate(loginForm);
    };

    useEffect(() => {
        if (data?.token) {
            sessionStorage.setItem("token", data.token)
            navigate("/admin")
            message.success(SUCCESS_MESSAGE.LOGIN_SUCCESS)
        }
    }, [data, navigate])

    return (
        <>
            <S.LogoWrap>
                <Logo />
            </S.LogoWrap>
            <S.AuthContainer>
                <S.AuthCard>
                    <S.Header>
                        <S.AuthTitle>Login</S.AuthTitle>
                    </S.Header>
                    <S.AuthForm onSubmit={handleSubmit}>
                        <S.FormGroup>
                            <S.Label>ID</S.Label>
                            <S.Input
                                id="id"
                                type="text"
                                placeholder="Enter your ID"
                                value={loginForm.id}
                                onChange={handleInputChange}
                            />
                        </S.FormGroup>
                        <S.FormGroup>
                            <S.Label>Password</S.Label>
                            <S.Input
                                id="password"
                                type="password"
                                placeholder="Enter your password"
                                value={loginForm.password}
                                onChange={handleInputChange}
                            />
                        </S.FormGroup>
                        <S.Button disabled={isPending} type="submit">{isPending ? <Spin size="small"/> : "Login"}</S.Button>
                    </S.AuthForm>
                </S.AuthCard>
            </S.AuthContainer>
        </>
    );
};

export default Login;