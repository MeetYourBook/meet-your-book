import useQueryStore from "@/stores/queryStore";
import { useEffect, useState } from "react";

export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const [librariesItem, setLibrariesItem] = useState<string[]>([]);
    const [search, setSearch] = useState("");
    const { librariesFilter, setLibrariesFilter } = useQueryStore();

    const handleSelectLibrary = (library: string) => {
        setLibrariesFilter(
            librariesFilter.includes(library)
                ? librariesFilter.filter((item) => item !== library)
                : [...librariesFilter, library]
        );
    };

    const toggleFilter = () => setIsOpen(!isOpen);

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        // mock 데이터 구현 후 useQuery으로 변경
        const getData = async () => {
            const response = await fetch("api/library");
            const data = await response.json();
            setLibrariesItem(data);
        };
        getData();
    }, []);

    useEffect(() => {
        console.log(librariesFilter)
    }, [librariesFilter])

    return {
        isOpen,
        librariesItem,
        search,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
    };
};
