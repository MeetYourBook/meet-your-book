import { useEffect, useState } from "react";
import { ViewType } from "@/types/View";
import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookCardStyle";
import { ANIMATION_TIME } from "@/constants";
import BookInfoModal from "@/components/Modal/BookInfoModal/BookInfoModal";
import { handleImageError } from "@/utils";
import FavoriteBtn from "@/components/FavoriteBtn/FavoriteBtn";
interface BookItemProps {
    bookData: BookContent;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
    const { imageUrl, title, author, publisher, publishDate, libraryResponses } = bookData;
    const [isVisible, setIsVisible] = useState(false);
    const [isModalOpen, setModalOpen] = useState(false);

    const handleModalOpen = () => setModalOpen(true);

    const handleModalClose = () => setModalOpen(false);

    useEffect(() => {
        const timer = setTimeout(() => setIsVisible(true), ANIMATION_TIME);
        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            {viewMode === "grid" ? (
                <S.GridCard $isVisible={isVisible} onClick={handleModalOpen}>
                    <S.FavoritesBtnWrap>
                        <FavoriteBtn item={bookData} storageName="books" />
                    </S.FavoritesBtnWrap>
                    <S.Image
                        src={`//${imageUrl}`}
                        alt={title}
                        onError={handleImageError}
                        loading="lazy"
                    />
                    <S.TextContainer $viewMode="grid">
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                    </S.TextContainer>
                </S.GridCard>
            ) : (
                <S.ListCard $isVisible={isVisible} onClick={handleModalOpen}>
                    <S.FavoritesBtnWrap>
                        <FavoriteBtn item={bookData} storageName="books" />
                    </S.FavoritesBtnWrap>
                    <S.Image
                        src={`//${imageUrl}`}
                        alt={title}
                        onError={handleImageError}
                    />
                    <S.ListBookInfo>
                        <S.ListTitle>{title}</S.ListTitle>
                        <S.MetaInfo>저자: {author}</S.MetaInfo>
                        <S.MetaInfo>출판사: {publisher}</S.MetaInfo>
                        <S.MetaInfo>출판일: {publishDate}</S.MetaInfo>
                        <S.LibrariesCount>
                            소장 도서관: {libraryResponses.length}
                        </S.LibrariesCount>
                    </S.ListBookInfo>
                </S.ListCard>
            )}
            {isModalOpen && (
                <BookInfoModal
                    bookData={bookData}
                    handleModalClose={handleModalClose}
                />
            )}
        </>
    );
};

export default BookCard;
