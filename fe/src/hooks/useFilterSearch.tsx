import { useMemo, useState } from "react";
import useDebounce from "./useDebounce";
import { DEBOUNCE_TIME } from "@/constants";
import { LibrariesResponseType } from "@/types/LibrariesResponse";
import { LibrariesType } from "@/types/Libraries";

type SearchableItem = LibrariesResponseType | LibrariesType;

interface UseSearchFilterProps {
    libraries: SearchableItem[];
    keyName: "name" | "LibraryName";
}

const useSearchFilter = ({ 
    libraries, 
    keyName 
}: UseSearchFilterProps) => {
    const [searchValue, setSearchValue] = useState("");

    const debouncedValue = useDebounce(searchValue, DEBOUNCE_TIME);

    const filteredLibraries = useMemo(() => {
        if (!debouncedValue) return libraries;

        return libraries.filter((item) => {
            const value = item[keyName as keyof SearchableItem];
            return typeof value === 'string' && value.toLowerCase().includes(debouncedValue.toLowerCase());
        });
    }, [debouncedValue, libraries, keyName]);

    return { searchValue, setSearchValue, filteredLibraries };
};

export default useSearchFilter;