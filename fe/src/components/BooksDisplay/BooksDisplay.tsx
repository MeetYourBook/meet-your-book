import React, { Suspense, useState } from "react";
import ViewSelector from "./ViewSelector/ViewSelector";
import { BookContent } from "@/types/Books";
import { ViewType } from "@/types/View";
import * as S from "@/styles/BookDisplayStyle";
import useBooksLogic from "@/hooks/useBooksLogic";
import LoadingFallBack from "../LoadingFallBack/LoadingFallBack";
const BookCard = React.lazy(() => import("./BookCard/BookCard"));

const BooksDisplay = () => {
    const [viewMode, setViewMode] = useState<ViewType>("grid");
    const { booksItem, observerRefCallback, lastPageNum, page, isLoading } =
        useBooksLogic();

    return (
        <S.BookContainer>
            <ViewSelector viewMode={viewMode} setViewMode={setViewMode} />
            <Suspense fallback={<LoadingFallBack />}>
                <S.BookWrap $viewMode={viewMode}>
                    {booksItem.map((book: BookContent, index: number) => (
                        <BookCard
                            key={`${book.id}-${index}`}
                            bookData={book}
                            viewMode={viewMode}
                        />
                    ))}
                </S.BookWrap>
                {isLoading && <LoadingFallBack />}
                {page === lastPageNum ? (
                    <S.LastPageView>마지막 페이지 입니다.</S.LastPageView>
                ) : (
                    <div ref={observerRefCallback} style={{ height: "10px" }} />
                )}
            </Suspense>
        </S.BookContainer>
    );
};

export default BooksDisplay;
