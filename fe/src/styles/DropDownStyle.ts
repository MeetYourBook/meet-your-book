import styled, { keyframes } from "styled-components";

const DropdownContainer = styled.div`
    position: relative;
    padding: 2px;
    width: 5rem;
    height: 100%;
    font-size: 0.9rem;
    text-align: center;
    font-weight: 400;
    display: flex;
    align-items: center;
`;

const dropdownAnimation = keyframes`
    0% {
        opacity: 0;
        transform: translateY(-10px);
    }
    100% {
        opacity: 1;
        transform: translateY(0);
    }
`;

const DropdownList = styled.ul`
    overflow: hidden;
    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 8px;
    z-index: 10;
    list-style: none;
    margin: 0;
    padding: 0;
    animation: ${dropdownAnimation} 0.3s ease-out;
`;

const DropdownItem = styled.li`
    padding: 8px;
    margin: auto;
    color: gray;
    font-size: 0.9rem;
    cursor: pointer;
    &:hover {
        background-color: #f0f0f0;
    }
`;

const SelectedItemText = styled.p`
    margin: auto;
    font-weight: 400;
`

export { DropdownContainer, DropdownList, DropdownItem, SelectedItemText };
