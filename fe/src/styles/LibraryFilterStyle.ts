import styled, { css } from "styled-components";

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
`;

const ListWrap = styled.div<{ $isOpen: boolean }>`
    font-weight: 400;
    font-size: 0.9rem;
    overflow-y: auto;
    padding: 1rem;
    border-top: ${({ $isOpen }) =>
        $isOpen ? "1px solid var(--border-color)" : "none"};

    ${({ $isOpen }) =>
        $isOpen
            ? css`
                display: block;
                max-height: 20rem;
                padding: 1rem;
                transform: translateY(0);
                transition:
                    max-height 0.3s ease-out,
                    padding 0.3s ease-out,
                    transform 0.3s ease-out;
            `
            : css`
                max-height: 0;
                padding: 0 1rem;
                transform: translateY(-10px);
                transition:
                    max-height 0.3s ease-out,
                    padding 0.3s ease-out,
                    transform 0.3s ease-out;
                transition-delay: 0s, 0s, 0s, 0.3s;
            `}
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
    color: ${({ theme }) => theme.text};
`;

export { Container, Header, ListWrap, ListUl, Input, Title };
