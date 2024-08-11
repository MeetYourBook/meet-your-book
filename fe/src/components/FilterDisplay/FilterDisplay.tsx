import * as S from "@/styles/FilterDisplay";
import LibraryFilter from "./LibraryFilter/LibraryFilter";

const FilterDisplay = () => {
    return (
        <S.FilterContainer>
            <S.Title>Filter Option</S.Title>
            <LibraryFilter />
            {/* 다른 필터 나오면 추가 */}
        </S.FilterContainer>
    );
};

export default FilterDisplay;
