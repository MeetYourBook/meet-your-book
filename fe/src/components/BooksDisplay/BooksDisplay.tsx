import { useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import BookCard from "./BookCard/BookCard";
import useGenerateQuery from "@/hooks/useGenerateQuery";
import { BookContent } from "@/types/Books";
import { ViewType } from "@/types/View";
import useQueryData from "@/hooks/useQueryData";
import * as S from "@/styles/BookDisplayStyle";

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const query = useGenerateQuery();
    const { data: books, isLoading } = useQueryData(query)

    if (isLoading) return <div>Loading...</div>; 
    // suspense 처리
    // errorBoundary 처리

    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <S.BookWrap $viewMode={viewMode}>
                {books.content.map((book: BookContent) => (
                    <BookCard
                        key={book.id}
                        bookData={book}
                        viewMode={viewMode}
                    />
                ))}
            </S.BookWrap>
        </S.BookContainer>
    );
};

export default BooksDisplay;
