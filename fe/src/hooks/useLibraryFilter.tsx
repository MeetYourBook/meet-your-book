import useQueryStore from "@/stores/queryStore";
import { useState, useMemo, useRef, useEffect } from "react";
import { FIRST_PAGE } from "@/constants";
import useQueryData from "./useQueryData";
import useSearchFilter from "./useFilterSearch";
import { getCurrentPageItems } from "@/utils";
import { LIBRARIES_FILTER_PER_PAGE, PAGINATION_FIRST_PAGE } from "@/constants";
import useInfiniteScroll from "./useInfiniteScroll";
import { LibrariesType } from "@/types/Libraries";

export const useLibraryFilter = () => {
    const observerRef = useRef(null);
    const [isOpen, setIsOpen] = useState(true);
    const [filterPage, setFilterPage] = useState(PAGINATION_FIRST_PAGE);
    const [disPlayLibraries, setDisplayLibraries] = useState<LibrariesType[]>([])
    const { librariesFilter, setLibrariesFilter, setPage } = useQueryStore();
    const { data = [], isLoading } = useQueryData("libraries");
    const { searchValue, setSearchValue, filteredLibraries } = useSearchFilter({
        libraries: data,
        keyName: "name",
    });

    const getDisplayLibraries = useMemo(() => {
        if (searchValue) return filteredLibraries;
        return disPlayLibraries;
    }, [searchValue, filteredLibraries, disPlayLibraries]);

    const handleSelectLibrary = (id: string) => {
        setPage(FIRST_PAGE);
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

    const handleLoadMore = () => setFilterPage(prev => prev + 1);

    const { observe } = useInfiniteScroll(handleLoadMore);

    useEffect(() => {
        if(data.length > 0) {
        const newPageData: LibrariesType[] = getCurrentPageItems(data, filterPage, LIBRARIES_FILTER_PER_PAGE)
        setDisplayLibraries(prevLibraries => [...prevLibraries, ...newPageData]) 
        }   
    }, [data, filterPage])

    useEffect(() => {
        if (observerRef.current) {
            observe(observerRef.current);
        }
    }, [observe]);

    return {
        isOpen,
        searchValue,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
        isLoading,
        getDisplayLibraries,
        observerRef,
    };
};
