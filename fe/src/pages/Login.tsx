import { useNavigate } from "react-router-dom";
import * as S from "@/styles/AuthForm";

const Login = () => {
    const navigate = useNavigate();
    const goToSignUp = () => {
        navigate("/SignUp");
    };
    return (
        <S.AuthContainer>
            <S.AuthCard>
                <S.AuthTitle>Login</S.AuthTitle>
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
    );
};

export default Login;
