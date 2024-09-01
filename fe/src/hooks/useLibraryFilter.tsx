import useQueryStore from "@/stores/queryStore";
import { useState } from "react";
import { FIRST_PAGE } from "@/constants";
import useQueryData from "./useQueryData";
import useSearchFilter from "./useFilterSearch";
export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const { librariesFilter, setLibrariesFilter, setPage } = useQueryStore();
    const {data = [], isLoading} = useQueryData("libraries")
    const { searchValue, setSearchValue, filteredLibraries } = useSearchFilter({
        libraries: data,
        keyName: 'name'
    });

    const handleSelectLibrary = (id: string) => {
        setPage(FIRST_PAGE)
        setLibrariesFilter(
            librariesFilter.includes(id)
                ? librariesFilter.filter((item) => item !== id)
                : [...librariesFilter, id]
        );
    };

    const toggleFilter = () => setIsOpen(!isOpen);

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(e.target.value);
    };

    return {
        isOpen,
        searchValue,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
        isLoading,
        filteredLibraries
    };
};
