import * as S from "@/styles/AuthFormStyle";
import Logo from "@/components/Navigation/Logo/Logo";
import { FormEvent, useState } from "react";
const Login = () => {
    const [loginForm, setLoginForm] = useState({ id: "", password: "" });

    const handleInputChange = (e: FormEvent<HTMLInputElement>) => {
        const { id, value } = e.currentTarget;
        setLoginForm((prev) => ({ ...prev, [id]: value }));
    };

    const handleSubmit = () => {}
    
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
                    <S.AuthForm>
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
                        <S.Button onClick={handleSubmit}>Login</S.Button>
                    </S.AuthForm>
                </S.AuthCard>
            </S.AuthContainer>
        </>
    );
};

export default Login;
