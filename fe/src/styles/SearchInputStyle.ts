import styled from "styled-components";
import { SearchOutlined } from "@ant-design/icons";
const InputWrap = styled.div`
    border: 1px solid var(--border-color);
    border-radius: 8px;
    max-width: 450px;
    min-width: 300px;
    display: flex;
    align-items: center;
    height: 2.4rem;

    @media (max-width: 768px) {
        margin: auto;
    }
`;

const InputField = styled.input`
    width: 300px;
    height: 100%;
    padding: 0 10px;
    outline: none;
    border: none;
    background-color: ${({ theme }) => theme.body};
    color: ${({ theme }) => theme.text};
    @media (max-width: 450px) {
        width: 200px;
    }
`;

const SearchField = styled(SearchOutlined)`
    width: 50px;
    height: 90%;
    font-size: 1rem;
    border: none;
    border-radius: 0 5px 5px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
`;

export { InputWrap, InputField, SearchField };
