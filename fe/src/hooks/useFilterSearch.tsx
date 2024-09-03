import { useMemo, useState } from "react";
import useDebounce from "./useDebounce";
import { DEBOUNCE_TIME } from "@/constants";
import { LibraryResponse } from "@/types/Books";
import { LibrariesType } from "@/types/Libraries";

type SearchableItem = LibraryResponse | LibrariesType;

interface UseSearchFilterProps<T extends SearchableItem> {
    libraries: T[];
    keyName: keyof T & string;
}

const useSearchFilter = <T extends SearchableItem>({ 
    libraries, 
    keyName 
}: UseSearchFilterProps<T>) => {
    const [searchValue, setSearchValue] = useState("");

    const debouncedValue = useDebounce(searchValue, DEBOUNCE_TIME);

    const filteredLibraries = useMemo(() => {
        if (!debouncedValue) return libraries;

        return libraries.filter((item) => {
            const value = item[keyName];
            return typeof value === 'string' && value.toLowerCase().includes(debouncedValue.toLowerCase());
        });
    }, [debouncedValue, libraries, keyName]);

    return { searchValue, setSearchValue, filteredLibraries };
};

export default useSearchFilter;
