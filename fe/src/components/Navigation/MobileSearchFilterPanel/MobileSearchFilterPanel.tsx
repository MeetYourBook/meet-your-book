import FilterDisplay from "@/components/FilterDisplay/FilterDisplay";
import * as S from "@/styles/MobileSearchFilterPanelStyle";
import { useState } from "react";
import SearchInput from "../SearchInput/SearchInput";
import ThemeSwitcher from "../ThemeSwitcher/ThemeSwitcher";

const MobileSearchFilterPanel = () => {
    const [isFilterOpen, setFilterOpen] = useState(false);
    const [isSearchOpen, setSearchOpen] = useState(false);

    const closePanels = () => {
        setFilterOpen(false);
        setSearchOpen(false);
    };
    return (
        <S.PanelContainer>
            <S.CustomSearchIcon onClick={() => setSearchOpen(!isSearchOpen)} />
            <S.CustomFilterIcon onClick={() => setFilterOpen(!isFilterOpen)} />

            {(isFilterOpen || isSearchOpen) && (
                <S.Overlay onClick={closePanels} />
            )}
            
            <S.FilterWrap $isFilterOpen={isFilterOpen}>
                <S.CancelBtn onClick={() => setFilterOpen(false)}>
                    X
                </S.CancelBtn>
                <FilterDisplay />
            </S.FilterWrap>

            <S.SearchWrap $isSearchOpen={isSearchOpen}>
                <SearchInput />
                <S.CancelBtn onClick={() => setSearchOpen(false)}>
                    X
                </S.CancelBtn>
            </S.SearchWrap>
            <ThemeSwitcher/>
        </S.PanelContainer>
    );
};

export default MobileSearchFilterPanel