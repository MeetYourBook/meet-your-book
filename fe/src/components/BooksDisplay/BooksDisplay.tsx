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

    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <S.BookWrap $viewMode={viewMode}>
                {isLoading && <div>Loading...</div>}
                {booksItem.map((book: BookContent, index: number) => (
                    <BookCard
                        key={`${book.id}-${index}`}
                        bookData={book}
                        viewMode={viewMode}
                    />
                ))}
            </S.BookWrap>
            {page === lastPageNum ? (
                <S.LastPageView style={{ margin: "30px", textAlign: "center" }}>
                    마지막 페이지 입니다.
                </S.LastPageView>
            ) : (
                <div ref={observerRef} style={{ height: "1px" }} />
            )}
        </S.BookContainer>
    );
};

export default BooksDisplay;
