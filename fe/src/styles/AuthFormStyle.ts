import styled from "styled-components";

const AuthContainer = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 16px;
`;

const AuthCard = styled.div`
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    padding: 32px;
    width: 100%;
    max-width: 300px;
`;

const Header = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 25px;
    padding: 0px 2px;
`;

const AuthTitle = styled.h2`
    font-size: 1.25rem;
    font-weight: bold;
`;

const AuthForm = styled.div`
    display: flex;
    flex-direction: column;
    gap: 16px;
`;

const FormGroup = styled.div`
    display: flex;
    flex-direction: column;
`;

const Label = styled.div`
    font-size: 0.875rem;
    margin-bottom: 8px;
`;

const Input = styled.input`
    padding: 8px;
    border-radius: 8px;
    border: 1px solid #d1d5db;
    outline: none;
    &:focus {
        border-color: #60a5fa;
    }
`;

const Button = styled.button`
    width: 100%;
    padding: 10px;
    background-color: black;
    color: white;
    border-radius: 8px;
    border: none;
    font-size: 1rem;
    text-align: center;
    cursor: pointer;
    &:hover {
        background-color: #a1a5ab;
    }
`;

const LogoWrap = styled.div`
    padding: 30px;
`;

export {
    AuthContainer,
    AuthCard,
    AuthTitle,
    AuthForm,
    FormGroup,
    Label,
    Input,
    Button,
    Header,
    LogoWrap,
};
