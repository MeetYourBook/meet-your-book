import useQueryStore from "@/stores/queryStore";
import { useState } from "react";
import { FIRST_PAGE } from "@/constants";
export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const [search, setSearch] = useState("");
    const { librariesFilter, setLibrariesFilter, setPage } = useQueryStore();
    
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
        setSearch(e.target.value);
    };

    return {
        isOpen,
        search,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
    };
};
