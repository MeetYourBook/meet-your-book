import styled from "styled-components";

const NavContainer = styled.nav`
    width: 100%;
    border-bottom: 1px solid var(--border-color);
    margin-bottom: 4rem;
    @media (max-width: 768px) {
        margin-bottom: 1rem;
    }
`;

const NavWrap = styled.div`
    max-width: 1060px;
    min-width: 350px;
    margin: 1rem auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    @media (max-width: 1000px) {
        width: 780px;
    }
    @media (max-width: 768px) {
        width: 100%;
    }
    @media (max-width: 600px) {
        width: 363px;
    }
`;

const LogoWrap = styled.div`
    text-align: center;
    padding-left: 1rem;
    gap: 3px;
    cursor: pointer;
`;

const LogoName = styled.h1`

    font-weight: 800;
    font-size: 24Px;
`;

const SearchWrap = styled.div`
    margin-right: 30px;
    @media (max-width: 1000px) {
        display: none;
    }
`

const ThemeSwitcherWrap = styled.span`
    @media (max-width: 1000px) {
        display: none;
    }
`

export {
    NavContainer,
    NavWrap,
    LogoName,
    LogoWrap,
    SearchWrap,
    ThemeSwitcherWrap,
};
