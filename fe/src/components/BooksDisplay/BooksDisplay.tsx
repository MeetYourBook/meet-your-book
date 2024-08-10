import { useEffect, useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import BookCard from "./BookCard/BookCard";
import * as S from "@/styles/BookDisplayStyle"

export type ViewType = "grid" | "list";
export interface Book {
    id: string;
    title: string;
    author: string;
    provider: string;
    publisher: string;
    publish_date: string;
    image_url: string;
}

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const [bookItem, setBookItem] = useState<Book[]>([]);

    useEffect(() => {
        const getData = async () => {
            const response = await fetch("api/books");
            const data = await response.json();
            setBookItem(data)
        };
        getData();
    }, []);

    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <S.BookWrap $viewMode={viewMode}>
                {bookItem.map((cur) => (
                    <BookCard key={cur.id} bookData={cur} viewMode={viewMode}/>
                ))}
            </S.BookWrap>
        </S.BookContainer>
    );
};

export default BooksDisplay;
