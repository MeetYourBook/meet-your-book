import { useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import BookCard from "./BookCard/BookCard";
import { BookContent } from "@/types/Books";
import { ViewType } from "@/types/View";
import * as S from "@/styles/BookDisplayStyle";
import useBooksLogic from "@/hooks/useBooksLogic";

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const { booksItem, observerRef, lastPageNum, page, isLoading } =
        useBooksLogic();

    if (isLoading) return <div>Loading...</div>;
    // suspense 처리
    
    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <S.BookWrap $viewMode={viewMode}>
                {booksItem.map((book: BookContent, index: number) => (
                    <BookCard
                        key={`${book.id}-${index}`}
                        bookData={book}
                        viewMode={viewMode}
                    />
                ))}
                {page === lastPageNum ? (
                    <div>마지막 페이지 입니다.</div>
                ) : (
                    <div ref={observerRef} style={{ height: "1px" }} />
                )}
            </S.BookWrap>
        </S.BookContainer>
    );
};

export default BooksDisplay;
