import BooksDisplay from "@/components/BooksDisplay/BooksDisplay";
import FilterDisplay from "@/components/FilterDisplay/FilterDisplay";
import styled from "styled-components";

const Home = () => {
    return (
        <HomeContainer>
            <FilterDisplay />
            <BooksDisplay />
        </HomeContainer>
    );
};

export default Home;

const HomeContainer = styled.main`
    max-width: 1200px;
    height: 100%;
    display: flex;
    justify-content: center;
    gap: 2rem;
    margin: 0px auto
`;
