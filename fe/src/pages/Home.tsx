import BooksDisplay from "@/components/BooksDisplay/BooksDisplay";
import FilterDisplay from "@/components/FilterDisplay/FilterDisplay";

const Home = () => {
    return (
        <div>
            <FilterDisplay />
            <BooksDisplay />
        </div>
    );
};

export default Home;
