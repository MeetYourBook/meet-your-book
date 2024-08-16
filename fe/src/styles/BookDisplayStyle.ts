import styled from "styled-components";
import { ViewType } from "./ViewSelectorStyle";

const BookContainer = styled.div`
    width: 750px;
    min-width: 750px;
    
`;

const BookWrap = styled.div<{ $viewMode: ViewType }>`
    display: ${({ $viewMode }) => ($viewMode === "grid" ? "grid" : "block")};
    grid-template-columns: ${({ $viewMode }) =>
        $viewMode === "grid" ? "repeat(4, 1fr)" : "none"};
    gap: 15px;
`;

export { BookContainer, BookWrap };
