import styled from "styled-components";

const NavContainer = styled.nav`
    width: 100%;
    border-bottom: 1px solid var(--border-color);
    padding: 1rem;
    margin-bottom: 4rem;
`;

const NavWrap = styled.div`
    max-width: 1000px;
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
`;

const LogoAbbreviation = styled.h1`
    font-weight: 700;
    font-size: 24Px;
`;

const LogoFullName = styled.p`
    font-weight: 300;
    font-size: 0.8rem;
    font-style: italic;
`;

const InputWrap = styled.div`
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
    border: 1px solid var(--border-color);
    border-radius: 8px;
    max-width: 600px;
    min-width: 300px;
    margin: 0 20px;
    display: flex;
    align-items: center;
    height: 2.4rem;
`;

const LoginBtn = styled.div`
    cursor: pointer;
    font-size: 0.9rem;
    margin-right: 2rem;
`

export {
    NavContainer,
    NavWrap,
    LogoAbbreviation,
    LogoFullName,
    LogoWrap,
    InputWrap,
    LoginBtn
};
