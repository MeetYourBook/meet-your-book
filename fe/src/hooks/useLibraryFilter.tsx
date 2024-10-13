import useQueryStore from "@/stores/queryStore";
import { useState, useMemo, useRef, useEffect } from "react";
import { FIRST_PAGE, LIBRARIES_DEFAULT_QUERY, LIBRARIES_PAGE_SIZE } from "@/constants";
import useInfiniteScroll from "./useInfiniteScroll";
import { Libraries } from "@/types/Libraries";
import useLibrariesQuery from "./queries/useLibrariesQuery";

export const useLibraryFilter = () => {
    const [isOpen, setIsOpen] = useState(true);
    const [debounceValue, setDebounceValue] = useState("")
    const [libraryPage, setLibraryPage] = useState(FIRST_PAGE)
    const [query, setQuery] = useState<string>(LIBRARIES_DEFAULT_QUERY)
    const { librariesFilter, setLibrariesFilter, setPage } = useQueryStore();
    const { data: libraries, isLoading } = useLibrariesQuery(query);
    const [librariesItem, setLibrariesItem] = useState<Libraries[]>([])
    const observerRef = useRef(null);

    const totalPages = useMemo(() => {
        if(libraries && libraries.totalPages) return libraries.totalPages
    }, [libraries])
    

    const handleSelectLibrary = (library: Libraries) => {
        setPage(FIRST_PAGE);
        setLibrariesFilter(
            librariesFilter.some(curLibrary => curLibrary.id === library.id)
                ? librariesFilter.filter((item) => item.id !== library.id)
                : [...librariesFilter, library]
        );
    };

    const toggleFilter = () => setIsOpen(!isOpen);

    const handleLoadMore = () => setLibraryPage(prev => prev + 1);

    const { observe } = useInfiniteScroll(handleLoadMore);

    useEffect(() => {
        const queryParams = { name: debounceValue === "" ? "서울" : debounceValue, page: libraryPage, size: LIBRARIES_PAGE_SIZE}
        const queryString = Object.entries(queryParams).map(([key, value]) => `${key}=${value}`).join("&")
        setQuery(`?${queryString}`)
    }, [debounceValue, libraryPage])

    useEffect(() => {
        if(libraries && libraries.content) 
        setLibrariesItem(libraryPage === FIRST_PAGE ? libraries.content : [...librariesItem, ...libraries.content])
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [libraries, libraryPage])

    useEffect(() => {
        if (observerRef.current) {
            observe(observerRef.current);
        }
    }, [observe]);

    return {
        isOpen,
        setDebounceValue, 
        toggleFilter,
        setLibraryPage,
        librariesItem,
        isLoading,
        librariesFilter,
        handleSelectLibrary,
        observerRef,
        totalPages,
        libraryPage,
    };
};
