import styled from "styled-components";
import { MenuOutlined, RedoOutlined } from "@ant-design/icons";

const AdminContainer = styled.div`
    width: 100%;
    height: 100%;
    max-width: 100vw;
`;

const AdminNav = styled.div`
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    box-sizing: border-box;
    border-bottom: 1px solid var(--border-color);
`;

const NavInfoWrap = styled.span`
    display: flex;
    gap: 10px;
    align-items: center;
`;

const MenuIcon = styled(MenuOutlined)`
    cursor: pointer;
    font-size: 16px;
`;

const CurrentActive = styled.h2`
    font-weight: bold;
    margin: 0;
    font-size: 1.2rem;
`;

const ReloadBtn = styled(RedoOutlined)`
    cursor: pointer;
    font-size: 16px;
`;

const SideBarContainer = styled.ul<{ $isOpen: boolean }>`
    font-size: 14px;
    width: 150px;
    height: 100vh;
    border-right: 1px solid var(--border-color);
    display: flex;
    flex-direction: column;
    gap: 13px;
    padding: 20px;
    transform: ${({ $isOpen }) =>
        $isOpen ? "translateX(0)" : "translateX(-100%)"};
    transition: transform 0.3s ease-in-out;
`;

export {
    AdminContainer,
    AdminNav,
    NavInfoWrap,
    MenuIcon,
    CurrentActive,
    ReloadBtn,
    SideBarContainer,
};
