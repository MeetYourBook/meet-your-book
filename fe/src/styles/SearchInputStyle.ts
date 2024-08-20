import styled from "styled-components";

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

export { InputField, SearchField };
