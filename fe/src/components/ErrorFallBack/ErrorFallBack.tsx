import * as S from "@/styles/ErrorFallBackStyle"

const ErrorFallBack = () => {
    return (
        <S.Container>
            <S.ContentWrapper>
                <S.Title>오류가 발생했습니다!</S.Title>
                <S.Description>
                    죄송합니다. 예기치 못한 오류가 발생했습니다. 잠시 후 다시
                    시도해 주세요.
                </S.Description>
                <S.ButtonWrapper>
                    <S.RetryButton onClick={() => window.location.reload()}>
                        다시 시도하기
                    </S.RetryButton>
                </S.ButtonWrapper>
            </S.ContentWrapper>
        </S.Container>
    );
};

export default ErrorFallBack;
