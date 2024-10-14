import { librariesAPI } from "@/services";
import { useQuery } from "@tanstack/react-query";
import { HTTPError } from "async-error-boundary";

const fetchLibraries = async (query: string) => {
    const response = await librariesAPI.get(query)
    if (!response.ok) {
        throw new HTTPError(response.status)
    }
    const data = await response.json()
    return data
}

const useLibrariesQuery = (query: string) => {
    return useQuery({
        queryKey: ["libraries", query],
        queryFn: () => fetchLibraries(query), 
        staleTime: Infinity,
        throwOnError: true,
    });
}

export default useLibrariesQuery