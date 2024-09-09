import useQueryStore from "@/stores/queryStore";
import { useEffect, useState } from "react";

const useGenerateQuery = () => {
    const { searchText, page, size, selectedValue, librariesFilter} =
        useQueryStore();
    const [query, setQuery] = useState(`?page=${page}&size=${size}`);

    useEffect(() => {
        const queryParams = {
            page,
            size,
            ...(searchText && selectedValue === "all" ? 
                {
                    title: searchText,
                    author: searchText,
                    publisher: searchText,
                }
                : searchText && { [selectedValue]: searchText }),
            ...(librariesFilter.length > 0 && {libraries: librariesFilter.join(",")}),
        };

        const queryString = Object.entries(queryParams)
            .map(([key, value]) => `${key}=${value}`)
            .join("&");

        setQuery(`?${queryString}`);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page, size, librariesFilter, searchText]);

    return query;
};

export default useGenerateQuery;
