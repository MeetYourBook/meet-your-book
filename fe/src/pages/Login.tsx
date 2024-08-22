import { useNavigate } from "react-router-dom";
import * as S from "@/styles/AuthFormStyle";
import BackButton from "@/components/BackButton/BackButton";
import { Logo } from "@/components/Navigation/Navigation";
const Login = () => {
    const navigate = useNavigate();
    const goToSignUp = () => {
        navigate("/SignUp");
    };
    return (
        <>
            <S.LogoWrap>
                <Logo />
            </S.LogoWrap>
            <S.AuthContainer>
                <S.AuthCard>
                    <S.Header>
                        <BackButton />
                        <S.AuthTitle>Login</S.AuthTitle>
                    </S.Header>
                    <S.AuthForm>
                        <S.FormGroup>
                            <S.Label>ID</S.Label>
                            <S.Input
                                id="ID"
                                type="ID"
                                placeholder="Enter your ID"
                            />
                        </S.FormGroup>
                        <S.FormGroup>
                            <S.Label>Password</S.Label>
                            <S.Input
                                id="password"
                                type="password"
                                placeholder="Enter your password"
                            />
                        </S.FormGroup>
                        <S.Button>Login</S.Button>
                        <S.AuthButton>Sign in with Google</S.AuthButton>
                        <S.SignUpText>
                            Don't have an account?{" "}
                            <S.SignUpLink onClick={goToSignUp}>
                                Sign up
                            </S.SignUpLink>
                        </S.SignUpText>
                    </S.AuthForm>
                </S.AuthCard>
            </S.AuthContainer>
        </>
    );
};

export default Login;
