import useQueryStore from "@/stores/queryStore";
import { useEffect, useState } from "react";

const useGenerateQuery = () => {
    const { searchText, page, size, selectedValue, librariesFilter} =
        useQueryStore();
    const [query, setQuery] = useState(`books?page=${page}&size=${size}`);

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

        setQuery(`books?${queryString}`);
    }, [page, size, selectedValue, librariesFilter, searchText]);

    useEffect(() => console.log(query), [query])
    return query;
};

export default useGenerateQuery;
