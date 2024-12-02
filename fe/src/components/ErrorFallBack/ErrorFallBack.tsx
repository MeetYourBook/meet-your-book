import * as S from "@/styles/ErrorFallBackStyle"
import { HTTP_ERROR_MESSAGE } from "@/constants/HTTPErrorMessage";
import { ErrorProps } from "async-error-boundary";

const ErrorFallBack = ({ statusCode = 404, resetError }: ErrorProps) => {
    const currentStatusCode = statusCode as keyof typeof HTTP_ERROR_MESSAGE;

    return (
        <S.Container>
            <S.ContentWrapper>
                <S.Title>{HTTP_ERROR_MESSAGE[currentStatusCode].HEADING}</S.Title>
                <S.Description>
                    {HTTP_ERROR_MESSAGE[currentStatusCode].BODY}
                </S.Description>
                <S.ButtonWrapper>
                    <S.RetryButton onClick={resetError}>
                        {HTTP_ERROR_MESSAGE[currentStatusCode].BUTTON}
                    </S.RetryButton>
                </S.ButtonWrapper>
            </S.ContentWrapper>
        </S.Container>
    );
};

export default ErrorFallBack;