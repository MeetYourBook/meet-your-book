import { booksAPI } from "@/services";
import { useQuery } from "@tanstack/react-query";
import { HTTPError } from "@/components/ErrorBoundary/HTTPError";

const fetchBooks = async (query: string) => {
    const response = await booksAPI.get(query);
    if (!response.ok) {
        throw new HTTPError(response.status);
    }
    return await response.json();
};

const useBooksQuery = (query: string) => {
    return useQuery({
        queryKey: ["books", query],
        queryFn: () => fetchBooks(query),
        staleTime: Infinity,
        throwOnError: true,
    });
};

export default useBooksQuery;

