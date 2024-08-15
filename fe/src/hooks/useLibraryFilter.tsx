import useQueryStore from "@/stores/queryStore";
import { useState } from "react";

export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const [search, setSearch] = useState("");
    const { librariesFilter, setLibrariesFilter } = useQueryStore();
    
    const handleSelectLibrary = (id: string) => {
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
