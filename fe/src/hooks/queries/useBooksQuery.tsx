import { booksAPI } from "@/services";
import { useQuery } from "@tanstack/react-query";

const fetchBooks = async (query: string) => {
    const response = await booksAPI.get(query)
    if (!response.ok) {
        throw new Error("books error")
    }
    const data = await response.json()
    return data
}

const useBooksQuery = (query: string) => {
    return useQuery({
        queryKey: ["books", query],
        queryFn: () => fetchBooks(query),
        staleTime: Infinity, 
    });
}

export default useBooksQuery