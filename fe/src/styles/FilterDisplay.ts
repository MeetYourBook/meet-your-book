import styled from "styled-components";

const FilterContainer = styled.div`
    width: 240px;

    @media (max-width: 768px) {
        margin: auto;
    }
`;

const Title = styled.h1`
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 1.5rem
`;
export { FilterContainer, Title };
