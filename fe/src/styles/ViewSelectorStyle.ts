import styled from "styled-components";
import { ViewType } from "@/types/View";

const ModeButton = styled.span`
    border: none;
    cursor: pointer;
    font-size: 1rem;
    padding: 1px 6px;
    
`

const GridButton = styled(ModeButton)<{ $active: ViewType }>`
    color: ${({ $active }) => $active === "grid" ? "#0064FF" : "gray"};
`;

const ListButton = styled(ModeButton)<{ $active: ViewType }>`
    color: ${({ $active }) => $active === "list" ? "#0064FF" : "gray"};
    
`;

const Container = styled.div`
    display: flex;
    justify-content: space-between;
    margin-bottom: 1.5rem;
    width: 100%
`;

const Title = styled.h1`
    font-size: 24px;
    font-weight: 700;
`;

const IconButtonGroup = styled.div`
    display: flex;
    gap: 0.5rem;
    margin-right: 0.5rem;
`;

export { GridButton, ListButton, Container, Title, IconButtonGroup };
