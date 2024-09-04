import styled, { keyframes, css } from "styled-components";

const dropdownOpenAnimation = keyframes`
    from {
        max-height: 0;
        transform: translateY(-10px);
    }
    to {
        max-height: 20rem;
        transform: translateY(0);
    }
`;

const dropdownCloseAnimation = keyframes`
    from {
        max-height: 20rem;
        transform: translateY(0);
    }
    to {
        max-height: 0;
        transform: translateY(-10px);
        display: none;
    }
`;

const Container = styled.div`
    min-width: 240px;
    border: 1px solid var(--border-color);
    border-radius: 8px;
`;

const Header = styled.header`
    display: flex;
    justify-content: space-between;
    margin: 1rem;
    font-weight: 400;
    font-size: 0.9rem;
    cursor: pointer;
    
`;

const Title = styled.div`
    font-weight: 600;
`

const ListWrap = styled.div<{ $isOpen: boolean }>`
    font-weight: 400;
    font-size: 0.9rem;
    overflow-y: auto;
    padding: 1rem;
    border-top: ${({$isOpen}) => $isOpen && "1px solid var(--border-color)"};
    transform: ${({ $isOpen }) =>
        $isOpen ? "translateY(0)" : "translateY(-10px)"};
    animation: ${({ $isOpen }) =>
        $isOpen
            ? css`${dropdownOpenAnimation} 0.3s ease-out forwards`
            : css`${dropdownCloseAnimation} 0.3s ease-out forwards`};

    &::-webkit-scrollbar {
        width: 4px;
    }

    &::-webkit-scrollbar-thumb {
        background-color: var(--border-color);
        border-radius: 10px;
    }

    &::-webkit-scrollbar-track {
        background-color: transparent;
    }
`;

const ListUl = styled.ul`
    list-style: none;
    padding: 0;
`;



const Input = styled.input`
    width: 90%;
    height: 2rem;
    border: ${({ theme }) => theme.border};
    border-radius: 8px;
    margin: 0px auto 1rem;
    padding: 0 0.6rem;
    outline: none;
    background-color: ${({ theme }) => theme.input};

`

export { Container, Header, ListWrap, ListUl, Input, Title };
