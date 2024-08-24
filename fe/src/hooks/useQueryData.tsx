import { useQuery } from "@tanstack/react-query";
import { fetchAPI } from "@/services/fetch";
import { get } from "@/services/APIOptions";

const useQueryData = (query: string) => {
    return useQuery({
        queryKey: [query],
        queryFn: () => fetchAPI(query, get()), 
    });
};

export default useQueryData;
