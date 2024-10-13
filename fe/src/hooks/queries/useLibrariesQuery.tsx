import { librariesAPI } from "@/services";
import { useQuery } from "@tanstack/react-query";

const fetchLibraries = async (query: string) => {
    const response = await librariesAPI.get(query)
    if (!response.ok) {
        throw new Error("libraries error")
    }
    const data = await response.json()
    return data
}

const useLibrariesQuery = (query: string) => {
    return useQuery({
        queryKey: ["libraries", query],
        queryFn: () => fetchLibraries(query), 
        staleTime: Infinity,
    });
}

export default useLibrariesQuery