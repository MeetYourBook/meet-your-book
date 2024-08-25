import styled from "styled-components";

const Container = styled.div`
    width: 100%;
    height: 100%;
    padding: 1rem 0;
`

const Input = styled.input`
    width: 50%;
    height: 2rem;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    margin: 15px auto;
    padding: 0 0.6rem;
    outline: none;
`
const LibrariesWrap = styled.div`
    display: flex;
    flex-direction: column;
    gap: 5px;
`

const LibraryItem = styled.a`
    font-size: 12px;
    margin: 2px 0;
    color: black;
    text-decoration: none;
    cursor: pointer;
    &:hover {
        text-decoration: underline;
        color: #1a0dab;
    }
`;

export {Container, Input, LibrariesWrap, LibraryItem}