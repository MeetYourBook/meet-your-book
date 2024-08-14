import useQueryStore from "@/stores/queryStore";
import { useEffect, useState } from "react";

export interface LibrariesType {
    id: string;
    name: string;
}

export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const [librariesItem, setLibrariesItem] = useState<LibrariesType[]>([]);
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

    useEffect(() => {
        // mock 데이터 구현 후 useQuery으로 변경
        const getData = async () => {
            const response = await fetch("api/libraries");
            const data = await response.json();
            setLibrariesItem(data);
        };
        getData();
    }, []);

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
