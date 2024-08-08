import styled from "styled-components";

const NavContainer = styled.nav`
    width: 100%;
    border-bottom: 1px solid var(--border-color);
    padding: 1rem;
`;

const NavWrap = styled.div`
    max-width: 1200px;
    min-width: 350px;
    margin: auto;
    display: flex;
    align-items: center;
`;

const LogoWrap = styled.div`
    text-align: center;
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    padding: 0px 2px;
    gap: 3px;
`;

const LogoAbbreviation = styled.h1`
    font-weight: 700;
    font-size: 1.6rem;

    @media (max-width: 768px) {
        font-size: 1rem;
    }
`;

const LogoFullName = styled.p`
    font-weight: 300;
    font-size: 0.8rem;
    font-style: italic;
    @media (max-width: 768px) {
        font-size: 0.4rem;
    }
`;

const InputWrap = styled.div`
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
    border: 1px solid var(--border-color);
    border-radius: 5px;
    max-width: 600px;
    min-width: 300px;
    margin: 0 20px;
    display: flex;
    align-items: center;
    height: 2.1rem;

    @media (max-width: 768px) {
        position: static;
        transform: none;
    }

`;

export {
    NavContainer,
    NavWrap,
    LogoAbbreviation,
    LogoFullName,
    LogoWrap,
    InputWrap,
};
