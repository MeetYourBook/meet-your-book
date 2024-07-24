import { useState } from "react";
import { useQuery } from "react-query";
import { getData } from "../../services/bookFetch";
import BookItem from "./BookItem/BookItem";
import ViewSelector from "./ViewSelector/ViewSelector";

export type ViewType = "grid" | "list"

export interface BookType {
    id: string;
    title: string;
    author: string;
    provider: string;
    publisher: string;
    publish_date: string;
    image_url: string;
}

const BookDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid")
    const { data, isLoading, error } = useQuery({
        queryKey: ["bookList"],
        queryFn: () => getData(),
    });

    return (
        <section className="w-[650px] mx-auto">
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode}/>
            <ul className={`${viewMode === "grid" ? "grid grid-cols-4 gap-4" : "flex flex-col gap-1"}`}>
                {data &&
                    data.map((book: BookType) => (
                        <BookItem key={book.id} bookData={book} viewMode={viewMode}/>
                    ))}
            </ul>
        </section>
    );
};

export default BookDisplay;

// <Pagination/>
