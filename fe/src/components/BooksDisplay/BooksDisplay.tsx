import { useEffect, useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import BookCard from "./BookCard/BookCard";
import * as S from "@/styles/BookDisplayStyle"
import { BookContent } from "@/types/Books";
import { ViewType } from "@/types/View";

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const [bookItem, setBookItem] = useState<BookContent[]>([]);

    useEffect(() => {
        const getData = async () => {
            const response = await fetch("api/books?page=0&size=20&title=지혜명상법&author=지혜명상법&publisher=지혜명상법");
            const data = await response.json();
            setBookItem(data.content)
        };
        getData();
    }, []);

    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <S.BookWrap $viewMode={viewMode}>
                {bookItem.map((book) => (
                    <BookCard key={book.id} bookData={book} viewMode={viewMode}/>
                ))}
            </S.BookWrap>
        </S.BookContainer>
    );
};

export default BooksDisplay;
