import styled from "styled-components";
import { SearchOutlined, FilterOutlined } from "@ant-design/icons";

const PanelContainer = styled.div`
    display: none;
    padding: 0 16px;
    @media (max-width: 1000px) {
        display: flex;
        gap: 16px;
        padding: 0 24px;
    }
`;

const CustomSearchIcon = styled(SearchOutlined)`
    font-size: 20px;
    cursor: pointer;
`;

const CustomFilterIcon = styled(FilterOutlined)`
    font-size: 20px;
    cursor: pointer;
`;

const FilterWrap = styled.div<{ $isFilterOpen: boolean }>`
    position: fixed;
    z-index: 50;
    width: 300px;
    top: 0;
    height: 100%;
    padding: 20px;
    background-color: ${({ theme }) => theme.body};
    right: ${({ $isFilterOpen }) => ($isFilterOpen ? "0" : "-100%")};
    box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);
    transition: right 0.3s ease;
    box-sizing: border-box;
    overflow-y: auto;
`;

const Overlay = styled.div`
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 40;
    backdrop-filter: blur(1px);
`;

const CancelBtn = styled.div`
    display: flex;
    align-items: center;
    justify-content: right;
    padding: 5px;
    top: 20px;
    right: 20px;
    
    cursor: pointer;
`;

const SearchWrap = styled.div<{$isSearchOpen: boolean}>`
    position: fixed;
    display: flex;
    justify-content: center;
    z-index: 50;
    width: 100%;
    left: 0;
    top: ${({ $isSearchOpen }) => ($isSearchOpen ? "0" : "-100%")};
    height: 80px;
    padding: 20px;
    background-color: ${({ theme }) => theme.body};
    box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);
    transition: top 0.3s ease;
    box-sizing: border-box;
`;

export {
    PanelContainer,
    CustomSearchIcon,
    CustomFilterIcon,
    FilterWrap,
    Overlay,
    CancelBtn,
    SearchWrap,
};
