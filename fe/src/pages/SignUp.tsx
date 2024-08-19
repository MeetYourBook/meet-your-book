import * as S from "@/styles/AuthForm";

const SignUp = () => {
    return (
        <S.AuthContainer>
            <S.AuthCard>
                <S.AuthTitle>Sign Up</S.AuthTitle>
                <S.AuthForm>
                    <S.FormGroup>
                        <S.Label>Name</S.Label>
                        <S.Input
                            id="Name"
                            type="text"
                            placeholder="Enter your Name"
                        />
                    </S.FormGroup>
                    <S.FormGroup>
                        <S.Label>ID</S.Label>
                        <S.Input
                            id="ID"
                            type="text"
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
                    <S.Button>Sign Up</S.Button>
                </S.AuthForm>
            </S.AuthCard>
        </S.AuthContainer>
    );
};

export default SignUp;
