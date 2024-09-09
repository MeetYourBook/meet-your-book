import useQueryStore from "@/stores/queryStore";
import { CloseOutlined } from "@ant-design/icons";
import { useMemo } from "react";
import styled from "styled-components";

const FilterStatusBar = () => {
    const { searchText, selectedValue, librariesFilter, resetFilter } = useQueryStore();

    const currentFilterState = useMemo(() => {
        const stateObject = {
            ...(searchText && { [selectedValue]: searchText }),
            ...(librariesFilter.length > 0 && {
                libraries: librariesFilter.map((item) => item.name).join(","),
            }),
        };
        return Object.entries(stateObject)
            .map(([key, value]) => `${key}=${value}`)
            .join("/");
    }, [searchText, selectedValue, librariesFilter]);

    const handleFilterReset = () => resetFilter()

    return (
        <FilterStatusBarContainer>
            <StateWrap>home/ books/ {currentFilterState}</StateWrap>
            {currentFilterState !== "" && (
                <FilterResetBtn onClick={handleFilterReset} />
            )}
        </FilterStatusBarContainer>
    );
};

export default FilterStatusBar;

const FilterStatusBarContainer = styled.nav`
    width: 100%;
    max-width: 1020px;
    min-width: 350px;
    margin: 1rem auto 2rem;
    display: flex;
    gap: 1rem;
    @media (max-width: 1000px) {
        width: 780px;
    }
    @media (max-width: 768px) {
        width: 100%;
        margin: 1rem auto 0;
    }
    @media (max-width: 600px) {
        width: 363px;
    }
`;

const StateWrap = styled.div`
    font-size: 14px;

    display: flex;
    align-items: center;
    justify-content: space-between;
`;

const FilterResetBtn = styled(CloseOutlined)`
    font-size: 14px;
    cursor: pointer;
`;
