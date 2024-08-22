import styled from "styled-components";

const NavContainer = styled.nav`
    width: 100%;
    border-bottom: 1px solid var(--border-color);
    padding: 1.5rem;
    margin-bottom: 4rem;
`;

const NavWrap = styled.div`
    max-width: 1075px;
    min-width: 350px;
    margin: auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
`;

const LogoWrap = styled.div`
    text-align: center;
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    padding: 0px 2px;
    gap: 3px;
    cursor: pointer;
`;

const LogoName = styled.h1`
    font-weight: 800;
    font-size: 24Px;
`;

export {
    NavContainer,
    NavWrap,
    LogoName,
    LogoWrap,
};
