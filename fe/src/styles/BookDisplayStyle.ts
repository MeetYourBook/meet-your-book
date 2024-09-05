import styled from "styled-components";
import { ViewType } from "@/types/View";

const BookContainer = styled.div`
width: 750px;
@media (max-width: 768px) {
    width: 100%;
    padding: 20px;
}
@media (max-width: 600px) {
    max-width: 330px;
    margin: 0 auto;
}
`;

const BookWrap = styled.div<{ $viewMode: ViewType }>`
    display: ${({ $viewMode }) => ($viewMode === "grid" ? "grid" : "block")};
    grid-template-columns: ${({ $viewMode }) =>
        $viewMode === "grid" ? "repeat(4, 1fr)" : "none"};
    gap: 15px;
    justify-content: center;
    @media (max-width: 600px) {
        grid-template-columns: repeat(2, 1fr);
        gap: 10px;
    }
`;

const LastPageView = styled.div`
    margin: 30px;
    text-align: center;
`

export { BookContainer, BookWrap, LastPageView };
