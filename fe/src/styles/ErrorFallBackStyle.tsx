import styled from "styled-components";

const Container = styled.div`
    display: flex;
    min-height: 100dvh;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 12px 16px;

    @media (min-width: 640px) {
        padding: 12px 24px;
    }

    @media (min-width: 1024px) {
        padding: 12px 32px;
    }
`;

const ContentWrapper = styled.div`
    max-width: 30rem;
    text-align: center;
    margin: 0 auto;
`;

const Title = styled.h1`
    margin-top: 1rem;
    font-size: 1.875rem;
    font-weight: bold;
    font-family: mono;

    @media (min-width: 640px) {
        font-size: 2.25rem;
    }
`;

const Description = styled.p`
    margin-top: 1rem;
    color: gray;
`;

const ButtonWrapper = styled.div`
    margin-top: 1.5rem;
`;

const RetryButton = styled.button`
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
    font-weight: 500;
    color: white;
    background-color: black;
    border-radius: 0.375rem;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    transition: background-color 0.2s;
    border: none;
    cursor: pointer;
    &:hover {
        background-color: #2b2a2a;
    }
`;

export {
    Container,
    ContentWrapper,
    Title,
    Description,
    ButtonWrapper,
    RetryButton,
};
