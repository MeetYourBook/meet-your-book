import { useEffect, useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import BookCard from "./BookCard/BookCard";
import * as S from "@/styles/BookDisplayStyle"
import { BookContent } from "@/types/Books";
import { ViewType } from "@/types/View";
import useGenerateQuery from "@/hooks/useGenerateQuery";

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const [bookItem, setBookItem] = useState<BookContent[]>([]);
    const query = useGenerateQuery();

    useEffect(() => {
        const getData = async () => {
            const response = await fetch(`api/${query}`);
            const data = await response.json();
            setBookItem(data.content)
        };
        getData();
    }, [query]);

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
