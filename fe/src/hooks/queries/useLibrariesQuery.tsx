import { librariesAPI } from "@/services";
import { useQuery } from "@tanstack/react-query";

const fetchLibraries = async () => {
    const response = await librariesAPI.get()
    if (!response.ok) {
        throw new Error("libraries error")
    }
    const data = await response.json()
    return data
}

const useLibrariesQuery = () => {
    return useQuery({
        queryKey: ["libraries"],
        queryFn: () => fetchLibraries(), 
    });
}

export default useLibrariesQuery