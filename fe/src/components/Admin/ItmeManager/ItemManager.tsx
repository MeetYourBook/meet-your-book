import styled from "styled-components";
import { SearchOutlined } from "@ant-design/icons";
import { FormEvent, useRef } from "react";
import { message } from "antd";
import { ERROR_MESSAGE } from "@/constants";

const ItemManager = () => {
    const inputRef = useRef<HTMLInputElement>(null)

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if(inputRef.current?.value === "") return message.warning(ERROR_MESSAGE.EMPTY_INPUT)
        console.log(inputRef.current?.value)
    }
    return (
        <ItemManagerContainer>
            <InputForm onSubmit={handleSubmit}>
                <Input ref={inputRef} placeholder="Search items..." />
                <SubmitBtnWrapper type="submit">
                    <SearchOutlined />
                </SubmitBtnWrapper>
            </InputForm>
        </ItemManagerContainer>
    )
};

export default ItemManager;

const ItemManagerContainer = styled.div`
    max-width: 1000px;
    height: 100vh;
    margin: 0 auto;
    padding: 30px;
    
`;

const InputForm = styled.form`
    display: flex;
    align-items: center;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    width: 300px;
    overflow: hidden;
`;

const Input = styled.input`
    border: none;
    flex: 1;
    padding: 8px 12px;
    font-size: 14px;
    outline: none;
    background-color: ${({ theme }) => theme.input};
`;

const SubmitBtnWrapper = styled.button`
    height: 100%;
    background: none;
    border: none;
    cursor: pointer;
    padding: 8px 12px;
    display: flex;
    align-items: center;
    justify-content: center;
`;