import styled from "styled-components";

const Container = styled.div`
    width: 100%;
    height: 100%;
    padding-bottom: 1rem;
`

const Input = styled.input`
    width: 60%;
    height: 2rem;
    border: ${({ theme }) => theme.border};
    border-radius: 8px;
    margin: 15px auto;
    padding: 0 0.6rem;
    outline: none;
    background-color: ${({ theme }) => theme.input};
    color: ${({ theme }) => theme.text};
`
const LibrariesWrap = styled.div`
    display: flex;
    flex-direction: column;
    gap: 5px;
`

const LibraryItem = styled.a`
    font-size: 13px;
    margin: 2px 0;
    color: ${({ theme }) => theme.text};
    text-decoration: none;
    cursor: pointer;
    &:hover {
        text-decoration: underline;
        color: #1a0dab;
    }
`;

const PagingWrap = styled.div`
    display: flex;
    justify-content: center;
    margin-top: 20px;
`;

export {Container, Input, LibrariesWrap, LibraryItem, PagingWrap}