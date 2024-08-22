import styled from "styled-components";

const InputWrap = styled.div`
    border: 1px solid var(--border-color);
    border-radius: 8px;
    max-width: 600px;
    min-width: 300px;
    margin: 0 50px 0 0;
    display: flex;
    align-items: center;
    height: 2.4rem;

    @media (max-width: 768px) {
        display: none;
    }
`;

const InputField = styled.input`
    width: 300px;
    height: 100%;
    padding: 0 10px;
    outline: none;
    border: none;
`;

const SearchField = styled.button`
    width: 50px;
    height: 90%;
    color: gray;
    font-size: 1rem;
    border: none;
    border-radius: 0 5px 5px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    background-color: white;
`;

export { InputWrap, InputField, SearchField };
